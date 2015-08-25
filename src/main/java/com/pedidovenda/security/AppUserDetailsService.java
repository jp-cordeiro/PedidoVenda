package com.pedidovenda.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pedidovenda.model.Grupo;
import com.pedidovenda.model.Usuario;
import com.pedidovenda.repositorios.UsuarioRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

//Classe que implementa interface do SpringSecurity, onde ela provê os detalhes de usuários
public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		UsuarioRepositorio repUsuario = CDIServiceLocator.getBean(UsuarioRepositorio.class);
		Usuario usuario = repUsuario.porLogin(login);
		UsuarioSistema user = null;
		
		if(usuario != null){
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}
		
		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario){
		List<SimpleGrantedAuthority> grupos = new ArrayList<>();
		
		for(Grupo grupo : usuario.getGrupos()){
			grupos.add(new SimpleGrantedAuthority(grupo.getNome().toUpperCase()));
		}
		
		return grupos;
	}
}
