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
public class ProductoBean
{
	private int idProducto;
	private String descripcion;
	private double precio;
	private CategoriaBean idCategoriaBean;
	private MarcaBean idMarcaBean;	
	private EstadoBean idEstadoBean;
	private UsuarioBean idUsuarioBean;
	
	public ProductoBean(){}
}