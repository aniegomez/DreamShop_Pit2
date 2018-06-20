package com.dreamteam.utilitarios;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class BeanConfiguration
{
	@Autowired
	EmailBean emailBean;
	
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource()
    {    	
        return DataSourceBuilder.create().build();
    }
    
	@Bean
	public JavaMailSender getJavaMailSender()
	{		
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(emailBean.getHost());
	    mailSender.setPort(emailBean.getPort());
	     
	    mailSender.setUsername(emailBean.getUsername());
	    mailSender.setPassword(emailBean.getPassword());
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", emailBean.getAuth());
	    props.put("mail.smtp.starttls.enable", emailBean.getEnable());
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}	
}
