package com.dreamteam.dao;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.ProductoBean;

public interface ProductoDAO
{
	//CRUD
	public Map<String, Object> createProducto(ProductoBean productoBean);
	public List<ProductoBean>  readProducto();
	public Map<String, Object> updateProducto(ProductoBean productoBean);
	public Map<String, Object> deleteProducto(int idProducto);
}