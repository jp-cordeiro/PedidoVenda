package com.pedidovenda.util.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AppContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//informa a liguagem de expressão que não é para converter inteiros para 0
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO","false");
		
	}

}
