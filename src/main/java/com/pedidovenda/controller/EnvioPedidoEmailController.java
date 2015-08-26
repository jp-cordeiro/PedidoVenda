package com.pedidovenda.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.velocity.tools.generic.NumberTool;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
import com.pedidovenda.model.Pedido;
import com.pedidovenda.util.jsf.FacesUtil;
import com.pedidovenda.util.mail.Mailer;

@Named
@RequestScoped
public class EnvioPedidoEmailController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;

	@Inject
	@PedidoEdicao
	// Pega o pedido que está sendo manipulado para poder trata-lo
	private Pedido pedido;

	public void enviarPedido(){
		MailMessage message = mailer.novaMensagem();

		message.to(this.pedido.getCliente().getEmail())
				.subject(
						"Pedido de venda com o status de " + pedido.getStatus().getDescricao()
								+ " de Nº " + pedido.getId() + ".")
				.bodyHtml(
						new VelocityTemplate(getClass().getResourceAsStream(
								"/emails/pedido.template")))
				.put("pedido",this.pedido)
				.put("numberTool", new NumberTool())
				.put("locale",new Locale("pt","BR"))
				.send();

		// Properties props = new Properties();
		//
		// props.load(getClass().getResourceAsStream("/mail.properties"));
		//
		// SimpleEmail email = new SimpleEmail();
		// email.setHostName(props.getProperty("mail.server.host")); // o
		// servidor SMTP para envio do
		// // e-mail
		// email.addTo("vicent.sistemas@hotmail.com"); // destinatário
		// email.setFrom(props.getProperty("mail.username")); // remetente
		// email.setSmtpPort(Integer.parseInt(props
		// .getProperty("mail.server.port")));
		// email.setSubject("Mensagem de Teste");// assunto do e-mail
		// email.setMsg("Teste de Email utilizando commons-email");// conteudo
		// do e-mail
		// email.send(); // envia o e-mail

		// email.setDebug(true);
		// email.setHostName("smtp.live.com");
		// email.setAuthentication("vicent.sistemas@hotmail.com","vs23031575");
		// email.setSmtpPort(465);
		// email.setSSL(true);
		// email.addTo("jpc.araujoti@gmail.com"); //pode ser qualquer um email
		// email.setFrom("vicent.sistemas@hotmail.com"); //aqui necessita ser o
		// email que voce fara a autenticacao
		// email.setSubject("Teste");
		// email.setMsg("Mensagem Testando");
		// email.send();

		FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso.");
	}
}
