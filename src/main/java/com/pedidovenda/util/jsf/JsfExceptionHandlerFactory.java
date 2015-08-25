package com.pedidovenda.util.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class JsfExceptionHandlerFactory extends ExceptionHandlerFactory{

	private ExceptionHandlerFactory fabrica;
	
	public JsfExceptionHandlerFactory(ExceptionHandlerFactory fabrica) {
		this.fabrica = fabrica;
	}
	
	@Override
	public ExceptionHandler getExceptionHandler() {
		return new JsfExceptionHandler(fabrica.getExceptionHandler());
	}

}
