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

import com.dreamteam.bean.DireccionBean;
import com.dreamteam.bean.DistritoBean;
import com.dreamteam.dao.DireccionDAO;
import com.dreamteam.utilitarios.Conexion;

@Repository
public class DireccionDAOImpl implements DireccionDAO
{	
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	private Map<String, Object> respuesta, parametros;
	private Connection conexion = null;
	private PreparedStatement pstm = null;
	private ResultSet rst = null;
	private String sql = null;
	
	public Map<String, Object> createDireccion(DireccionBean direccionBean)
	{
		try
		{
			conexion = new Conexion().getConexion();
			conexion.setAutoCommit(false);
			
			sql = "INSERT INTO DIRECCION VALUES(NULL,?,?,1,?,?)";
			pstm = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);			
			pstm = Conexion.validarNulo(pstm, "ID. Cliente",  1, Conexion.validarNulo_Bean(direccionBean.getIdClienteBean()) ? null : direccionBean.getIdClienteBean().getIdCliente());
			pstm = Conexion.validarNulo(pstm, "ID. Distrito", 2, Conexion.validarNulo_Bean(direccionBean.getIdDistritoBean()) ? null : direccionBean.getIdDistritoBean().getIdDistrito());
			pstm = Conexion.validarNulo(pstm, "Dirección", 	  3, direccionBean.getDescripcion());
			pstm = Conexion.admitirNulo(pstm, "Referencia",   4, direccionBean.getReferencia(), String.class);
						
			pstm.executeUpdate();
			rst = pstm.getGeneratedKeys();
			
			respuesta = Conexion.getRespuestaOK();
			while(rst.next())
				respuesta.put("idDireccion", rst.getInt(1));
			
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
	public List<DireccionBean> readDireccionPorCliente(int idCliente)
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_LISTAR_DIRECCION_X_CLIENTE").returningResultSet("rst", new RowMapper<DireccionBean>()
		{
			@Override
			public DireccionBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return DireccionBean.builder()
							.idDireccion(rst.getInt("idDireccion"))
							.descripcion(rst.getString("direccion"))
							.referencia(rst.getString("referencia"))
							.idDistritoBean(DistritoBean.builder()
											.idDistrito(rst.getInt("idDistrito"))
											.descripcion(rst.getString("distrito"))
											.build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_idCliente", idCliente);
		
		return (List<DireccionBean>) jdbcCall.execute(parametros).get("rst");
	}
}
