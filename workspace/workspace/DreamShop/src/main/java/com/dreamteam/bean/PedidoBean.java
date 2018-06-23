package com.dreamteam.bean;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PedidoBean
{
	private int idPedido;
	private double total;
	private String fecha_pedido;
	private ClienteBean idClienteBean;
	private EstadoBean idEstadoBean;
	private List<Detalle_PedidoBean> listaDetallePedido;
	
	public PedidoBean(){}
}