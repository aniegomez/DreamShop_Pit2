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
public class Detalle_ProductoBean
{
	private ProductoBean idProductoBean;
	private ColorBean idColorBean;
	private int cantidad;
	private String foto_ruta;
	private DescuentoBean idDescuentoBean;
	
	public Detalle_ProductoBean(){}
}
