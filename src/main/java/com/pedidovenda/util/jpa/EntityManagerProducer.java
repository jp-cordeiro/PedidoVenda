package com.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;

	public EntityManagerProducer() {
		factory = Persistence.createEntityManagerFactory("pedidovendadb");
	}

	@Produces
	@RequestScoped
	// Produtor de EntityManager
	public EntityManager createEtityManager() {
		return factory.createEntityManager();
	}

	// Quando o escopo finalizar o EntityManager precisa ser fechado, o
	// @Disposes funciona como um gatilho para acionar o método após o fim da
	// requisição.
	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}
}
