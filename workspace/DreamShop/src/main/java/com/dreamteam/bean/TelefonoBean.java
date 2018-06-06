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
public class TelefonoBean
{
	private int idTelefono;
	private ClienteBean idClienteBean;
	private EstadoBean idEstadoBean;
	private String numero;
	
	public TelefonoBean(){}
}
