package com.dreamteam.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.bean.Detalle_ProductoBean;
import com.dreamteam.service.Detalle_ProductoService;

@RestController
@RequestMapping("/detalleproducto")
public class Detalle_ProductoController
{
    @Autowired
    Detalle_ProductoService detalle_ProductoService;
       
    //CRUD
    @RequestMapping(value = "/createDetalleProducto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createDetalleProducto(@RequestBody Detalle_ProductoBean detalle_ProductoBean)
    {return detalle_ProductoService.createDetalleProducto(detalle_ProductoBean);}
    
    @RequestMapping(value = "/readProductoTop", method = RequestMethod.GET)
    @ResponseBody public List<Detalle_ProductoBean> readProductoTop()
    { return detalle_ProductoService.readProductoTop(); }
    
    @RequestMapping(value = "/updateDetalleProducto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateDetalleProducto(@RequestBody Detalle_ProductoBean detalle_ProductoBean)
    {return detalle_ProductoService.updateDetalleProducto(detalle_ProductoBean);}
    
    @RequestMapping(value = "/deleteDetalleProducto/{idProducto}/{idColor}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteDetalleProducto(@PathVariable int idProducto, @PathVariable int idColor)
    {return detalle_ProductoService.deleteDetalleProducto(idProducto, idColor);}   
    
    //
    @RequestMapping(value = "/readProductoPorCategoria/{idCategoria}", method = RequestMethod.GET)
    @ResponseBody  public List<Detalle_ProductoBean> readProductoPorCategoria(@PathVariable int idCategoria)
    {return detalle_ProductoService.readProductoPorCategoria(idCategoria);}
    
    @RequestMapping(value = "/readProductoPorProducto/{idProducto}", method = RequestMethod.GET)
    @ResponseBody  public List<Detalle_ProductoBean> readProductoPorProducto(@PathVariable int idProducto)
    {return detalle_ProductoService.readProductoPorProducto(idProducto);}
    
    @RequestMapping(value = "/getFotoProducto/{idProducto}/{idColor}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody public byte[] getFotoProducto(@PathVariable int idProducto, @PathVariable int idColor)
    {return detalle_ProductoService.getFotoProducto(idProducto, idColor);}
    
    @RequestMapping(value = "/guardarFoto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> guardarFoto(@RequestBody Detalle_ProductoBean detalle_ProductoBean)
    {return detalle_ProductoService.guardarFoto(detalle_ProductoBean);}
}