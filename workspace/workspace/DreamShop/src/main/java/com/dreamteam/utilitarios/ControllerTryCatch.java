package com.dreamteam.utilitarios;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RestController
public class ControllerTryCatch
{
	private String mensaje;
	
	@ExceptionHandler(Exception.class)
	public void handleBadRequests(HttpServletResponse response, Exception e) throws IOException
	{
		mensaje = e.getMessage();
		
		if(e instanceof HttpRequestMethodNotSupportedException)
			response.sendError(HttpStatus.BAD_REQUEST.value(),
				"El método que ingresó no es válido (" +
				mensaje.substring( mensaje.indexOf("method '")+8 , mensaje.indexOf("' not supported") ) + ")"
			);
		else if(e instanceof HttpMessageNotReadableException)
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Usted no envió parámetros o no ingresó correctamente los datos del objeto (bean).");
		else if(e instanceof MissingServletRequestParameterException)
			response.sendError(HttpStatus.BAD_REQUEST.value(), 
				"Usted no envió el parámetro " +
				mensaje.substring( mensaje.indexOf("'"), mensaje.lastIndexOf("'") +1 ) +
				" de tipo " + mensaje.substring( mensaje.indexOf("Required") + 9, mensaje.indexOf(" parameter") ));
		else if(e instanceof MethodArgumentTypeMismatchException)
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Usted envió parámetro de tipo '" +
					mensaje.substring(mensaje.indexOf("Failed to convert value of type 'java.lang.") +43, mensaje.indexOf(" to required type ")) +
					" en vez del tipo " +
					mensaje.substring(mensaje.indexOf("to required type ")+17, mensaje.indexOf("; nested exception is "))
			);
		else if(e instanceof UncategorizedSQLException)
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Error interno con la base de datos. Intente nuevamente, si el problema persiste comuníquese con el administrador");
		else if(e instanceof ClientAbortException)
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Cliente abortó operación.");
		else if(e instanceof HttpMediaTypeNotSupportedException)
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"El contenido que envió no es válido (" +
					mensaje.substring( mensaje.indexOf("Content type")+13 , mensaje.indexOf("' not supported") ) + ")"
				);
		else if(e instanceof IllegalArgumentException)
			response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		else
		{
			e.printStackTrace();
			response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

}