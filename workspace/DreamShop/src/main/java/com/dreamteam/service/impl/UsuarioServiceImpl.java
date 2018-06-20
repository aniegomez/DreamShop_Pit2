package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.dao.UsuarioDAO;
import com.dreamteam.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
    @Autowired
    private UsuarioDAO usuarioDAO;

	@Override
	public Map<String, Object> createUsuario(UsuarioBean usuarioBean)
	{return usuarioDAO.createUsuario(usuarioBean);}
    
    @Override public List<UsuarioBean> readUsuario()
    {return  usuarioDAO.readUsuario();}
        
	@Override public Map<String, Object> updateUsuario(UsuarioBean usuarioBean)
	{return usuarioDAO.updateUsuario(usuarioBean);}

	@Override
	public Map<String, Object> deleteUsuario(int idUsuario)
	{return usuarioDAO.deleteUsuario(idUsuario);}
	
	@Override public UsuarioBean loginUsuario(UsuarioBean usuarioBean)
    {return usuarioDAO.loginUsuario(usuarioBean);}
}