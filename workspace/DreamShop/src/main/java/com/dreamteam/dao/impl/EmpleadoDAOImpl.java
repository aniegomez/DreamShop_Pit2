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

import com.dreamteam.bean.EmpleadoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.RolBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.EmpleadoDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class EmpleadoDAOImpl implements EmpleadoDAO
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
	public Map<String, Object> createEmpleado(EmpleadoBean empleadoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "INSERT INTO Empleado VALUES(NULL,?,?,?,?,?,?,?,?,1,?,CURRENT_DATE)";
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm = Conexion.validarNulo(pstm, "Nombre", 			1,  empleadoBean.getNombre());
			pstm = Conexion.validarNulo(pstm, "Apellido Paterno", 	2,  empleadoBean.getApellido_p());
			pstm = Conexion.validarNulo(pstm, "Apellido Materno", 	3,  empleadoBean.getApellido_m());
			pstm = Conexion.validarNulo(pstm, "ID Tipo Documento", 	4,  Conexion.validarNulo_Bean(empleadoBean.getIdTipoDocumentoBean()) ? null : empleadoBean.getIdTipoDocumentoBean().getIdTipoDocumento());
			pstm = Conexion.validarNulo(pstm, "Número Documento", 	5,  empleadoBean.getNro_documento());
			pstm = Conexion.validarNulo(pstm, "Correo", 			6,  empleadoBean.getCorreo());
			pstm = Conexion.admitirNulo(pstm, "Foto Ruta", 			7,  empleadoBean.getFoto_ruta(), String.class);
			pstm = Conexion.validarNulo(pstm, "ID Usuario", 		8,  Conexion.validarNulo_Bean(empleadoBean.getIdUsuarioBean()) ? null : empleadoBean.getIdUsuarioBean().getIdUsuario());
			pstm = Conexion.admitirNulo(pstm, "ID Estado Civil", 	9,  Conexion.validarNulo_Bean(empleadoBean.getIdEstadoCivilBean()) ? null : empleadoBean.getIdEstadoCivilBean().getIdEstadoCivil(), Integer.class);
			
			pstm.executeUpdate();
			rst = pstm.getGeneratedKeys();		
			
			respuesta = Conexion.getRespuestaOK();
			while(rst.next())
				respuesta.put("idEmpleado", rst.getInt(1));
			
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
	public List<EmpleadoBean>  readEmpleado()
	{	
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_EMPLEADO").returningResultSet("rst", new RowMapper<EmpleadoBean>()
		{
			@Override
			public EmpleadoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return EmpleadoBean.builder()
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
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.idUsuarioBean(UsuarioBean.builder()
												.idUsuario(rst.getInt("idUsuario"))
												.usuario(rst.getString("usuario"))
												.idRolBean(RolBean.builder()
																.idRol(rst.getInt("idRol"))
																.descripcion(rst.getString("rol"))
																.build())
												.build())
							.fe_crea(rst.getString("fe_crea"))
							.build();
			}
		});
		
		return (List<EmpleadoBean>) jdbcCall.execute().get("rst");
	}

	@Override
	public Map<String, Object> updateEmpleado(EmpleadoBean empleadoBean)
	{			
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = 	"UPDATE EMPLEADO SET "+
					"	nombre = ? 			,"+
					"	apellido_p = ? 		,"+
					"	apellido_m = ? 		,"+
					"	idTipoDocumento = ? ,"+
					"	nro_documento = ? 	,"+
					"	correo = ? 			,"+
					"	idEstadoCivil = ? 	,"+
					"	idEstado = ? 	 	,"+
					"	usu_crea = ? 	 	 "+
					"WHERE IDEMPLEADO = ? 	 ";
					
			pstm = conexion.prepareStatement(sql);
			pstm = Conexion.validarNulo(pstm, "Nombre", 			1,  empleadoBean.getNombre());
			pstm = Conexion.validarNulo(pstm, "Apellido Paterno", 	2,  empleadoBean.getApellido_p());
			pstm = Conexion.validarNulo(pstm, "Apellido Materno", 	3,  empleadoBean.getApellido_m());
			pstm = Conexion.validarNulo(pstm, "ID Tipo Documento", 	4,  Conexion.validarNulo_Bean(empleadoBean.getIdTipoDocumentoBean()) ? null : empleadoBean.getIdTipoDocumentoBean().getIdTipoDocumento());
			pstm = Conexion.validarNulo(pstm, "Número Documento", 	5,  empleadoBean.getNro_documento());
			pstm = Conexion.validarNulo(pstm, "Correo", 			6,  empleadoBean.getCorreo());
			pstm = Conexion.admitirNulo(pstm, "ID Estado Civil", 	7,  Conexion.validarNulo_Bean(empleadoBean.getIdEstadoCivilBean()) ? null : empleadoBean.getIdEstadoCivilBean().getIdEstadoCivil(), Integer.class);
			pstm = Conexion.validarNulo(pstm, "ID Estado", 			8,  Conexion.validarNulo_Bean(empleadoBean.getIdEstadoBean()) ? null : empleadoBean.getIdEstadoBean().getIdEstado());
			pstm = Conexion.validarNulo(pstm, "ID Usuario", 		9,  Conexion.validarNulo_Bean(empleadoBean.getIdUsuarioBean()) ? null : empleadoBean.getIdUsuarioBean().getIdUsuario());
			pstm = Conexion.validarNulo(pstm, "ID Empleado", 		10, empleadoBean.getIdEmpleado());
						
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontró Empleado con id " + empleadoBean.getIdEmpleado());
			
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
	public Map<String, Object> deleteEmpleado(int idEmpleado)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "DELETE FROM EMPLEADO WHERE IDEMPLEADO = ?";
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idEmpleado);
			
			if( pstm.executeUpdate() >= 1 )
				respuesta = Conexion.getRespuestaOK();
			else
				respuesta = Conexion.getRespuestaError("No se encontrÃ³ Empleado con id " + idEmpleado);
			
			conexion.commit();
		}
		catch(SQLException e)
		{
			if( e.getErrorCode() == 1451 )
			{
				try
				{
					sql = "UPDATE EMPLEADO SET IDESTADO = 2 WHERE IDEMPLEADO = ?";
					pstm = conexion.prepareStatement(sql);
					pstm.setInt(1, idEmpleado);
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
}