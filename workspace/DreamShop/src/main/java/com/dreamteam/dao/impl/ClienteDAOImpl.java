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

import com.dreamteam.bean.ClienteBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.dao.ClienteDAO;
import com.dreamteam.utilitarios.ClaveAutogenerada;
import com.dreamteam.utilitarios.Conexion;
import com.dreamteam.utilitarios.EmailService;
import com.dreamteam.utilitarios.FotosBean;
import com.dreamteam.utilitarios.Metodos;

@Repository
public class ClienteDAOImpl implements ClienteDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;

	@Autowired
	EmailService emailService;
	@Autowired
	private FotosBean fotosBean;
	private File archivo;
	private FileOutputStream fos;
	private BufferedImage imagen;
	private ByteArrayOutputStream lectorBytes;
	private byte [] imagen_bytes;
	
	private String emailAutogenerado = null;
	private String claveAutogenerada = null;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta, parametros;
	
	@Override
	public Map<String, Object> createCliente(ClienteBean clienteBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "INSERT INTO CLIENTE VALUES(NULL, ?,?,?,?,?,?,?,?,?,1, CURRENT_DATE, ?)";
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Nombre", 			1,  clienteBean.getNombre());
			pstm = Conexion.validarNulo(pstm, "Apellido Paterno", 	2,  clienteBean.getApellido_p());
			pstm = Conexion.validarNulo(pstm, "Apellido Materno", 	3,  clienteBean.getApellido_m());
			pstm = Conexion.validarNulo(pstm, "ID Tipo Documento", 	4,  Conexion.validarNulo_Bean(clienteBean.getIdTipoDocumentoBean()) ? null : clienteBean.getIdTipoDocumentoBean().getIdTipoDocumento());
			pstm = Conexion.validarNulo(pstm, "N˙mero Documento", 	5,  clienteBean.getNro_documento());
			pstm = Conexion.validarNulo(pstm, "Correo", 			6,  clienteBean.getCorreo());
			pstm = Conexion.validarNulo(pstm, "Clave", 				7,  clienteBean.getClave());
			pstm = Conexion.admitirNulo(pstm, "Foto Ruta", 			8,  clienteBean.getFoto_ruta(), String.class);
			pstm = Conexion.admitirNulo(pstm, "ID Estado Civil", 	9,  Conexion.validarNulo_Bean(clienteBean.getIdEstadoCivilBean()) ? null : clienteBean.getIdEstadoCivilBean().getIdEstadoCivil(), Integer.class);
			pstm = Conexion.admitirNulo(pstm, "TelÈfono", 			10, clienteBean.getTelefono(), String.class);
			
			pstm.executeUpdate();
			rst = pstm.getGeneratedKeys();
			
			new Thread( () ->
			{
				try
				{
					if( pstm.getUpdateCount() >= 1 )
					{							
						emailAutogenerado = String.format("DreamShop:\nLe damos la bienvenida a nuestra tienda virtual.\n%s",
								"Recuerde que usted es importante para nosotros cualquier duda contactarse con nuestor centro de atenci√≥n al cliente");
						emailService.enviarMensaje(clienteBean.getCorreo(), "DreamShop", emailAutogenerado);
					}
				}
				catch (Exception e) {}
			}).start();
			
			respuesta = Conexion.getRespuestaOK();
			while(rst.next())
				respuesta.put("idCliente", rst.getInt(1));
			
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
	@Override
	public List<ClienteBean> readCliente()
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_CLIENTE").returningResultSet("rst", new RowMapper<ClienteBean>()
		{
			@Override
			public ClienteBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ClienteBean.builder()
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
							.build();
			}
		});
		
		return (List<ClienteBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateCliente(ClienteBean clienteBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = 	"UPDATE CLIENTE SET "+
					"	nombre = ? 			,"+
					"	apellido_p = ? 		,"+
					"	apellido_m = ? 		,"+
					"	idTipoDocumento = ? ,"+
					"	nro_documento = ? 	,"+
					"	correo = ? 			,"+
					"	clave = ? 			,"+
					"	foto_ruta = ? 		,"+
					"	idEstadoCivil = ? 	,"+
					" 	telefono = ? 		 "+
					"WHERE idCliente = ? 	 ";
					
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Nombre", 			1,  clienteBean.getNombre());
			pstm = Conexion.validarNulo(pstm, "Apellido Paterno", 	2,  clienteBean.getApellido_p());
			pstm = Conexion.validarNulo(pstm, "Apellido Materno", 	3,  clienteBean.getApellido_m());
			pstm = Conexion.validarNulo(pstm, "ID Tipo Documento", 	4,  Conexion.validarNulo_Bean(clienteBean.getIdTipoDocumentoBean()) ? null : clienteBean.getIdTipoDocumentoBean().getIdTipoDocumento());
			pstm = Conexion.validarNulo(pstm, "N√∫mero Documento", 	5,  clienteBean.getNro_documento());
			pstm = Conexion.validarNulo(pstm, "Correo", 			6,  clienteBean.getCorreo());
			pstm = Conexion.validarNulo(pstm, "Clave", 				7,  clienteBean.getClave());
			pstm = Conexion.admitirNulo(pstm, "Foto Ruta", 			8,  clienteBean.getFoto_ruta(), String.class);
			pstm = Conexion.admitirNulo(pstm, "ID Estado Civil", 	9,  Conexion.validarNulo_Bean(clienteBean.getIdEstadoCivilBean()) ? null : clienteBean.getIdEstadoCivilBean().getIdEstadoCivil(), Integer.class);
			pstm = Conexion.admitirNulo(pstm, "TelÈfono", 			10, clienteBean.getTelefono(), String.class);
			pstm = Conexion.validarNulo(pstm, "IDCLIENTE", 			11, clienteBean.getIdCliente());
						
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontrÛ cliente con id " + clienteBean.getIdCliente());
			
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
		finally{Conexion.closeConexion(conexion, null, pstm);}
		
		return respuesta;
	}

	@Override
	public Map<String, Object> deleteCliente(int idCliente)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM CLIENTE WHERE IDCLIENTE = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idCliente);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontr√≥ cliente con id " + idCliente);
			
			conexion.commit();
		}
		catch(SQLException e)
		{
			if( e.getErrorCode() == 1451 )
			{
				try
				{
					sql = "UPDATE CLIENTE SET IDESTADO = 2 WHERE IDCLIENTE = ?";
					pstm = conexion.prepareStatement(sql);
					pstm.setInt(1, idCliente);
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

	@SuppressWarnings({ "unchecked" })
	@Override
	public ClienteBean getCliente(int idCliente)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_GET_CLIENTE").returningResultSet("rst", new RowMapper<ClienteBean>()
		{
			@Override
			public ClienteBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ClienteBean.builder()
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
							.build();
			}
		});
		
		parametros = new HashMap<>();
		parametros.put("_IDCLIENTE", idCliente);
		
		try {return ((List<ClienteBean>) jdbcCall.execute(parametros).get("rst")).get(0);}
		catch (Exception e) {return ClienteBean.builder().build();}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public ClienteBean loginCliente(ClienteBean clienteBean)
	{
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_LOGIN_CLIENTE").returningResultSet("rst", new RowMapper<ClienteBean>()
		{
			@Override
			public ClienteBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ClienteBean.builder()
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
							.build();
			}
		});
		
		parametros = new HashMap<>();
		parametros.put("_CORREO", clienteBean.getCorreo());
		parametros.put("_CLAVE", clienteBean.getClave());
		
		try {return ((List<ClienteBean>) jdbcCall.execute(parametros).get("rst")).get(0);}
		catch(IndexOutOfBoundsException e) {return ClienteBean.builder().build();}
		catch (Exception e) {e.printStackTrace(); return ClienteBean.builder().build();}
	}
	
	public Map<String, Object> cambiarClave(ClienteBean clienteBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = 	"UPDATE CLIENTE SET clave = ? WHERE idCliente = ? ";
					
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Clave", 	1,  clienteBean.getClave());
			pstm = Conexion.validarNulo(pstm, "IdCliente", 2,  clienteBean.getIdCliente());		
						
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontr√≥ cuenta con id " + clienteBean.getIdCliente());
			
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
		finally{Conexion.closeConexion(conexion, null, pstm);}
		
		return respuesta;
	}

	public Map<String, Object> 	recuperarClave(ClienteBean clienteBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			claveAutogenerada = new ClaveAutogenerada().getClave();
			
			sql = 	"UPDATE CLIENTE SET clave = ? WHERE correo = ? ";
					
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Clave", 	1,  claveAutogenerada);
			pstm = Conexion.validarNulo(pstm, "Correo", 2,  clienteBean.getCorreo());		
						
			if( pstm.executeUpdate() >= 1 )
			{
				respuesta = Conexion.getRespuestaOK();
				emailAutogenerado = String.format("DreamShop:\n%s%s\n\nLo esperamos en nuestra tienda virtual.", "Su nueva contrase√±a es ", claveAutogenerada);
				emailService.enviarMensaje(clienteBean.getCorreo(), "DreamShop", emailAutogenerado);
			}
			else
				respuesta = Conexion.getRespuestaError("No se encontr√≥ correo " + clienteBean.getCorreo());
			
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
		finally{Conexion.closeConexion(conexion, null, pstm);}
		
		return respuesta;
	}

	public Map<String, Object> 	guardarFoto(ClienteBean clienteBean)
	{
		try
		{
			if( clienteBean.getFoto_ruta().trim().isEmpty() )
				throw new NullPointerException("No ha ingresado una foto");
			if( !clienteBean.getFoto_ruta().contains("jpeg") && !clienteBean.getFoto_ruta().contains("jpg") )
				throw new NullPointerException("No ha ingresado una foto en formato jpg");				
			
			imagen_bytes = new Base64().decode(clienteBean.getFoto_ruta().split(",")[1]);
			archivo = new File(fotosBean.getRutaCliente() + clienteBean.getIdCliente() + ".jpg");
			fos = new FileOutputStream(archivo);
			fos.write(imagen_bytes);
			fos.flush();

		    respuesta = Conexion.getRespuestaOK();
		}		
		catch(Exception e){respuesta = Conexion.getError(e);}
		finally {Metodos.cerrar(fos);}
		
		return respuesta;
	}

	public byte[] getFotoCliente(int idCliente)
	{
		imagen_bytes = null;
		try	
		{					
			archivo = new File( fotosBean.getRutaCliente() + idCliente  + ".jpg" );
			imagen = ImageIO.read(archivo);
		    lectorBytes = new ByteArrayOutputStream();
		    ImageIO.write( imagen, "jpg", lectorBytes );
		    imagen_bytes = lectorBytes.toByteArray();
		}
		catch (Exception e){imagen_bytes = null;}
		
		return imagen_bytes;
	}

}