package com.dreamteam.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamteam.bean.Cliente_PedidosBean;
import com.dreamteam.bean.Detalle_PedidoBean;
import com.dreamteam.bean.PedidoBean;
import com.dreamteam.dao.PedidoDAO;
import com.dreamteam.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService
{
	@Autowired
	PedidoDAO pedidoDAO;
	
	//CRUD
	@Override
	public Map<String, Object> 	createPedido(PedidoBean pedidoBean)
	{return pedidoDAO.createPedido(pedidoBean);}
	
	@Override
	public List<PedidoBean> readPedidoPorCliente(String fecha, int idEstado, int idCliente)
	{return pedidoDAO.readPedidoPorCliente(fecha, idEstado, idCliente);}
	
	@Override
	public List<Detalle_PedidoBean> readDetallePedido(int idPedido)
	{return pedidoDAO.readDetallePedido(idPedido);}
	
	@Override
	public List<PedidoBean> readPedidoFiltros(String fecha, int idEstado, String cliente)
	{return pedidoDAO.readPedidoFiltros(fecha, idEstado, cliente);}
	
	@Override
	public Map<String, Object> estadoPedido(int idEstado, List<Integer> listaPedido)
	{return pedidoDAO.estadoPedido(idEstado, listaPedido);}
	
	@Override
	public List<Cliente_PedidosBean> readClientes()
	{return pedidoDAO.readClientes();}
}