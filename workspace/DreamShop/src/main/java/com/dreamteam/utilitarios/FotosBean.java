package com.dreamteam.utilitarios;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.fotos")
public class FotosBean
{  
	private String ruta;
	private String producto;
	private String empleado;
	private String cliente;
	
	public String getRutaProducto()
	{
		return this.ruta + File.separator + this.producto + File.separator;
	}
	
	public String getRutaEmpleado()
	{
		return this.ruta + File.separator + this.empleado + File.separator;
	}
	
	public String getRutaCliente()
	{
		return this.ruta + File.separator + this.cliente + File.separator;
	}
}