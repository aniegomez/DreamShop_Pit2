package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.Detalle_ProductoBean;
import com.dreamteam.dao.Detalle_ProductoDAO;
import com.dreamteam.service.Detalle_ProductoService;

@Service
public class Detalle_ProductoServiceImpl implements Detalle_ProductoService
{
    @Autowired
    private Detalle_ProductoDAO detalle_ProductoDAO;

	//CRUD
    @Override public Map<String, Object> createDetalleProducto(Detalle_ProductoBean detalle_ProductoBean)
    {return detalle_ProductoDAO.createDetalleProducto(detalle_ProductoBean);}
    
    @Override public List<Detalle_ProductoBean> readProductoTop()
    {return  detalle_ProductoDAO.readProductoTop();}
    
    @Override public Map<String, Object> updateDetalleProducto(Detalle_ProductoBean detalle_ProductoBean)
    {return detalle_ProductoDAO.updateDetalleProducto(detalle_ProductoBean);}
    
    @Override public Map<String, Object> deleteDetalleProducto(int idProducto, int idColor)
    {return detalle_ProductoDAO.deleteDetalleProducto(idProducto, idColor);}
    
    //
    @Override public List<Detalle_ProductoBean> readProductoPorCategoria(int idCategoria)
    {return detalle_ProductoDAO.readProductoPorCategoria(idCategoria);}
    
    @Override public List<Detalle_ProductoBean> readProductoPorProducto(int idProducto)
    {return detalle_ProductoDAO.readProductoPorProducto(idProducto);}
    
    @Override public byte[] getFotoProducto(int idProducto, int idColor)
    {return detalle_ProductoDAO.getFotoProducto(idProducto, idColor);}
    
    public Map<String, Object> guardarFoto(Detalle_ProductoBean detalle_ProductoBean) {return detalle_ProductoDAO.guardarFoto(detalle_ProductoBean);}
}
