package com.dreamteam.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.bean.Cliente_PedidosBean;
import com.dreamteam.bean.Detalle_PedidoBean;
import com.dreamteam.bean.PedidoBean;
import com.dreamteam.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController
{
    @Autowired
    PedidoService pedidoService;

    @RequestMapping(value = "/createPedido", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> createPedido(@RequestBody PedidoBean pedidoBean)
    {return pedidoService.createPedido(pedidoBean);}
    
    @RequestMapping(value = "/readPedidoPorCliente/{idCliente}", method = RequestMethod.GET)
    public List<PedidoBean> readPedidoPorCliente(@RequestParam String fecha, @RequestParam int idEstado, @PathVariable int idCliente)
	{return pedidoService.readPedidoPorCliente(fecha, idEstado, idCliente);}
    
    @RequestMapping(value = "/readDetallePedido/{idPedido}", method = RequestMethod.GET)
    @ResponseBody public List<Detalle_PedidoBean> readDetallePedido(@PathVariable int idPedido)
	{return pedidoService.readDetallePedido(idPedido);}
        
    @RequestMapping(value = "/readPedidoFiltros", method = RequestMethod.GET)
    @ResponseBody public List<PedidoBean> readPedidoFiltros(@RequestParam String fecha, @RequestParam int idEstado, @RequestParam String cliente)
	{return pedidoService.readPedidoFiltros(fecha, idEstado, cliente);}
    
    @RequestMapping(value = "/estadoPedido/{idEstado}", method = RequestMethod.POST)
    @ResponseBody public Map<String, Object> estadoPedido(@PathVariable int idEstado, @RequestBody List<Integer> listaPedido)
	{return pedidoService.estadoPedido(idEstado, listaPedido);}
        
    @RequestMapping(value = "/readClientes", method = RequestMethod.GET)
    @ResponseBody public List<Cliente_PedidosBean> readClientes()
	{return pedidoService.readClientes();}
}