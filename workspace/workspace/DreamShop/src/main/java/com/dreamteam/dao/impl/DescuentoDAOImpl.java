package com.dreamteam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.DescuentoDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class DescuentoDAOImpl implements DescuentoDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta = null;
	
	@Override
	public Map<String, Object> createDescuento(DescuentoBean descuentoBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);

			sql = "INSERT INTO DESCUENTO VALUES(NULL,?,?,?,?,CURRENT_DATE,1,?)";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Descripcion",  		1,  descuentoBean.getDescripcion() );
			pstm = Conexion.validarNulo(pstm, "Porcentaje",  		2,  descuentoBean.getPorcentaje() );
			pstm = Conexion.validarNulo(pstm, "Fecha de Inicio",  	3,  descuentoBean.getFe_inicio() );
			pstm = Conexion.validarNulo(pstm, "Fecha de Término",  	4,  descuentoBean.getFe_termino() );
			pstm = Conexion.validarNulo(pstm, "Id. Usuario",  		5,  Conexion.validarNulo_Bean(descuentoBean.getIdUsuarioBean() == null ) ? null : descuentoBean.getIdUsuarioBean().getIdUsuario() );			
			
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
	public List<DescuentoBean> readDescuento()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_DESCUENTO").returningResultSet("rst", new RowMapper<DescuentoBean>()
		{
			@Override
			public DescuentoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return DescuentoBean.builder()
							.idDescuento(rst.getInt("idDescuento"))
							.descripcion(rst.getString("descripcion"))
							.porcentaje(rst.getDouble("porcentaje"))
							.fe_inicio(rst.getString("fe_inicio"))
							.fe_termino(rst.getString("fe_termino"))
							.fe_crea(rst.getString("fe_crea"))
							.idEstadoBean(EstadoBean.builder()
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.idUsuarioBean(UsuarioBean.builder()
											.idUsuario(rst.getInt("idUsuario"))
											.usuario(rst.getString("usuario"))
											.build())
							.build();
			}
		});
		
		return (List<DescuentoBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateDescuento(DescuentoBean descuentoBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);

			sql = "SELECT COUNT(*) FROM DETALLE_PEDIDO WHERE IDDESCUENTO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Id. Descuento", 1, descuentoBean.getIdDescuento() );			
			rst = pstm.executeQuery();
			
			while(rst.next())
				if( rst.getInt(1) > 0 )
					throw new Exception("Descuento ya está siendo usado en un pedido");
			
			sql = "UPDATE DESCUENTO SET " +
					"DESCRIPCION = ?, " +
					"PORCENTAJE= ?, " +
					"FE_INICIO = ?, " +
					"FE_TERMINO = ?, " +
					"IDUSUARIO = ? " +
					"WHERE IDDESCUENTO = ?";
			
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Descripcion",  		1,  descuentoBean.getDescripcion() );
			pstm = Conexion.validarNulo(pstm, "Porcentaje",  		2,  descuentoBean.getPorcentaje() );
			pstm = Conexion.validarNulo(pstm, "Fecha de Inicio",  	3,  descuentoBean.getFe_inicio() );
			pstm = Conexion.validarNulo(pstm, "Fecha de Término",  	4,  descuentoBean.getFe_termino() );
			pstm = Conexion.validarNulo(pstm, "Id. Usuario",  		5,  Conexion.validarNulo_Bean(descuentoBean.getIdUsuarioBean() == null ) ? null : descuentoBean.getIdUsuarioBean().getIdUsuario() );
			pstm = Conexion.validarNulo(pstm, "Id. Descuento",  	6,  descuentoBean.getIdDescuento() );
			
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

	@Override
	public Map<String, Object> deleteDescuento(int idDescuento)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM DESCUENTO WHERE IDDESCUENTO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idDescuento);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró descuento con id " + idDescuento);
			
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

	@SuppressWarnings("unchecked")
	public List<DescuentoBean> readDescuentoActivo()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_DESCUENTO_ACTIVO").returningResultSet("rst", new RowMapper<DescuentoBean>()
		{
			@Override
			public DescuentoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return DescuentoBean.builder()
							.idDescuento(rst.getInt("idDescuento"))
							.descripcion(rst.getString("descripcion"))
							.porcentaje(rst.getDouble("porcentaje"))
							.fe_inicio(rst.getString("fe_inicio"))
							.fe_termino(rst.getString("fe_termino"))
							.fe_crea(rst.getString("fe_crea"))
							.idEstadoBean(EstadoBean.builder()
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.idUsuarioBean(UsuarioBean.builder()
											.idUsuario(rst.getInt("idUsuario"))
											.usuario(rst.getString("usuario"))
											.build())
							.build();
			}
		});
		
		return (List<DescuentoBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> asignarDescuentoProducto(int idDescuento, List<Integer> listaProducto)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);

			for(Integer idProducto:listaProducto)
			{
				sql = "UPDATE DETALLE_PRODUCTO SET IDDESCUENTO = ? WHERE IDPRODUCTO = ?";
				pstm = conexion.prepareStatement(sql);
				pstm = Conexion.validarNulo(pstm, "Id. Descuento", 1, idDescuento);
				pstm = Conexion.validarNulo(pstm, "Id. Producto",  2, idProducto);
				
				if( pstm.executeUpdate() < 0 )
					throw new Exception("No se pudo asignar descuento al Producto con id" + idProducto);
			}
			
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

	@Override
	public Map<String, Object> asignarDescuentoDetalleProducto(int idDescuento, int idProducto, List<Integer> listaDetalleProducto)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);

			for(Integer idColor:listaDetalleProducto)
			{
				sql = "UPDATE DETALLE_PRODUCTO SET IDDESCUENTO = ? WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
				pstm = conexion.prepareStatement(sql);
				pstm = Conexion.validarNulo(pstm, "Id. Descuento", 1, idDescuento);
				pstm = Conexion.validarNulo(pstm, "Id. Producto",  2, idProducto);
				pstm = Conexion.validarNulo(pstm, "Id. Producto",  3, idColor);
				
				if( pstm.executeUpdate() < 0 )
					throw new Exception("No se pudo asignar descuento al Producto con id" + idProducto + " y color " + idColor);
			}
			
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

	@Override
	public Map<String, Object> desAsignarDescuentoProducto(int idProducto)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);

			sql = "UPDATE DETALLE_PRODUCTO SET IDDESCUENTO = NULL WHERE IDPRODUCTO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Id. Producto", 1, idProducto);
				
			if (pstm.executeUpdate() < 0)
				throw new Exception("No se encontró producto con id" + idProducto);
			
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

	@Override
	public Map<String, Object> desAsignarDescuentoDetalleProducto(int idProducto, int idColor)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "UPDATE DETALLE_PRODUCTO SET IDDESCUENTO = NULL WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Id. Producto", 1, idProducto);
			pstm = Conexion.validarNulo(pstm, "Id. Color", 2, idColor);

			if (pstm.executeUpdate() < 0)
				throw new Exception(
						"No se pudo desasignar descuento al Producto con id" + idProducto + " y color " + idColor);
			
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

}