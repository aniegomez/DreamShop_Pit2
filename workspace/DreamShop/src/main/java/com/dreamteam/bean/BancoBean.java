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
public class BancoBean
{
	private int idBanco;
	private String descripcion;
	private String abreviatura;
	private EstadoBean idEstadoBean;
	
	public BancoBean(){}
}
