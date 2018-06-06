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
import com.dreamteam.bean.RolBean;
import com.dreamteam.dao.RolDAO;

@Repository
public class RolDAOImpl implements RolDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<RolBean> readRol()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_ROL").returningResultSet("rst", new RowMapper<RolBean>()
		{
			@Override
			public RolBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return RolBean.builder()
							.idRol(rst.getInt("idRol"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder()
											.idEstado(rst.getInt("idEstado"))
											.descripcion(rst.getString("estado"))
											.build())
							.build();
			}
		});
		
		return (List<RolBean>) jdbcCall.execute().get("rst");
	}
}
