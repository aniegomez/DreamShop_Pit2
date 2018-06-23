package com.dreamteam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.bean.ClienteBean;
import com.dreamteam.bean.Cliente_PedidosBean;
import com.dreamteam.bean.ColorBean;
import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.bean.Detalle_PedidoBean;
import com.dreamteam.bean.Detalle_ProductoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.PedidoBean;
import com.dreamteam.bean.ProductoBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.dao.PedidoDAO;
import com.dreamteam.utilitarios.Conexion;
import com.dreamteam.utilitarios.EmailService;

@Repository
public class PedidoDAOImpl implements PedidoDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;

	@Autowired
	EmailService emailService;	
	private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta, parametros;
	private int idPedido = Types.INTEGER;
	private int cantidad = Types.INTEGER;
	
	@Override
	public Map<String, Object> createPedido (PedidoBean pedidoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);			
			
			sql = "INSERT INTO PEDIDO VALUES(NULL, ?,CURRENT_DATE,?,3)";
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Total", 		 1, pedidoBean.getTotal());
			pstm = Conexion.validarNulo(pstm, "Id. Cliente", 2, Conexion.validarNulo_Bean( pedidoBean.getIdClienteBean() ) ? null : pedidoBean.getIdClienteBean().getIdCliente());
			
			pstm.executeUpdate();
			
			rst = pstm.getGeneratedKeys();
			
			while(rst.next())
				idPedido = rst.getInt(1);
			
			for(Detalle_PedidoBean x:pedidoBean.getListaDetallePedido())
			{
				if( Conexion.validarNulo_Bean(x.getDetalle_ProductoBean()) )
					throw new NullPointerException("Detalle Producto está vacío");
				
				sql = "SELECT CANTIDAD FROM DETALLE_PRODUCTO WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
				pstm = conexion.prepareStatement(sql);
				pstm = Conexion.validarNulo(pstm, "idProducto", 1, Conexion.validarNulo_Bean(x.getDetalle_ProductoBean().getIdProductoBean()) ? null : x.getDetalle_ProductoBean().getIdProductoBean().getIdProducto());
				pstm = Conexion.validarNulo(pstm, "idColor", 	2, Conexion.validarNulo_Bean(x.getDetalle_ProductoBean().getIdColorBean()) ? null : x.getDetalle_ProductoBean().getIdColorBean().getIdColor());
				
				rst = pstm.executeQuery();
				
				cantidad = 0;
				while(rst.next())
					cantidad = rst.getInt(1);
				
				if( cantidad <= 0)					
					throw new Exception("Uno o más de los productos elegidos ya no se encuentra en stock");
				else if( cantidad - x.getCantidad() < 0)
					throw new Exception("Uno o más de los productos elegidos no tiene suficiente stock.");
				
				sql = "INSERT INTO DETALLE_PEDIDO VALUES(?,?,?,?,?,?,?)";
				pstm = conexion.prepareStatement(sql);
				pstm = Conexion.validarNulo(pstm, "idPedido", 		1, idPedido);
				pstm = Conexion.validarNulo(pstm, "idProducto", 	2, Conexion.validarNulo_Bean(x.getDetalle_ProductoBean().getIdProductoBean()) ? null : x.getDetalle_ProductoBean().getIdProductoBean().getIdProducto());
				pstm = Conexion.validarNulo(pstm, "idColor", 		3, Conexion.validarNulo_Bean(x.getDetalle_ProductoBean().getIdColorBean()) ? null : x.getDetalle_ProductoBean().getIdColorBean().getIdColor());
				pstm = Conexion.validarNulo(pstm, "Cantidad", 		4, x.getCantidad());
				pstm = Conexion.validarNulo(pstm, "Precio", 		5, x.getPrecio());
				pstm = Conexion.admitirNulo(pstm, "Id. Descuento", 	6, Conexion.validarNulo_Bean(x.getDetalle_ProductoBean().getIdDescuentoBean()) ? null: x.getDetalle_ProductoBean().getIdDescuentoBean().getIdDescuento(), Integer.class);
				pstm = Conexion.validarNulo(pstm, "Subtotal", 		7, x.getSubTotal());
				
				if( pstm.executeUpdate() > 0 )
				{
					sql = "UPDATE DETALLE_PRODUCTO SET CANTIDAD = CANTIDAD - ? WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
					pstm = conexion.prepareStatement(sql);
					pstm.setInt(1, x.getCantidad());
					pstm.setInt(2, x.getDetalle_ProductoBean().getIdProductoBean().getIdProducto());
					pstm.setInt(3, x.getDetalle_ProductoBean().getIdColorBean().getIdColor());
					pstm.executeUpdate();
				}
			}
			
			new Thread( () ->
			{
				try
				{
					if( pstm.getUpdateCount() >= 1 )
					{
						//emailAutogenerado = String.format("DreamShop:\nLe damos la bienvenida a nuestra tienda virtual.\n%s",
						//		"Recuerde que usted es importante para nosotros cualquier duda contactarse con nuestor centro de atenciÃ³n al pedido");
						//emailService.enviarMensaje(pedidoBean.getCorreo(), "DreamShop", emailAutogenerado);
					}
				}
				catch (Exception e) {}
			}).start();
			
			respuesta = Conexion.getRespuestaOK();
			
			conexion.commit();
		}
		catch (SQLException e)
		{
			Conexion.rollback(conexion);
			respuesta = Conexion.getErrorSQL(e);
		}
		catch (Exception e)
		{			
			Conexion.rollback(conexion);
			respuesta = Conexion.getError(e);
		}
		finally{Conexion.closeConexion(conexion, rst, pstm);}
		
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PedidoBean> readPedidoPorCliente(String fecha, int idEstado, int idCliente)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PEDIDO_CLIENTE").returningResultSet("rst", new RowMapper<PedidoBean>()
		{
			@Override
			public PedidoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return PedidoBean.builder()
						.idPedido(rst.getInt("idPedido"))
						.total(rst.getDouble("total"))
						.fecha_pedido(rst.getString("fecha_pedido"))
						.idEstadoBean(EstadoBean.builder()
										.idEstado(rst.getInt("idEstado"))
										.descripcion(rst.getString("estado"))
										.build())
						.build();
			}
		});
		
		try		
		{
			if(fecha.isEmpty() )
				fecha = null;
			else
				formatoFecha.parse(fecha);
		}
		catch (Exception e) {return new ArrayList<>();}	
		
		parametros = new HashMap<String, Object>();
		parametros.put("_fecha", fecha);
		parametros.put("_idEstado", idEstado);
		parametros.put("_idCliente", idCliente);
		
		return (List<PedidoBean>) jdbcCall.execute(parametros).get("rst");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Detalle_PedidoBean> readDetallePedido(int idPedido)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_GET_DETALLE_PEDIDO").returningResultSet("rst", new RowMapper<Detalle_PedidoBean>()
		{
			@Override
			public Detalle_PedidoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return Detalle_PedidoBean.builder()
							.idPedido(PedidoBean.builder()
										.idPedido(rst.getInt("idPedido"))
										.total(rst.getDouble("total"))
										.fecha_pedido(rst.getString("fecha_pedido"))
										.idEstadoBean(EstadoBean.builder()
														.idEstado(rst.getInt("idEstado"))
														.descripcion(rst.getString("estado"))
														.build())
										.build())
							.detalle_ProductoBean(Detalle_ProductoBean.builder()
									.idProductoBean(ProductoBean.builder()
														.idProducto(rst.getInt("idProducto"))
														.descripcion(rst.getString("producto"))
														.idMarcaBean(MarcaBean.builder()
																		.idMarca(rst.getInt("idMarca"))
																		.descripcion(rst.getString("marca"))
																		.build())
														.idCategoriaBean(CategoriaBean.builder()
																			.idCategoria(rst.getInt("idCategoria"))
																			.descripcion(rst.getString("categoria"))
																			.build())
														.build())
									.idColorBean(ColorBean.builder()
													.idColor(rst.getInt("idColor"))
													.descripcion(rst.getString("color"))
													.build())
									.idDescuentoBean(DescuentoBean.builder()
																	.idDescuento(rst.getInt("idDescuento"))
																	.porcentaje(rst.getDouble("porcentaje"))
																	.build())
									.build())									
							.cantidad(rst.getInt("cantidad")).precio(rst.getDouble("precio"))
							.subTotal(rst.getDouble("subTotal"))
							.build();
			}
		});	
		
		parametros = new HashMap<String, Object>();
		parametros.put("_idPedido", idPedido);
		
		return (List<Detalle_PedidoBean>) jdbcCall.execute(parametros).get("rst");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PedidoBean> readPedidoFiltros(String fecha, int idEstado, String cliente)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PEDIDOS_FILTROS").returningResultSet("rst", new RowMapper<PedidoBean>()
		{
			@Override
			public PedidoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return PedidoBean.builder()
						.idPedido(rst.getInt("idPedido"))
						.total(rst.getDouble("total"))
						.fecha_pedido(rst.getString("fecha_pedido"))
						.idEstadoBean(EstadoBean.builder()
										.idEstado(rst.getInt("idEstado"))
										.descripcion(rst.getString("estado"))
										.build())
						.idClienteBean(ClienteBean.builder()
											.idCliente(rst.getInt("idCliente"))
											.nombre(rst.getString("cliente"))
											.nro_documento(rst.getString("nro_documento"))
											.build())
						.build();
			}
		});
		
		try		
		{
			if(fecha.isEmpty() )
				fecha = null;
			else
				formatoFecha.parse(fecha);
		}
		catch (Exception e) {return new ArrayList<>();}	
		
		parametros = new HashMap<String, Object>();
		parametros.put("_fecha", fecha);
		parametros.put("_idEstado", idEstado);
		parametros.put("_cliente", cliente);
		
		return (List<PedidoBean>) jdbcCall.execute(parametros).get("rst");
	}

	@Override
	public Map<String, Object> estadoPedido(int idEstado, List<Integer> listaPedido)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);			
			
			for(Integer idPedido:listaPedido)
			{
				sql = "UPDATE PEDIDO SET IDESTADO = ? WHERE IDPEDIDO = ?";
				pstm = conexion.prepareStatement(sql);
				pstm = Conexion.validarNulo(pstm, "Id. Estado", 1, idEstado);
				pstm = Conexion.validarNulo(pstm, "Id. Pedido", 2, idPedido);
				pstm.executeUpdate();
			}							
						
			respuesta = Conexion.getRespuestaOK();
			
			conexion.commit();
		}
		catch (SQLException e)
		{
			Conexion.rollback(conexion);
			respuesta = Conexion.getErrorSQL(e);
			e.printStackTrace();
		}
		catch (Exception e)
		{			
			Conexion.rollback(conexion);
			respuesta = Conexion.getError(e);
			e.printStackTrace();
		}
		finally{Conexion.closeConexion(conexion, rst, pstm);}
		
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente_PedidosBean> readClientes()
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_CLIENTES").returningResultSet("rst", new RowMapper<Cliente_PedidosBean>()
		{
			@Override
			public Cliente_PedidosBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return Cliente_PedidosBean.builder()						
							.idClienteBean(ClienteBean.builder()
												.idCliente(rst.getInt("idCliente"))
												.nombre(rst.getString("nombre"))
												.apellido_p(rst.getString("apellido_p"))
												.apellido_m(rst.getString("apellido_m"))
												.idTipoDocumentoBean(TipoDocumentoBean.builder()
																		.idTipoDocumento(rst.getInt("idTipoDocumento"))
																		.descripcion(rst.getString("tipoDocumento"))
																		.build())
												.nro_documento(rst.getString("nro_documento"))
												.correo(rst.getString("correo"))
												.idEstadoCivilBean(EstadoCivilBean.builder()
																		.idEstadoCivil(rst.getInt("idEstadoCivil"))
																		.descripcion(rst.getString("estadoCivil"))
																		.build())
												.idEstadoBean(EstadoBean.builder()
																.idEstado(rst.getInt("idEstado"))
																.descripcion(rst.getString("estado"))
																.build())
												.fe_crea(rst.getString("fe_crea"))
												.telefono(rst.getString("telefono"))
												.build())
							.pedidos(rst.getInt("pedidos"))
							.build();
			}
		});
		
		return (List<Cliente_PedidosBean>) jdbcCall.execute().get("rst");
	}
}