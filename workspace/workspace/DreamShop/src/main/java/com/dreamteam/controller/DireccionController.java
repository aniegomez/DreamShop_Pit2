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

import com.dreamteam.bean.DireccionBean;
import com.dreamteam.service.DireccionService;

@RestController
@RequestMapping("/direccion")
public class DireccionController
{
    @Autowired
    DireccionService direccionService;

    @RequestMapping(value = "/createDireccion", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createDireccion(@RequestBody DireccionBean direccionBean)
	{return direccionService.createDireccion(direccionBean);}
	
    @RequestMapping(value = "/readDireccionPorCliente/{idCliente}", method = RequestMethod.GET)
    @ResponseBody public List<DireccionBean> readDireccionPorCliente(@PathVariable int idCliente)
	{return direccionService.readDireccionPorCliente(idCliente);}
}