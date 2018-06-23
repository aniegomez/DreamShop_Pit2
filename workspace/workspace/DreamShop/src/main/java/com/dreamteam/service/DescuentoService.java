package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.DescuentoBean;

public interface DescuentoService
{
	//CRUD
	public Map<String, Object> createDescuento(DescuentoBean descuentoBean);
	public List<DescuentoBean> readDescuento();
	public Map<String, Object> updateDescuento(DescuentoBean descuentoBean);
	public Map<String, Object> deleteDescuento(int idDescuento);
	public List<DescuentoBean> readDescuentoActivo();
	public Map<String, Object> asignarDescuentoProducto(int idDescuento, List<Integer> listaProducto);
	public Map<String, Object> asignarDescuentoDetalleProducto(int idDescuento, int idProducto, List<Integer> listaDetalleProducto);
	public Map<String, Object> desAsignarDescuentoProducto(int idProducto);
	public Map<String, Object> desAsignarDescuentoDetalleProducto(int idProducto, int idColor);
}