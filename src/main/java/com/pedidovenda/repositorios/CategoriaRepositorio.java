package com.pedidovenda.repositorios;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.pedidovenda.model.Categoria;

public class CategoriaRepositorio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Categoria> getAll() {

		return manager.createQuery("from Categoria where categoriaPai is null",
				Categoria.class).getResultList();
	}

	public Categoria geById(Long id) {
		return manager.find(Categoria.class, id);
	}

	public List<Categoria> getSubCategorias(Categoria categoriaPai) {
		return manager
				.createQuery("from Categoria where categoriaPai = :raiz",
						Categoria.class).setParameter("raiz", categoriaPai)
				.getResultList();
	}

}
