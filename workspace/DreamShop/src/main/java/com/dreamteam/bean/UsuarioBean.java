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
public class UsuarioBean
{
	private int idUsuario;
	private String usuario;
	private String clave;
	private EmpleadoBean idEmpleadoBean;
	private RolBean idRolBean;
	private EstadoBean idEstadoBean;	
	private String fe_crea;
	private UsuarioBean usu_creaBean;
	
	public UsuarioBean(){}
}
