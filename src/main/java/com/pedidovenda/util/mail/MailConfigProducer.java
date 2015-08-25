package com.pedidovenda.util.mail;

import java.io.IOException;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.SimpleMailConfig;

public class MailConfigProducer {

	// Como a implementação de SimpleEmail não possui suporte ao CDI é
	// necessário criar um produtor
	// que gere uma instância para ser injetada.
	@Produces
	@ApplicationScoped
	public SessionConfig getMailConfig() throws IOException {
		Properties props = new Properties();

		props.load(getClass().getResourceAsStream("/mail.properties"));

		 SimpleMailConfig config = new SimpleMailConfig();
		 //Criada uma propriedade para armazenar os dados na mesma, para que estes não
		 //estejam inline no código.
		 config.setServerHost(props.getProperty("mail.server.host"));
		 config.setServerPort(Integer.parseInt(props
		 .getProperty("mail.server.port")));
		  config.setEnableSsl(Boolean.parseBoolean(props
		  .getProperty("mail.enable.ssl")));
//		 config.setEnableTls(Boolean.parseBoolean(props
//		 .getProperty("mail.enable.tsl")));
		 config.setAuth(Boolean.parseBoolean(props.getProperty("mail.auth")));
		 config.setUsername(props.getProperty("mail.username"));
		 config.setPassword(props.getProperty("mail.password"));

//		Email config = new SimpleEmail();
//
//		config.setHostName(props.getProperty("mail.server.host"));
//		config.setSmtpPort(Integer.parseInt(props
//				.getProperty("mail.server.port")));
//		config.setAuthentication("mail.username", "mail.password");
//		config.setSSLOnConnect(Boolean.parseBoolean(props
//				.getProperty("mail.enable.ssl")));
//		config.setFrom("mail.username");
		
		return config;

	}
}
