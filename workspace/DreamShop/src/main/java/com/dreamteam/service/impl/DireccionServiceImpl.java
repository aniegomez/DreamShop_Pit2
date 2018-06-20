package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.DireccionBean;
import com.dreamteam.dao.DireccionDAO;
import com.dreamteam.service.DireccionService;

@Service
public class DireccionServiceImpl implements DireccionService
{
	@Autowired
	DireccionDAO direccionDAO;
	
	public Map<String, Object> createDireccion(DireccionBean direccionBean)
	{return direccionDAO.createDireccion(direccionBean);}
	
	public List<DireccionBean> readDireccionPorCliente(int idCliente)
	{return direccionDAO.readDireccionPorCliente(idCliente);}
}