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
public class Cliente_PedidosBean
{
	private ClienteBean idClienteBean;
	private int pedidos;
	
	public Cliente_PedidosBean(){}
}