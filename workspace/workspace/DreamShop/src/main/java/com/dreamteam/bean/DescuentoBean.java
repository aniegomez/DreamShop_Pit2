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
public class DescuentoBean
{
	private int idDescuento;
	private String descripcion;
	private double porcentaje;
	private String fe_inicio;
	private String fe_termino;
	private String fe_crea;
	private EstadoBean idEstadoBean;
	private UsuarioBean idUsuarioBean;

	public DescuentoBean(){}
}
