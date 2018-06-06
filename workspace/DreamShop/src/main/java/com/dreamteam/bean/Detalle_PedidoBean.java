package com.dreamteam.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Detalle_PedidoBean
{
	private PedidoBean idPedido;
	private Detalle_ProductoBean detalle_ProductoBean;	
	private int cantidad;
	private double precio;
	private DescuentoBean idDescuentoBean;
	private double subTotal;
	
	public Detalle_PedidoBean(){}
}
