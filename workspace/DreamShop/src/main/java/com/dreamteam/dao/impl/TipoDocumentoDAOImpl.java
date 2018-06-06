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
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.dao.TipoDocumentoDAO;

@Repository
public class TipoDocumentoDAOImpl implements TipoDocumentoDAO
{
	@Autowired
	DataSource dataSource;
	SimpleJdbcCall jdbcCall;
	
	@SuppressWarnings("unchecked")
	public List<TipoDocumentoBean> readTipoDocumento()
	{				
		jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_READ_TIPODOCUMENTO").returningResultSet("rst", new RowMapper<TipoDocumentoBean>()
		{
			@Override
			public TipoDocumentoBean mapRow(ResultSet rst, int rowNum) throws SQLException
			{
				return TipoDocumentoBean.builder()
							.idTipoDocumento(rst.getInt("idTipoDocumento"))
							.descripcion(rst.getString("descripcion"))
							.idEstadoBean(EstadoBean.builder().idEstado(rst.getInt("idEstado")).build())
							.build();
			}
		});
		
		return (List<TipoDocumentoBean>) jdbcCall.execute().get("rst");
	}
}
