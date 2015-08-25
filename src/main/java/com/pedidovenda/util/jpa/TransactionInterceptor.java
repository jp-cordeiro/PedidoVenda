package com.pedidovenda.util.jpa;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.interceptor.AroundInvoke;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Interceptor
// Define como interceptador
@Transactional
// Interceptador da anotação Transactional
// Sempre que um método Transacional for chamado, o método invoke é chamado
// antes.
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject EntityManager manager;

	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
		EntityTransaction trx = manager.getTransaction();
		boolean criador = false;

		try {
			if (!trx.isActive()) {
				// truque para fazer rollback no que já passou
				// senão um futuro commit, confirmaria até mesmo operações sem
				// transação
				trx.begin();
				trx.rollback();

				// agora sim inicia a transação
				trx.begin();

				criador = true;
			}

			return context.proceed();// permite que o processo siga, pois já foi
										// interceptado
		} catch (Exception e) {
			if (trx != null && criador) {
				trx.rollback();
			}
			throw e;
		} finally {
			if (trx != null && trx.isActive() && criador) {
				trx.commit();
			}
		}
	}

}
