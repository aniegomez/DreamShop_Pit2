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
import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.ProductoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.ProductoDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class ProductoDAOImpl implements ProductoDAO
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
	public Map<String, Object> createProducto(ProductoBean productoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
		
			sql = "INSERT INTO PRODUCTO VALUES(NULL,?,?,?,?,1,?)";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Descripcion",  	1,  productoBean.getDescripcion() );
			pstm = Conexion.validarNulo(pstm, "Precio",  		2,  productoBean.getPrecio() );
			pstm = Conexion.validarNulo(pstm, "Id. Categoría", 	3,  Conexion.validarNulo_Bean(productoBean.getIdCategoriaBean() == null ) ? null : productoBean.getIdCategoriaBean().getIdCategoria() );
			pstm = Conexion.validarNulo(pstm, "Id. Marca", 	  	4,  Conexion.validarNulo_Bean(productoBean.getIdMarcaBean() == null ) ? null : productoBean.getIdMarcaBean().getIdMarca() );
			pstm = Conexion.validarNulo(pstm, "Id. Usuario",  	5,  Conexion.validarNulo_Bean(productoBean.getIdUsuarioBean() == null ) ? null : productoBean.getIdUsuarioBean().getIdUsuario() );			
			
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
	public List<ProductoBean> readProducto()
	{					
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PRODUCTO").returningResultSet("rst", new RowMapper<ProductoBean>()
		{
			@Override
			public ProductoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ProductoBean.builder()
							.idProducto(rst.getInt("idProducto"))
							.descripcion(rst.getString("producto"))
							.precio(rst.getDouble("precio"))
							.idCategoriaBean(CategoriaBean.builder()
												.idCategoria(rst.getInt("idCategoria"))
												.descripcion(rst.getString("categoria"))
												.build())
							.idMarcaBean(MarcaBean.builder()
											.idMarca(rst.getInt("idMarca"))
											.descripcion(rst.getString("marca"))
											.build())
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
						
		return (List<ProductoBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateProducto(ProductoBean productoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
		
			sql = "UPDATE PRODUCTO SET " +
			      " DESCRIPCION = ?, PRECIO = ?, IDCATEGORIA = ?, " + 
			      " IDMARCA = ?, IDUSUARIO = ? " +
			      " WHERE IDPRODUCTO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Descripcion",  	1,  productoBean.getDescripcion() );
			pstm = Conexion.validarNulo(pstm, "Precio",  		2,  productoBean.getPrecio() );
			pstm = Conexion.validarNulo(pstm, "Id. Categoría", 	3,  Conexion.validarNulo_Bean(productoBean.getIdCategoriaBean() == null ) ? null : productoBean.getIdCategoriaBean().getIdCategoria() );
			pstm = Conexion.validarNulo(pstm, "Id. Marca", 	  	4,  Conexion.validarNulo_Bean(productoBean.getIdMarcaBean() == null ) ? null : productoBean.getIdMarcaBean().getIdMarca() );
			pstm = Conexion.validarNulo(pstm, "Id. Usuario",  	5,  Conexion.validarNulo_Bean(productoBean.getIdUsuarioBean() == null ) ? null : productoBean.getIdUsuarioBean().getIdUsuario() );
			pstm = Conexion.validarNulo(pstm, "Id. Producto",  	6,  productoBean.getIdProducto() );
			
			if( pstm.executeUpdate() > 0)
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró producto con id " + productoBean.getIdProducto());								
			
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
	public Map<String, Object> deleteProducto(int idProducto)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM PRODUCTO WHERE IDPRODUCTO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idProducto);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró producto con id " + idProducto);
			
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