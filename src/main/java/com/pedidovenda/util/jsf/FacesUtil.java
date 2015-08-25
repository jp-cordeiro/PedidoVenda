package com.pedidovenda.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

	public static boolean isPostBack() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	// Verifica no contexto das requisições se a requisição é postback para não
	// refazer as manipulações da página pois a mesma já fez uma requisição.
	public static boolean isNotPostBack() {
		return !isPostBack();
	}

	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
								message));
	}
	
	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, message,
								message));
	}
}
