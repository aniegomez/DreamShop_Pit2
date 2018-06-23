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

import com.dreamteam.bean.DepartamentoBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.dao.DepartamentoDAO;

@Repository
public class DepartamentoDAOImpl implements DepartamentoDAO
{	
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	Map<String, Object> parametros;
	
	@SuppressWarnings("unchecked")
	public List<DepartamentoBean> readDepartamentoPorPais(int idPais)
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_DEPARTAMENTO_POR_PAIS").returningResultSet("rst", new RowMapper<DepartamentoBean>()
		{
			@Override
			public DepartamentoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return DepartamentoBean.builder()
							.idDepartamento(rst.getInt("idDepartamento"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		parametros = new HashMap<String, Object>();
		parametros.put("_idPais", idPais);
		
		return (List<DepartamentoBean>) jdbcCall.execute(parametros).get("rst");
	}
}
