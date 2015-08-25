package com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.pedidovenda.model.Usuario;
import com.pedidovenda.repositorios.UsuarioRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Usuario.class)
public class UsuarioConverter implements Converter{

	private UsuarioRepositorio repUsuario;
	
	public UsuarioConverter() {
		this.repUsuario = (UsuarioRepositorio)CDIServiceLocator.getBean(UsuarioRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Usuario retorno =null;
		if(value!=null){
			retorno = repUsuario.porId(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value!=null){
			Usuario usuario = (Usuario) value;
			return usuario.getId() == null ? null : usuario.getId().toString();
		}
		return "";
	}

}
