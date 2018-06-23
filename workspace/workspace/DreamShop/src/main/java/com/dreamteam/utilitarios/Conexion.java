package com.dreamteam.utilitarios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;

public class Conexion
{
	private Connection conexion = null;
	
	@Autowired	
	ResourceBundle rb;
	
	public Connection getConexion()
	{					
		  try
		  {
			  rb = ResourceBundle.getBundle("application");
			  
			  conexion = DriverManager.getConnection(
					  rb.getString("spring.datasource.jdbcUrl"),
					  rb.getString("spring.datasource.username"),
					  rb.getString("spring.datasource.password"));
			  
			  return conexion;
		  }
		  catch (SQLException e)
		  {
			  e.printStackTrace();
		      return null;
		  }
	}

	public static void closeConexion(Connection conexion, ResultSet rst, Object... stament)
	{
		if(conexion != null)
		{
			try {conexion.close();}
			catch (SQLException e) {e.printStackTrace();}
		}
		
		if(rst != null)
		{
			try {rst.close();}
			catch (SQLException e) {e.printStackTrace();}
		}
		
		for(Object x:stament)
		{
			if(x instanceof PreparedStatement)
			{
				try { ( (PreparedStatement) x ).close();}
				catch (SQLException e) {e.printStackTrace();}
			}
			else if(x instanceof CallableStatement)
			{
				try { ( (CallableStatement) x ).close();}
				catch (SQLException e) {e.printStackTrace();}
			}
		}		
	}

	public static PreparedStatement setInt(PreparedStatement pstm, int indice, int valor) throws SQLException
	{
		if( valor == 0 )
			pstm.setNull(indice, Types.INTEGER);
		else
			pstm.setInt(indice, valor);
			
		return pstm;
	}

	public static void rollback(Connection conexion)
	{
		try
		{
			if( conexion != null )
				conexion.rollback();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("serial")
	public static Map<String, Object> getErrorSQL(SQLException e)
	{
		try
		{
			if( e.getErrorCode() == 1062 )
				return new HashMap<String, Object>()
				{{
					put("error", true);
					put("mensaje", getKey(e.getMessage()).toUpperCase() + ": " + getEntry(e.getMessage()) + " ya existe.");
				}};
			else if( e.getErrorCode() == 1451 )
					return new HashMap<String, Object>()
					{{
						put("error", true);
						put("mensaje", "No se puede eliminar el registro porque ya está en uso");
					}};			
			else
				return new HashMap<String, Object>()
				{{
					put("error", true);
					put("mensaje", e.getMessage());
				}};
		}
		catch (Exception e2)
		{
			return new HashMap<String, Object>()
			{{
				put("error", true);
				put("mensaje", e.getMessage());
			}};
		}
	}
	
	@SuppressWarnings("serial")
	public static Map<String, Object> getError(Exception e)
	{
		return new HashMap<String, Object>()
		{{
			put("error", true);
			put("mensaje", e.getMessage());
		}};
	}
			
	public static PreparedStatement validarNulo(PreparedStatement pstm, String titulo, int indice, Object contenido) throws SQLException
	{
		if(contenido == null)
			throw new NullPointerException(titulo + " no puede ser nulo");		
		else if(contenido instanceof String)
		{
			if(  ( (String) contenido).trim().isEmpty() )
				throw new NullPointerException(titulo + " no puede estar vacío");
			else
			{
				pstm.setString(indice, (String) contenido );
				return pstm;
			}
		}
		else if(contenido instanceof Integer)
		{
			if(  ( (Integer) contenido) < 1 )
				throw new NullPointerException(titulo + " no puede ser menor a 1");
			else
			{
				pstm.setInt(indice, (Integer) contenido );
				return pstm;
			}
		}
		else if(contenido instanceof Double)
		{
			if(  ( (Double) contenido) < 1 )
				throw new NullPointerException(titulo + " no puede ser menor a 1");
			else
			{
				pstm.setDouble(indice, (Double) contenido );
				return pstm;
			}
		}
		
		return null;
	}
	
	public static <T> PreparedStatement admitirNulo(PreparedStatement pstm, String titulo, int indice, Object contenido, T tipo) throws SQLException
	{
		if(tipo == String.class)
		{
			pstm.setString(indice, (String) contenido );
			return pstm;
		}
		else if(tipo == Integer.class)
		{
			if( contenido == null || ((Integer) contenido) <= 0 )
				pstm.setNull(indice, Types.INTEGER);
			else
				pstm.setInt(indice, (Integer) contenido );			
			return pstm;
		}
		else if(tipo == Double.class)
		{
			if( contenido == null || ((Double) contenido) < 1 )
				pstm.setNull(indice, Types.DOUBLE);
			else
				pstm.setDouble(indice, (Double) contenido );
			
			return pstm;
		}
		
		throw new NullPointerException(tipo + " no está soportado en este método. Intente: String, Integer o Double");
	}

	public static boolean validarNulo_Bean(Object objeto)
	{
		return (objeto == null);
	}
	
	@SuppressWarnings("serial")
	public static Map<String, Object> getRespuestaOK()
	{
		return new HashMap<String, Object>()
		{{
			put("error", false);
			put("mensaje", "OK");
		}};
	}
	
	@SuppressWarnings("serial")
	public static Map<String, Object> getRespuestaError(String mensaje)
	{
		return new HashMap<String, Object>()
		{{
			put("error", true);
			put("mensaje", mensaje);
		}};
	}
	
	//Métodos locales
	private static String getEntry(String mensaje)
	{
		return mensaje.substring( mensaje.indexOf("Duplicate entry '") + 16, mensaje.indexOf("' for key '")+1 );			
	}
	
	private static String getKey(String mensaje)
	{
		return mensaje.substring( mensaje.indexOf("' for key '") + 11, mensaje.indexOf("_UNIQUE'") );
	}
}
