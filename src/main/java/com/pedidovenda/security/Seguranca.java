package com.pedidovenda.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;

	public String getNomeUsuario() {
		String nome = null;

		// Recebe o usuário logado no sistema
		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getLogin();
		}

		return nome;
	}

	@Produces
	@UsuarioLogado
	public  UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();
		// Através do FacesContext entra-se na CurrentInstance e no
		// ContextoExterno para pegar o usuário logado

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
			// O get pricipal retorna um UsuárioSistema, é feito um cast para
			// forçar a conversão
		}
		return usuario;
	}

	public boolean isEmitirPedidoPermitido() {
		return externalContext.isUserInRole("ADMINISTRADORES")
				|| externalContext.isUserInRole("VENDEDORES");
	}
	
	public boolean isCancelarPedidoPermitido() {
		return externalContext.isUserInRole("ADMINISTRADORES")
				|| externalContext.isUserInRole("VENDEDORES");
	}
}
