package com.dreamteam.dao;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.DireccionBean;

public interface DireccionDAO
{
	public Map<String, Object> createDireccion(DireccionBean direccionBean);
	public List<DireccionBean> readDireccionPorCliente(int idCliente);
}
