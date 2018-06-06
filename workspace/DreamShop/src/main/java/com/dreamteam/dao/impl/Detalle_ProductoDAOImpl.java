package com.dreamteam.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.CategoriaBean;
import com.dreamteam.bean.ColorBean;
import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.bean.Detalle_ProductoBean;
import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.ProductoBean;
import com.dreamteam.dao.Detalle_ProductoDAO;
import com.dreamteam.utilitarios.Conexion;
import com.dreamteam.utilitarios.FotosBean;
import com.dreamteam.utilitarios.Metodos;

@Repository
public class Detalle_ProductoDAOImpl implements Detalle_ProductoDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	@Autowired
	private FotosBean fotosBean;
	private File archivo;
	private FileOutputStream fos;
	private BufferedImage imagen;
	private ByteArrayOutputStream lectorBytes;
	private byte [] imagen_bytes;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta = null, parametros = null;
	
	@Override
	public Map<String, Object> createDetalleProducto(Detalle_ProductoBean detalle_ProductoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
		
			sql = "INSERT INTO DETALLE_PRODUCTO VALUES(?,?,?,NULL,NULL)";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Id. Producto", 	1,  Conexion.validarNulo_Bean(detalle_ProductoBean.getIdProductoBean() == null ) ? null : detalle_ProductoBean.getIdProductoBean().getIdProducto() );
			pstm = Conexion.validarNulo(pstm, "Id. Color", 	  	2,  Conexion.validarNulo_Bean(detalle_ProductoBean.getIdColorBean() == null ) ? null : detalle_ProductoBean.getIdColorBean().getIdColor() );
			pstm = Conexion.validarNulo(pstm, "Cantidad",  		3,  detalle_ProductoBean.getCantidad() );			
			
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
	public List<Detalle_ProductoBean> readProductoTop()
	{					
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PRODUCTO_TOP").returningResultSet("rst", new RowMapper<Detalle_ProductoBean>()
		{					
			@Override
			public Detalle_ProductoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				imagen_bytes = null;
				try
				{					
					archivo = new File( fotosBean.getRutaProducto() + rst.getInt("idProducto") + "_" + rst.getInt("idColor") + ".jpg" );
					imagen = ImageIO.read(archivo);
				    lectorBytes = new ByteArrayOutputStream();
				    ImageIO.write( imagen, "jpg", lectorBytes );
				    imagen_bytes = lectorBytes.toByteArray();
				    new Base64();
					Base64.encodeBase64String(imagen_bytes);
				}
				catch (Exception e){imagen_bytes = null;}
				
				return Detalle_ProductoBean.builder()
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
							.cantidad(rst.getInt("cantidad"))
							.foto_ruta( Base64.encodeBase64String(imagen_bytes) )
							.idDescuentoBean(DescuentoBean.builder()
												.idDescuento(rst.getInt("idDescuento"))
												.descripcion(rst.getString("descuento"))
												.porcentaje(rst.getDouble("porcentaje"))
												.build())
							.build();
			}
		});
						
		return (List<Detalle_ProductoBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateDetalleProducto(Detalle_ProductoBean detalle_ProductoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
		
			sql = 	"UPDATE DETALLE_PRODUCTO SET " + 
					"	CANTIDAD = ? " + 
					"WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Cantidad",  		1,  detalle_ProductoBean.getCantidad() );
			pstm = Conexion.validarNulo(pstm, "Id. Producto", 	2,  Conexion.validarNulo_Bean(detalle_ProductoBean.getIdProductoBean() == null ) ? null : detalle_ProductoBean.getIdProductoBean().getIdProducto() );
			pstm = Conexion.validarNulo(pstm, "Id. Color", 	  	3,  Conexion.validarNulo_Bean(detalle_ProductoBean.getIdColorBean() == null ) ? null : detalle_ProductoBean.getIdColorBean().getIdColor() );
									
			if(pstm.executeUpdate() > 0)
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró producto con idProducto " +
														detalle_ProductoBean.getIdProductoBean().getIdProducto() +
														" e idColor: " +
														detalle_ProductoBean.getIdColorBean().getIdColor());
			
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
	public Map<String, Object> deleteDetalleProducto(int idProducto, int idColor)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM DETALLE_PRODUCTO WHERE IDPRODUCTO = ? AND IDCOLOR = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idProducto);
			pstm.setInt(2, idColor);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró producto con idProducto " +
						idProducto + " e idColor: " + idColor );
			
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
	public List<Detalle_ProductoBean> readProductoPorCategoria(int idCategoria)
	{		
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_GET_DETA_PRODUCTO_POR_CATEGORIA").returningResultSet("rst", new RowMapper<Detalle_ProductoBean>()
		{					
			@Override
			public Detalle_ProductoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				imagen_bytes = null;
				try
				{					
					archivo = new File( fotosBean.getRutaProducto() + rst.getInt("idProducto") + "_" + rst.getInt("idColor") + ".jpg" );
					imagen = ImageIO.read(archivo);
				    lectorBytes = new ByteArrayOutputStream();
				    ImageIO.write( imagen, "jpg", lectorBytes );
				    imagen_bytes = lectorBytes.toByteArray();
				    Base64.encodeBase64String(imagen_bytes);
				}
				catch (Exception e){imagen_bytes = null;}
				
				return Detalle_ProductoBean.builder()
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
							.cantidad(rst.getInt("cantidad"))							
							.foto_ruta( Base64.encodeBase64String(imagen_bytes) )
							.idDescuentoBean(DescuentoBean.builder()
												.idDescuento(rst.getInt("idDescuento"))
												.descripcion(rst.getString("descuento"))
												.porcentaje(rst.getDouble("porcentaje"))
												.build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_IDCATEGORIA", idCategoria);
						
		return (List<Detalle_ProductoBean>) jdbcCall.execute(parametros).get("rst");
	}	

	@SuppressWarnings("unchecked")
	public List<Detalle_ProductoBean> readProductoPorProducto(int idProducto)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_GET_DETA_PRODUCTO_POR_PRODUCTO").returningResultSet("rst", new RowMapper<Detalle_ProductoBean>()
		{					
			@Override
			public Detalle_ProductoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				imagen_bytes = null;
				try
				{					
					archivo = new File( fotosBean.getRutaProducto() + rst.getInt("idProducto") + "_" + rst.getInt("idColor") + ".jpg" );
					imagen = ImageIO.read(archivo);
				    lectorBytes = new ByteArrayOutputStream();
				    ImageIO.write( imagen, "jpg", lectorBytes );
				    imagen_bytes = lectorBytes.toByteArray();
				    Base64.encodeBase64String(imagen_bytes);
				}
				catch (Exception e){imagen_bytes = null;}
				
				return Detalle_ProductoBean.builder()
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
							.cantidad(rst.getInt("cantidad"))
							.foto_ruta( Base64.encodeBase64String(imagen_bytes) )
							.idDescuentoBean(DescuentoBean.builder()
												.idDescuento(rst.getInt("idDescuento"))
												.descripcion(rst.getString("descuento"))
												.porcentaje(rst.getDouble("porcentaje"))
												.build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_IDPRODUCTO", idProducto);
						
		return (List<Detalle_ProductoBean>) jdbcCall.execute(parametros).get("rst");
	}

	public byte[] getFotoProducto(int idProducto, int idColor)
	{
		imagen_bytes = null;
		try
		{					
			archivo = new File( fotosBean.getRutaProducto() + idProducto + "_" + idColor + ".jpg" );
			imagen = ImageIO.read(archivo);
		    lectorBytes = new ByteArrayOutputStream();
		    ImageIO.write( imagen, "jpg", lectorBytes );
		    imagen_bytes = lectorBytes.toByteArray();
		}
		catch (Exception e){imagen_bytes = null;}
		
		return imagen_bytes;
	}

	public Map<String, Object> guardarFoto(Detalle_ProductoBean detalle_ProductoBean)
	{
		try
		{
			if( detalle_ProductoBean.getFoto_ruta().trim().isEmpty() )
				throw new NullPointerException("No ha ingresado una foto");
			if( !detalle_ProductoBean.getFoto_ruta().contains("jpeg") && !detalle_ProductoBean.getFoto_ruta().contains("jpg") )
				throw new NullPointerException("No ha ingresado una foto en formato jpg");				
			
			imagen_bytes = new Base64().decode(detalle_ProductoBean.getFoto_ruta().split(",")[1]);			
			archivo = new File
					(
							fotosBean.getRutaProducto() +
							detalle_ProductoBean.getIdProductoBean().getIdProducto() 	+ "_" +
									detalle_ProductoBean.getIdColorBean().getIdColor() 	+ ".jpg"
					);
			fos = new FileOutputStream(archivo);
			fos.write(imagen_bytes);
			fos.flush();

		    respuesta = Conexion.getRespuestaOK();
		}		
		catch(Exception e){respuesta = Conexion.getError(e);}
		finally {Metodos.cerrar(fos);}
		
		return respuesta;
	}

}