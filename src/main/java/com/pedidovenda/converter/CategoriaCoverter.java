package com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.pedidovenda.model.Categoria;
import com.pedidovenda.repositorios.CategoriaRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Categoria.class)
public class CategoriaCoverter  implements Converter{

	//@Inject
	private CategoriaRepositorio categoriaRep;
	
	public CategoriaCoverter() {
		categoriaRep = CDIServiceLocator.getBean(CategoriaRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext contex, UIComponent componente, String value) {
		Categoria retorno = null;
		if(value!=null){
			Long id = new Long(value);
			retorno = categoriaRep.geById(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext contex, UIComponent componente, Object value) {
		if(value!=null){
			return ((Categoria)value).getId().toString();
		}
		return "";
	}

}
