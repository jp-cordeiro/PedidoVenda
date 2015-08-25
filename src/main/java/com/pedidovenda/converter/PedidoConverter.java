package com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.PedidoRepositorio;
import com.pedidovenda.repositorios.ProdutoRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Pedido.class)
public class PedidoConverter  implements Converter{

	//@Inject
	private PedidoRepositorio pedidoRep;
	
	public PedidoConverter() {
		pedidoRep = CDIServiceLocator.getBean(PedidoRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext contex, UIComponent componente, String value) {
		Pedido retorno = null;
		if(value!=null){
			Long id = new Long(value);
			retorno = pedidoRep.getById(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext contex, UIComponent componente, Object value) {
		if(value!=null){
			Pedido produto = (Pedido) value;
			return produto.getId() == null ? null : produto.getId().toString();
		}
		return "";
	}

}
