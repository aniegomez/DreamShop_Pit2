package com.dreamteam.service;

import java.util.List;
import java.util.Map;

import com.dreamteam.bean.Cliente_PedidosBean;
import com.dreamteam.bean.Detalle_PedidoBean;
import com.dreamteam.bean.PedidoBean;

public interface PedidoService
{
	//CRUD
	public Map<String, Object> 	createPedido (PedidoBean pedidoBean);
	public List<PedidoBean> readPedidoPorCliente(String fecha, int idEstado, int idCliente);
	public List<Detalle_PedidoBean> readDetallePedido(int idPedido);
	public List<PedidoBean> readPedidoFiltros(String fecha, int idEstado, String cliente);
	public Map<String, Object> estadoPedido(int idEstado, List<Integer> listaPedido);
	public List<Cliente_PedidosBean> readClientes();
}