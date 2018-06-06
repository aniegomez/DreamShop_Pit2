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
public class MedioPagoBean
{
	private int idPago;
	private String descripcion;
	private EstadoBean idEstadoBean;
	
	public MedioPagoBean(){}
}
