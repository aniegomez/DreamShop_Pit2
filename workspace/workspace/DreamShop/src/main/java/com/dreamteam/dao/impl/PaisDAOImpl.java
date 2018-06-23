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
import com.dreamteam.bean.PaisBean;
import com.dreamteam.dao.PaisDAO;

@Repository
public class PaisDAOImpl implements PaisDAO
{	
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<PaisBean> readPais()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_PAIS").returningResultSet("rst", new RowMapper<PaisBean>()
		{
			@Override
			public PaisBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return PaisBean.builder()
							.idPais(rst.getInt("idPais"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		return (List<PaisBean>) jdbcCall.execute().get("rst");
	}
}
