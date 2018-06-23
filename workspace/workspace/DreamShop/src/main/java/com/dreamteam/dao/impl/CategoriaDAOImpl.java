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

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.CategoriaDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class CategoriaDAOImpl implements CategoriaDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta;
	
	@Override
	public Map<String, Object> createCategoria(CategoriaBean categoriaBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "INSERT INTO CATEGORIA VALUES (NULL,?,1,?);";
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Descripcion", 1,  categoriaBean.getDescripcion());
			pstm = Conexion.validarNulo(pstm, "ID Usuario",  2,  Conexion.validarNulo_Bean( categoriaBean.getIdUsuarioBean() ) ? null : categoriaBean.getIdUsuarioBean().getIdUsuario());		
			
			pstm.executeUpdate();
			rst = pstm.getGeneratedKeys();
			
			respuesta = Conexion.getRespuestaOK();
			while(rst.next())
				respuesta.put("idCategoria", rst.getInt(1));
			
			conexion.commit();
		}
		catch (SQLException e)
		{
			Conexion.rollback(conexion);
			respuesta = Conexion.getErrorSQL(e);
		}
		catch (Exception e)
		{
			//No olvidar de validar los beans (validarNulo_Bean) porque pueden venir nulos			
			Conexion.rollback(conexion);
			respuesta = Conexion.getError(e);
		}
		finally{Conexion.closeConexion(conexion, rst, pstm);}
			
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	public List<CategoriaBean> readCategoria()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_CATEGORIA").returningResultSet("rst", new RowMapper<CategoriaBean>()
		{
			@Override
			public CategoriaBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return CategoriaBean.builder()
							.idCategoria(rst.getInt("idCategoria"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.idUsuarioBean(UsuarioBean.builder().idUsuario(rst.getInt("idUsuario")).build())
							.build();
			}
		});
		
		return (List<CategoriaBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateCategoria(CategoriaBean categoriaBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = 	"UPDATE CATEGORIA SET " +
						"DESCRIPCION = ? " +
			    	"WHERE IDCATEGORIA = ?";
			
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Descripcion",  1,  categoriaBean.getDescripcion());
			pstm = Conexion.validarNulo(pstm, "ID Categoría", 2,  categoriaBean.getIdCategoria());
			
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
			//No olvidar de validar los beans (validarNulo_Bean) porque pueden venir nulos			
			Conexion.rollback(conexion);
			respuesta = Conexion.getError(e);
		}
		finally{Conexion.closeConexion(conexion, rst, pstm);}
			
		return respuesta;
	}

	@Override
	public Map<String, Object> deleteCategoria(int idCategoria)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM CATEGORIA WHERE IDCATEGORIA = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idCategoria);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró categoría con id " + idCategoria);
			
			conexion.commit();
		}
		catch(SQLException e)
		{
			if( e.getErrorCode() == 1451 )
			{
				try
				{
					sql = "UPDATE CATEGORIA SET IDESTADO = 2 WHERE IDCATEGORIA = ?";
					pstm = conexion.prepareStatement(sql);
					pstm.setInt(1, idCategoria);
					pstm.executeUpdate();
					
					respuesta = Conexion.getRespuestaOK();
					
					conexion.commit();
				}
				catch (SQLException e1)
				{
					Conexion.rollback(conexion);
					respuesta = Conexion.getErrorSQL(e1);
				}
			}
			else
			{
				Conexion.rollback(conexion);			
				respuesta = Conexion.getErrorSQL(e);
			}
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
	public List<CategoriaBean> readCategoriaActiva()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_CATEGORIA_ACTIVA").returningResultSet("rst", new RowMapper<CategoriaBean>()
		{
			@Override
			public CategoriaBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return CategoriaBean.builder()
							.idCategoria(rst.getInt("idCategoria"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.idUsuarioBean(UsuarioBean.builder().idUsuario(rst.getInt("idUsuario")).build())
							.build();
			}
		});
		
		return (List<CategoriaBean>) jdbcCall.execute().get("rst");
	}

}