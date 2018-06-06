package com.dreamteam.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.bean.DescuentoBean;
import com.dreamteam.service.DescuentoService;

@RestController
@RequestMapping("/descuento")
public class DescuentoController
{
    @Autowired
    DescuentoService descuentoService;

    @RequestMapping(value = "/createDescuento", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createDescuento(@RequestBody DescuentoBean descuentoBean)
	{return  descuentoService.createDescuento(descuentoBean);}
	
    @RequestMapping(value = "/readDescuento", method = RequestMethod.GET)
    @ResponseBody public List<DescuentoBean> readDescuento()
	{return  descuentoService.readDescuento();}
	
    @RequestMapping(value = "/updateDescuento", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateDescuento(@RequestBody DescuentoBean descuentoBean)
	{return  descuentoService.updateDescuento(descuentoBean);}
	
    @RequestMapping(value = "/deleteDescuento/{idDescuento}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteDescuento(@PathVariable int idDescuento)
	{return  descuentoService.deleteDescuento(idDescuento);}
    
    @RequestMapping(value = "/readDescuentoActivo", method = RequestMethod.GET)
    @ResponseBody public List<DescuentoBean> readDescuentoActivo()
	{return  descuentoService.readDescuentoActivo();}
    
    @RequestMapping(value = "/asignarDescuentoProducto/{idDescuento}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> asignarDescuentoProducto(@PathVariable int idDescuento, @RequestBody List<Integer> listaProducto)
    {return  descuentoService.asignarDescuentoProducto(idDescuento, listaProducto);}
    
    @RequestMapping(value = "/asignarDescuentoDetalleProducto/{idDescuento}/{idProducto}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> asignarDescuentoDetalleProducto(@PathVariable int idDescuento, @PathVariable int idProducto, @RequestBody List<Integer> listaDetalleProducto)
    {return  descuentoService.asignarDescuentoDetalleProducto(idDescuento, idProducto, listaDetalleProducto);}
    
    @RequestMapping(value = "/desAsignarDescuentoProducto/{idProducto}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> desAsignarDescuentoProducto(@PathVariable int idProducto)
    {return  descuentoService.desAsignarDescuentoProducto(idProducto);}
    
    @RequestMapping(value = "/desAsignarDescuentoDetalleProducto/{idProducto}/{idColor}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> desAsignarDescuentoDetalleProducto(@PathVariable int idProducto, @PathVariable int idColor)
    {return  descuentoService.desAsignarDescuentoDetalleProducto(idProducto, idColor);}
}