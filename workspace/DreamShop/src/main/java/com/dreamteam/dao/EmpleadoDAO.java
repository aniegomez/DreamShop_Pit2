package com.dreamteam.dao;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.EmpleadoBean;

public interface EmpleadoDAO
{
	public Map<String, Object> createEmpleado(EmpleadoBean empleadoBean);
	public List<EmpleadoBean>  readEmpleado();
	public Map<String, Object> updateEmpleado(EmpleadoBean empleadoBean);
	public Map<String, Object> deleteEmpleado(int idEmpleado);
}