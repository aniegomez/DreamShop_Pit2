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
public class DepartamentoBean
{
	private int idDepartamento;	
	private String descripcion;
	private PaisBean idPaisBean;
	private EstadoBean idEstadoBean;
	
	public DepartamentoBean(){}
}
