package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.UsuarioBean;

public interface UsuarioService
{
	public Map<String, Object> createUsuario(UsuarioBean usuarioBean);
	public List<UsuarioBean>   readUsuario();
	public Map<String, Object> updateUsuario(UsuarioBean usuarioBean);
	public Map<String, Object> deleteUsuario(int idUsuario);		
	public UsuarioBean loginUsuario(UsuarioBean usuarioBean);
}