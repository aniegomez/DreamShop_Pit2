package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.dao.DescuentoDAO;
import com.dreamteam.service.DescuentoService;

@Service
public class DescuentoServiceImpl implements DescuentoService
{
    @Autowired
    private DescuentoDAO descuentoDAO;

	//CRUD
    @Override public Map<String, Object> createDescuento(DescuentoBean descuentoBean)
	{return  descuentoDAO.createDescuento(descuentoBean);}
	
    @Override public List<DescuentoBean> readDescuento()
	{return  descuentoDAO.readDescuento();}
	
    @Override public Map<String, Object> updateDescuento(DescuentoBean descuentoBean)
	{return  descuentoDAO.updateDescuento(descuentoBean);}
	
    @Override public Map<String, Object> deleteDescuento(int idDescuento)
	{return  descuentoDAO.deleteDescuento(idDescuento);}
    
    @Override public List<DescuentoBean> readDescuentoActivo()
    {return  descuentoDAO.readDescuentoActivo();}
    
    @Override public Map<String, Object> asignarDescuentoProducto(int idDescuento, List<Integer> listaProducto)
    {return  descuentoDAO.asignarDescuentoProducto(idDescuento, listaProducto);}
    
    @Override public Map<String, Object> asignarDescuentoDetalleProducto(int idDescuento, int idProducto, List<Integer> listaDetalleProducto)
    {return  descuentoDAO.asignarDescuentoDetalleProducto(idDescuento, idProducto, listaDetalleProducto);}
    
    @Override public Map<String, Object> desAsignarDescuentoProducto(int idProducto)
    {return  descuentoDAO.desAsignarDescuentoProducto(idProducto);}
    
    @Override public Map<String, Object> desAsignarDescuentoDetalleProducto(int idProducto, int idColor)
    {return  descuentoDAO.desAsignarDescuentoDetalleProducto(idProducto, idColor);}
}