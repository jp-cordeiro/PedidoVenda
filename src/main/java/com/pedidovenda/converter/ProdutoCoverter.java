package com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.ProdutoRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Produto.class)
public class ProdutoCoverter  implements Converter{

	//@Inject
	private ProdutoRepositorio produtoRep;
	
	public ProdutoCoverter() {
		produtoRep = CDIServiceLocator.getBean(ProdutoRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext contex, UIComponent componente, String value) {
		Produto retorno = null;
		if(value!=null){
			Long id = new Long(value);
			retorno = produtoRep.getById(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext contex, UIComponent componente, Object value) {
		if(value!=null){
			Produto produto = (Produto) value;
			return produto.getId() == null ? null : produto.getId().toString();
		}
		return "";
	}

}
