package com.dreamteam.utilitarios;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailBean
{  
	private String host;
	private int port;
	private String username;
	private String password;
	private String auth;
	private String enable;
}