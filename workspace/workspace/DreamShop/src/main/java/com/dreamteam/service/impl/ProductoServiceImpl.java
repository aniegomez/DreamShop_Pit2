package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.ProductoBean;
import com.dreamteam.dao.ProductoDAO;
import com.dreamteam.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService
{
    @Autowired private ProductoDAO productoDAO;
       
	//CRUD
    @Override public Map<String, Object> createProducto(ProductoBean productoBean) {return productoDAO.createProducto(productoBean);}
	@Override public List<ProductoBean> readProducto() {return  productoDAO.readProducto();}
	@Override public Map<String, Object> updateProducto(ProductoBean productoBean) {return productoDAO.updateProducto(productoBean);}
	@Override public Map<String, Object> deleteProducto(int idProducto) {return productoDAO.deleteProducto(idProducto);}
}