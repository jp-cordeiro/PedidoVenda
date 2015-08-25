package com.pedidovenda.repositorios;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.filter.ProdutoFilter;
import com.pedidovenda.service.NegocioException;
import com.pedidovenda.util.jpa.Transactional;

@Named
public class ProdutoRepositorio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto Add(Produto produto) {
		return produto = manager.merge(produto);
	}

	public Produto getById(Long id) {
		return manager.find(Produto.class, id);
	}

	//Consulta produtos por SKU
	public Produto getSku(String sku) {
		try {
			return manager
					.createQuery("from Produto where upper(sku) = :sku",
							Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	// Informa que o método irá realizar uma transação no BD
	public void remove(Produto produto) {
		try {
			produto = getById(produto.getId());
			manager.remove(produto);
			manager.flush();// Método que realiza a execução de todas as
			// pendências
			// existentes para realizar no BD.Pois se não fosse
			// chamado o flush iria dispará uma exceção.
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		// Criteria de equals
		if (StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		// Criteria de like case insensitive
		// where nome like '%joao%'
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public List<Produto> porNome(String nome) {
		//Consultará o produto pelo nome e retornará uma lista
		return this.manager.createQuery("from Produto where upper(nome) like :nome",Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}
	
	public void carregarProdutoLinhaEditavel(){
		
	}
}
