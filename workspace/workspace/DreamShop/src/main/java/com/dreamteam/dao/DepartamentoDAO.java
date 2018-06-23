package com.dreamteam.dao;

import java.util.List;

import com.dreamteam.bean.DepartamentoBean;

public interface DepartamentoDAO
{
	public List<DepartamentoBean> readDepartamentoPorPais(int idPais);
}