package com.pedidovenda.repositorios;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.pedidovenda.model.Usuario;

public class UsuarioRepositorio implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Usuario porId(Long id){
		return manager.find(Usuario.class, id);
	}
	
	public List<Usuario> vendedores(){
		//Filtrar apenas vendedores
		return this.manager.createQuery("from Usuario",Usuario.class).getResultList();
	}

	public Usuario porLogin(String login) {
		Usuario usuario = null;
		try {
			usuario = this.manager.createQuery("from Usuario where lower(login) = :login",Usuario.class)
					.setParameter("login",login.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			//Nenhum usuário com o e-mail informado
		}
		usuario = this.manager.createQuery("from Usuario where lower(login) = :login",Usuario.class)
				.setParameter("login",login.toLowerCase()).getSingleResult();
		
		return usuario;
	}
}
