package com.dreamteam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.dao.EstadoCivilDAO;

@Repository
public class EstadoCivilDAOImpl implements EstadoCivilDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<EstadoCivilBean> readEstadoCivil()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_ESTADOCIVIL").returningResultSet("rst", new RowMapper<EstadoCivilBean>()
		{
			@Override
			public EstadoCivilBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return EstadoCivilBean.builder()
							.idEstadoCivil(rst.getInt("idEstadoCivil"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		return (List<EstadoCivilBean>) jdbcCall.execute().get("rst");
	}
}
