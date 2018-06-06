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

import com.dreamteam.bean.EmpleadoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.RolBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.UsuarioDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	Map<String, Object> parametros;
	
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	private Map<String, Object> respuesta;
	
	@Override
	public Map<String, Object> createUsuario(UsuarioBean usuarioBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "INSERT INTO USUARIO VALUES(NULL,?,?,?,?,1,CURRENT_DATE,?)";
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Usuario", 	 		1,  usuarioBean.getUsuario());
			pstm = Conexion.validarNulo(pstm, "Clave", 		 		2,  usuarioBean.getClave());
			pstm = Conexion.validarNulo(pstm, "ID Empleado", 		3,  Conexion.validarNulo_Bean(usuarioBean.getIdEmpleadoBean()) ? null : usuarioBean.getIdEmpleadoBean().getIdEmpleado());
			pstm = Conexion.validarNulo(pstm, "ID Rol", 			4,  Conexion.validarNulo_Bean(usuarioBean.getIdRolBean()) ? null : usuarioBean.getIdRolBean().getIdRol());
			pstm = Conexion.validarNulo(pstm, "ID Usuario Creador", 5,  Conexion.validarNulo_Bean(usuarioBean.getUsu_creaBean()) ? null : usuarioBean.getUsu_creaBean().getIdUsuario());
			
			pstm.executeUpdate();
			rst = pstm.getGeneratedKeys();		
			
			respuesta = Conexion.getRespuestaOK();
			while(rst.next())
				respuesta.put("idUsuario", rst.getInt(1));
			
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
	public List<UsuarioBean> readUsuario()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_USUARIO").returningResultSet("rst", new RowMapper<UsuarioBean>()
		{
			@Override
			public UsuarioBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return UsuarioBean.builder()
							.idUsuario(rst.getInt("idUsuario"))
							.usuario(rst.getString("usuario"))
							.idEmpleadoBean(EmpleadoBean.builder()
												.idEmpleado(rst.getInt("idEmpleado"))
												.nombre(rst.getString("nombre"))
												.apellido_p(rst.getString("apellido_p"))
												.apellido_m(rst.getString("apellido_m"))
												.build())
							.idRolBean(RolBean.builder()
											.idRol(rst.getInt("idRol"))
											.descripcion(rst.getString("rol"))
											.build())
							.idEstadoBean(EstadoBean.builder()
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.usu_creaBean(UsuarioBean.builder()
											.idUsuario(rst.getInt("idUsuario_padre"))
											.usuario(rst.getString("usuario_padre"))
											.build())
							.build();
			}
		});
		
		return (List<UsuarioBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateUsuario(UsuarioBean usuarioBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = 	"UPDATE USUARIO SET 	 " +
					"	usuario = ? 		," +
					"	clave = ? 			," +
					"	idEmpleado = ? 		," +
					"	idRol = ? 			," +
					"	usu_crea = ? 		 " +
					"WHERE IDUSUARIO = ? 	 ";
					
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Usuario", 	 		1,  usuarioBean.getUsuario());
			pstm = Conexion.validarNulo(pstm, "Clave", 		 		2,  usuarioBean.getClave());
			pstm = Conexion.validarNulo(pstm, "ID Empleado", 		3,  Conexion.validarNulo_Bean(usuarioBean.getIdEmpleadoBean()) ? null : usuarioBean.getIdEmpleadoBean().getIdEmpleado());
			pstm = Conexion.validarNulo(pstm, "ID Rol", 			4,  Conexion.validarNulo_Bean(usuarioBean.getIdRolBean()) ? null : usuarioBean.getIdRolBean().getIdRol());
			pstm = Conexion.validarNulo(pstm, "ID Usuario Creador",	5,  Conexion.validarNulo_Bean(usuarioBean.getUsu_creaBean()) ? null : usuarioBean.getUsu_creaBean().getIdUsuario());
			pstm = Conexion.validarNulo(pstm, "ID Usuario", 		6, usuarioBean.getIdUsuario());
						
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró Usuario con id " + usuarioBean.getIdUsuario());
			
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
	public Map<String, Object> deleteUsuario(int idUsuario)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM USUARIO WHERE IDUSUARIO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idUsuario);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró Usuario con id " + idUsuario);
			
			conexion.commit();
		}
		catch(SQLException e)
		{
			if( e.getErrorCode() == 1451 )
			{
				try
				{
					sql = "UPDATE USUARIO SET IDESTADO = 2 WHERE IDUSUARIO = ?";
					pstm = conexion.prepareStatement(sql);
					pstm.setInt(1, idUsuario);
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
	public UsuarioBean loginUsuario(UsuarioBean usuarioBean)
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_LOGIN_USUARIO").returningResultSet("rst", new RowMapper<UsuarioBean>()
		{
			@Override
			public UsuarioBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return UsuarioBean.builder()
							.idUsuario(rst.getInt("idUsuario"))
							.usuario(rst.getString("usuario"))
							.idEmpleadoBean(EmpleadoBean.builder()
												.idEmpleado(rst.getInt("idEmpleado"))
												.nombre(rst.getString("nombre"))
												.apellido_p(rst.getString("apellido_p"))
												.apellido_m(rst.getString("apellido_m"))
												.idTipoDocumentoBean(TipoDocumentoBean.builder()
																		.idTipoDocumento(rst.getInt("idTipoDocumento"))
																		.descripcion(rst.getString("tipoDocumento"))
																		.build())
												.nro_documento(rst.getString("nro_documento"))
												.correo(rst.getString("correo"))
												.foto_ruta(rst.getString("foto_ruta"))
												.idEstadoCivilBean(EstadoCivilBean.builder()
																		.idEstadoCivil(rst.getInt("idEstadoCivil"))
																		.descripcion(rst.getString("estadoCivil"))
																		.build())
												.idEstadoBean(EstadoBean.builder()
																.idEstado(rst.getInt("idEstado_emp"))
																.descripcion(rst.getString("estado_emp"))
																.build())
												.idUsuarioBean(UsuarioBean.builder()
																	.usuario(rst.getString("usuario"))
																	.build())
												.fe_crea(rst.getString("fe_crea_emp"))
												.build())
							.idRolBean(RolBean.builder()
											.idRol(rst.getInt("idRol"))
											.descripcion(rst.getString("rol"))
											.build())
							.idEstadoBean(EstadoBean.builder()
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.usu_creaBean(UsuarioBean.builder()
											.idUsuario(rst.getInt("idUsuario_padre"))
											.usuario(rst.getString("usuario_padre"))
											.build())
							.build();
			}
		});
		
		parametros = new HashMap<>();
		parametros.put("_USUARIO", usuarioBean.getUsuario());
		parametros.put("_CLAVE", usuarioBean.getClave());
		
		try{ return ( (List<UsuarioBean>) jdbcCall.execute(parametros).get("rst")).get(0);}
		catch(IndexOutOfBoundsException e){return UsuarioBean.builder().build();}
		catch(Exception e){e.printStackTrace(); return UsuarioBean.builder().build();}
	}
}