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
public class PagoBean
{
	private int idPago;
	private ClienteBean idClienteBean;
	private MedioPagoBean idMedioPagoBean;
	private TarjetaBean nroTarjetaBean;
	
	public PagoBean(){}
}
