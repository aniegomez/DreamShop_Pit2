package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.ClienteBean;
import com.dreamteam.dao.ClienteDAO;
import com.dreamteam.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService
{
	@Autowired
	ClienteDAO clienteDAO;
	
	//CRUD
	@Override
	public Map<String, Object> 	createCliente(ClienteBean clienteBean){return clienteDAO.createCliente(clienteBean);}
	
	@Override
	public List<ClienteBean> 	readCliente(){return clienteDAO.readCliente();}
	
	@Override
	public Map<String, Object> 	updateCliente(ClienteBean clienteBean){return clienteDAO.updateCliente(clienteBean);}
	
	@Override
	public Map<String, Object> 	deleteCliente(int idCliente){return clienteDAO.deleteCliente(idCliente);}
	//
	
	@Override
	public ClienteBean 			getCliente(int idCliente){return clienteDAO.getCliente(idCliente);}
	
	@Override
	public ClienteBean 			loginCliente(ClienteBean clienteBean){return clienteDAO.loginCliente(clienteBean);}
	
	@Override
	public Map<String, Object> 	cambiarClave(ClienteBean clienteBean){return clienteDAO.cambiarClave(clienteBean);}
	
	@Override
	public Map<String, Object> 	recuperarClave(ClienteBean clienteBean){return clienteDAO.recuperarClave(clienteBean);}
	
	@Override
	public Map<String, Object> 	guardarFoto(ClienteBean clienteBean) {return clienteDAO.guardarFoto(clienteBean);}
	
	@Override
	public byte[] getFotoCliente(int idCliente) {return clienteDAO.getFotoCliente(idCliente);}
}