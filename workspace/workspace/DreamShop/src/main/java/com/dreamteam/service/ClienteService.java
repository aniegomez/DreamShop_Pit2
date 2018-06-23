package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.ClienteBean;

public interface ClienteService
{
	//CRUD
	public Map<String, Object> 	createCliente(ClienteBean clienteBean);
	public List<ClienteBean> 	readCliente();
	public Map<String, Object> 	updateCliente(ClienteBean clienteBean);
	public Map<String, Object> 	deleteCliente(int idCliente);
	//
	public ClienteBean 			getCliente(int idCliente);
	public ClienteBean 			loginCliente(ClienteBean clienteBean);
	public Map<String, Object> 	cambiarClave(ClienteBean clienteBean);
	public Map<String, Object> 	recuperarClave(ClienteBean clienteBean);
	public Map<String, Object> 	guardarFoto(ClienteBean clienteBean);
	public byte[] getFotoCliente(int idCliente);
}