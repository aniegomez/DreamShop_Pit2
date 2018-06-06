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

import com.dreamteam.bean.EmpleadoBean;
import com.dreamteam.service.EmpleadoService;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController
{
    @Autowired
    EmpleadoService empleadoService;

    @RequestMapping(value = "/createEmpleado", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createEmpleado(@RequestBody EmpleadoBean empleadoBean)
	{return empleadoService.createEmpleado(empleadoBean);}
    
    @RequestMapping(value = "/readEmpleado", method = RequestMethod.GET)
    @ResponseBody public List<EmpleadoBean> readEmpleado() {return empleadoService.readEmpleado(); }   

    @RequestMapping(value = "/updateEmpleado", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateEmpleado(@RequestBody EmpleadoBean empleadoBean)
	{return empleadoService.updateEmpleado(empleadoBean);}

    @RequestMapping(value = "/deleteEmpleado/{idEmpleado}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteEmpleado(@PathVariable int idEmpleado)
	{return empleadoService.deleteEmpleado(idEmpleado);}
}