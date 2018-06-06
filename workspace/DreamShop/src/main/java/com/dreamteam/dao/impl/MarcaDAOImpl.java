package com.dreamteam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.dreamteam.bean.MarcaBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.MarcaDAO;

@Repository
public class MarcaDAOImpl implements MarcaDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<MarcaBean> readMarca()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_MARCA").returningResultSet("rst", new RowMapper<MarcaBean>()
		{
			@Override
			public MarcaBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return MarcaBean.builder()
							.idMarca(rst.getInt("idMarca"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.idUsuarioBean(UsuarioBean.builder().idUsuario(rst.getInt("idUsuario")).build())
							.build();
			}
		});
		
		return (List<MarcaBean>) jdbcCall.execute().get("rst");
	}
}
