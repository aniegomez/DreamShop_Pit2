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

import com.dreamteam.bean.ProvinciaBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.dao.ProvinciaDAO;

@Repository
public class ProvinciaDAOImpl implements ProvinciaDAO
{	
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	Map<String, Object> parametros;
	
	@SuppressWarnings("unchecked")
	public List<ProvinciaBean> readProvinciaPorDepartamento(int idDepartamento)
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PROVINCIA_POR_DEPARTAMENTO").returningResultSet("rst", new RowMapper<ProvinciaBean>()
		{
			@Override
			public ProvinciaBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ProvinciaBean.builder()
							.idProvincia(rst.getInt("idProvincia"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_idDepartamento", idDepartamento);
		
		return (List<ProvinciaBean>) jdbcCall.execute(parametros).get("rst");
	}
}
