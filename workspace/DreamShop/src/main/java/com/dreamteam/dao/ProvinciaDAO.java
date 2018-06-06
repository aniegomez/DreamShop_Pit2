package com.dreamteam.dao;

import java.util.List;

import com.dreamteam.bean.ProvinciaBean;

public interface ProvinciaDAO
{
	public List<ProvinciaBean> readProvinciaPorDepartamento(int idDepartamento);
}
