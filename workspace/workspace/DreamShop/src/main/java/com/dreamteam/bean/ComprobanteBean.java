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
public class ComprobanteBean
{
	private int idComprobante;
	private PedidoBean idPedidoBean;
	private PagoBean idPagoBean;
	private String fecha_pago;
	private EstadoBean idEstadoBean;
	
	public ComprobanteBean(){}
}
