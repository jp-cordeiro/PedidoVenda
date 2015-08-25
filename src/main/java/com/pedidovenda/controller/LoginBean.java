package com.pedidovenda.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pedidovenda.util.jsf.FacesUtil;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	public void preRender(){
		if("true".equals(request.getParameter("invalid"))){
			FacesUtil.addErrorMessage("Usuário ou senha inválido.");
		}
	}
	
	// Método que efetua o login na aplicação
	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(request, response);

		facesContext.responseComplete();// Interrompe o ciclo de vida do JSF,
										// pois já dispachou a requisição no
										// Spring.
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
