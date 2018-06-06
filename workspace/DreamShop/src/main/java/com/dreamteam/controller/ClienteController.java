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

import com.dreamteam.bean.ClienteBean;
import com.dreamteam.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController
{
    @Autowired
    ClienteService clienteService;

    @RequestMapping(value = "/createCliente", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createCliente(@RequestBody ClienteBean clienteBean)
    {return clienteService.createCliente(clienteBean);}
	
    @RequestMapping(value = "/readCliente", method = RequestMethod.GET)
    @ResponseBody public List<ClienteBean> readCliente()
    {return clienteService.readCliente();}
	
    @RequestMapping(value = "/updateCliente", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateCliente(@RequestBody ClienteBean clienteBean)
    {return clienteService.updateCliente(clienteBean);}
	
    @RequestMapping(value = "/deleteCliente/{idCliente}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteCliente(@PathVariable int idCliente)
    {return clienteService.deleteCliente(idCliente);}
	
    @RequestMapping(value = "/getCliente/{idCliente}", method = RequestMethod.GET)
    @ResponseBody public ClienteBean getCliente(@PathVariable int idCliente)
    {return clienteService.getCliente(idCliente);}
    
    @RequestMapping(value = "/loginCliente", method = RequestMethod.POST)
    @ResponseBody public ClienteBean loginCliente(@RequestBody ClienteBean clienteBean)
    {return clienteService.loginCliente(clienteBean);}
	
    @RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> cambiarClave(@RequestBody ClienteBean clienteBean)
    {return clienteService.cambiarClave(clienteBean);}
	
    @RequestMapping(value = "/recuperarClave", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> recuperarClave(@RequestBody ClienteBean clienteBean)
    {return clienteService.recuperarClave(clienteBean);}
    
    @RequestMapping(value = "/guardarFoto", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> guardarFoto(@RequestBody ClienteBean clienteBean)
    {            	
    	return clienteService.guardarFoto(clienteBean);
    }
    
    @RequestMapping(value = "/getFotoCliente/{idCliente}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody public byte[] getFotoCliente(@PathVariable int idCliente)
    {return clienteService.getFotoCliente(idCliente);}
}