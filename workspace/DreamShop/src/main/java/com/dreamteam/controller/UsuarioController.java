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

import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController
{
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/createUsuario", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createUsuario(@RequestBody UsuarioBean usuarioBean)
	{return usuarioService.createUsuario(usuarioBean);}
    
    @RequestMapping(value = "/readUsuario", method = RequestMethod.GET)
    @ResponseBody public List<UsuarioBean> readUsuario() { return usuarioService.readUsuario(); }
            
    @RequestMapping(value = "/updateUsuario", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> updateUsuario(@RequestBody UsuarioBean usuarioBean)
	{return usuarioService.updateUsuario(usuarioBean);}

    @RequestMapping(value = "/deleteUsuario/{idUsuario}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> deleteUsuario(@PathVariable int idUsuario)
	{return usuarioService.deleteUsuario(idUsuario);}
    
    @RequestMapping(value = "/loginUsuario", method = RequestMethod.POST)
    @ResponseBody  public UsuarioBean loginUsuario(@RequestBody UsuarioBean usuarioBean) {return usuarioService.loginUsuario(usuarioBean);}
}