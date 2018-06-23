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
public class TarjetaBean
{
	private String nro_tarjeta;
	private int mes_vence;
	private int anio_vence;
	private String nombre;
	private String apellido_p;
	private String nro_cuenta;
	private String nro_interbancario;
	private BancoBean idBancoBean;
	private EstadoBean idEstadoBean;
	
	public TarjetaBean(){}
}
