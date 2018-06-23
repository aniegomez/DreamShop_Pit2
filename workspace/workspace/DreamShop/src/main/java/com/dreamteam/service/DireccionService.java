package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.DireccionBean;

public interface DireccionService
{
	public Map<String, Object> createDireccion(DireccionBean direccionBean);
	public List<DireccionBean> readDireccionPorCliente(int idCliente);
}