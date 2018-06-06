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

import com.dreamteam.bean.ProductoBean;
import com.dreamteam.service.ProductoService;

@RestController
@RequestMapping("/producto")
public class ProductoController
{
    @Autowired
    ProductoService productoService;
    
    @RequestMapping(value = "/createProducto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createProducto(@RequestBody ProductoBean productoBean)
    {return productoService.createProducto(productoBean);}
    
    @RequestMapping(value = "/readProducto", method = RequestMethod.GET)
    @ResponseBody public List<ProductoBean> readProducto()
    { return productoService.readProducto(); }
    
    @RequestMapping(value = "/updateProducto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateProducto(@RequestBody ProductoBean productoBean)
    {return productoService.updateProducto(productoBean);}
    
    @RequestMapping(value = "/deleteProducto/{idProducto}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteProducto(@PathVariable int idProducto)
    {return productoService.deleteProducto(idProducto);}
}