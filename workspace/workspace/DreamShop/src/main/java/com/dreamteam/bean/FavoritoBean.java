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
public class FavoritoBean
{
	private ClienteBean idClienteBean;
	private Detalle_ProductoBean detalle_ProductoBean;
	
	public FavoritoBean(){}
}
