package com.dreamteam.dao.impl;

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

import com.dreamteam.bean.DistritoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.dao.DistritoDAO;

@Repository
public class DistritoDAOImpl implements DistritoDAO
{	
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	Map<String, Object> parametros;
	
	@SuppressWarnings("unchecked")
	public List<DistritoBean> readDistritoPorProvincia(int idProvincia)
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_DISTRITO_POR_PROVINCIA").returningResultSet("rst", new RowMapper<DistritoBean>()
		{
			@Override
			public DistritoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return DistritoBean.builder()
							.idDistrito(rst.getInt("idDistrito"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_idProvincia", idProvincia);
		
		return (List<DistritoBean>) jdbcCall.execute(parametros).get("rst");
	}
}
