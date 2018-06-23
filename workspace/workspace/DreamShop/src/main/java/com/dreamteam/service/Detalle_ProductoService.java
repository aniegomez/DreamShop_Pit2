package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.Detalle_ProductoBean;

public interface Detalle_ProductoService
{
	//CRUD
	public Map<String, Object> createDetalleProducto(Detalle_ProductoBean detalle_ProductoBean);
	public List<Detalle_ProductoBean> readProductoTop();
	public Map<String, Object> updateDetalleProducto(Detalle_ProductoBean detalle_ProductoBean);
	public Map<String, Object> deleteDetalleProducto(int idProducto, int idColor);	
	//
	public List<Detalle_ProductoBean> readProductoPorCategoria(int idCategoria);
	public List<Detalle_ProductoBean> readProductoPorProducto(int idProducto);
	public byte[] getFotoProducto(int idProducto, int idColor);
	public Map<String, Object> guardarFoto(Detalle_ProductoBean detalle_ProductoBean);
}