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
public class ProvinciaBean
{
	private int idProvincia;	
	private String descripcion;
	private DepartamentoBean idDepartamentoBean;
	private EstadoBean idEstadoBean;
	
	public ProvinciaBean(){}
}
