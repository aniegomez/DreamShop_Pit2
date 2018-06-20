package com.dreamteam.utilitarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService
{  
    @Autowired
    public JavaMailSender emailSender;
 
    @SuppressWarnings("serial")
	public void enviarMensaje(String destino, String titulo, String contenido)
    {
        emailSender.send(new SimpleMailMessage()
        {{
        	setTo(destino); 
            setSubject(titulo); 
            setText(contenido);
        }});
    }
}