package com.dreamteam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.dreamteam.bean.ColorBean;
import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.bean.Detalle_ProductoBean;
import com.dreamteam.bean.FavoritoBean;
import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.ProductoBean;
import com.dreamteam.dao.FavoritoDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class FavoritoDAOImpl implements FavoritoDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta = null, parametros = null;
	
	@Override
	public Map<String, Object> createFavorito(FavoritoBean favoritoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			if( favoritoBean.getDetalle_ProductoBean() == null )
				throw new NullPointerException("Detalle Producto está vacío");
			
			sql = "INSERT INTO FAVORITO VALUES(?,?,?)";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Id. Cliente",  1,  Conexion.validarNulo_Bean(favoritoBean.getIdClienteBean()) ? null : favoritoBean.getIdClienteBean().getIdCliente() );
			pstm = Conexion.validarNulo(pstm, "Id. Producto", 2,  Conexion.validarNulo_Bean(favoritoBean.getDetalle_ProductoBean().getIdProductoBean()) ? null : favoritoBean.getDetalle_ProductoBean().getIdProductoBean().getIdProducto() );
			pstm = Conexion.validarNulo(pstm, "Id. Color", 	  3,  Conexion.validarNulo_Bean(favoritoBean.getDetalle_ProductoBean().getIdColorBean()) ? null : favoritoBean.getDetalle_ProductoBean().getIdColorBean().getIdColor() );			
			
			pstm.executeUpdate();
			
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
	public List<FavoritoBean> readFavoritoPorCliente(int idCliente)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_FAVORITO_X_CLIENTE").returningResultSet("rst", new RowMapper<FavoritoBean>()
		{
			@Override
			public FavoritoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return FavoritoBean.builder()
							.idClienteBean(ClienteBean.builder().idCliente(rst.getInt("idCliente")).build())
							.detalle_ProductoBean(Detalle_ProductoBean.builder()
													.idProductoBean(ProductoBean.builder()
																		.idProducto(rst.getInt("idProducto"))
																		.descripcion(rst.getString("producto"))
																		.idCategoriaBean(CategoriaBean.builder()
																				.idCategoria(rst.getInt("idCategoria"))
																				.descripcion(rst.getString("categoria"))
																				.build())
																		.idMarcaBean(MarcaBean.builder()
																				.idMarca(rst.getInt("idMarca"))
																				.descripcion(rst.getString("marca"))
																				.build())
																		.precio(rst.getDouble("precio"))
																		.build())
													.idColorBean(ColorBean.builder()
																	.idColor(rst.getInt("idColor"))
																	.descripcion(rst.getString("color"))
																	.build())
													.idDescuentoBean(DescuentoBean.builder()
																		.idDescuento(rst.getInt("idDescuento"))
																		.porcentaje(rst.getDouble("porcentaje"))
																		.build())
													.cantidad(rst.getInt("cantidad"))
													.build())							
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_IDCLIENTE", idCliente);
		
		return (List<FavoritoBean>) jdbcCall.execute(parametros).get("rst");
	}

	@Override
	public Map<String, Object> deleteFavorito(int idCliente, int idProducto, int idColor)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM FAVORITO WHERE IDCLIENTE = ? AND IDPRODUCTO = ? AND IDCOLOR = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idCliente);
			pstm.setInt(2, idProducto);
			pstm.setInt(3, idColor);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró favorito con idProducto " + idProducto + " y idColor: " + idColor);
			
			conexion.commit();
		}
		catch(SQLException e)
		{
			Conexion.rollback(conexion);			
			respuesta = Conexion.getErrorSQL(e);
		}
		catch(Exception e)
		{
			Conexion.rollback(conexion);
			respuesta = Conexion.getError(e);
		}
		finally{Conexion.closeConexion(conexion, null, pstm);}
		
		return respuesta;
	}
}