package com.dreamteam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.ColorBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.ColorDAO;

@Repository
public class ColorDAOImpl implements ColorDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<ColorBean> readColor()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_COLOR").returningResultSet("rst", new RowMapper<ColorBean>()
		{
			@Override
			public ColorBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return ColorBean.builder()
							.idColor(rst.getInt("idColor"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.idUsuarioBean(UsuarioBean.builder().idUsuario(rst.getInt("idUsuario")).build())
							.build();
			}
		});
		
		return (List<ColorBean>) jdbcCall.execute().get("rst");
	}
}
