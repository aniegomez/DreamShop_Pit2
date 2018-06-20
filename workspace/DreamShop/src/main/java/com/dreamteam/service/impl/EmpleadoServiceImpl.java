package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.EmpleadoBean;
import com.dreamteam.dao.EmpleadoDAO;
import com.dreamteam.service.EmpleadoService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService
{
    @Autowired
    private EmpleadoDAO empleadoDAO;

	@Override
	public Map<String, Object> createEmpleado(EmpleadoBean empleadoBean)
	{return empleadoDAO.createEmpleado(empleadoBean);}
	
    @Override public List<EmpleadoBean> readEmpleado()
    {return empleadoDAO.readEmpleado();}

	@Override public Map<String, Object> updateEmpleado(EmpleadoBean empleadoBean)
	{return empleadoDAO.updateEmpleado(empleadoBean);}

	@Override public Map<String, Object> deleteEmpleado(int idEmpleado)
	{return empleadoDAO.deleteEmpleado(idEmpleado);}
}