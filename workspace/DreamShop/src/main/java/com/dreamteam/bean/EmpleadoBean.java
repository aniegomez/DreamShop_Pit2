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
public class EmpleadoBean
{
	private int idEmpleado;
	private String nombre;
	private String apellido_p;
	private String apellido_m;
	private TipoDocumentoBean idTipoDocumentoBean;
	private String nro_documento;
	private String correo;
	private String foto_ruta;
	private EstadoCivilBean idEstadoCivilBean;	
	private EstadoBean idEstadoBean;
	private UsuarioBean idUsuarioBean;
	private String fe_crea;
	
	public EmpleadoBean(){}
}
