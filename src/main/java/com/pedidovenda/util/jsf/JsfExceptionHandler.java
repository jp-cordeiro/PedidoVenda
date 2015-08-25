package com.pedidovenda.util.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pedidovenda.service.NegocioException;

public class JsfExceptionHandler extends ExceptionHandlerWrapper {

	private static Log log = LogFactory.getLog(JsfExceptionHandler.class);
	
	private ExceptionHandler wrapped;

	public JsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents()
				.iterator();

		while (events.hasNext()) {
			ExceptionQueuedEvent event = events.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
					.getSource();

			Throwable exception = context.getException();

			NegocioException negocioException = getNegocioException(exception);

			boolean handled = false;

			try {
				if (exception instanceof ViewExpiredException) {
					handled = true;
					redirect("/");
				} else if (negocioException != null) {
					// Adiciona a mesagem de erro pega pela NegocioException no
					// utilitário de menssagens do sistema
					handled = true;
					FacesUtil.addErrorMessage(negocioException.getMessage());
				} else {
					handled = true;
					log.error("Erro de sitema: " + exception.getMessage(), exception );
					redirect("/Erro.xhtml");
				}
			} finally {
				if (handled) {
					events.remove();
				}
			}
		}

		getWrapped().handle();
	}

	// Recebe a pilha de exceções e a desmonta para verificar se existe uma
	// NegocioExcpetion
	private NegocioException getNegocioException(Throwable exception) {
		// Se a exceção for uma instancia de NegocioException
		if (exception instanceof NegocioException) {
			return (NegocioException) exception;
		} // Se a causa não for nula, verifica a causa da exceção dentro da
			// pilha
		else if (exception.getCause() != null) {
			return getNegocioException(exception.getCause());
		}
		return null;
	}

	private void redirect(String page) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();

			externalContext.redirect(contextPath + page);
			facesContext.responseComplete();
		} catch (IOException e) {
			throw new FacesException(e);
		}

	}
}