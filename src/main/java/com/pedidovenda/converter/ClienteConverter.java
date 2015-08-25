package com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.pedidovenda.model.Cliente;
import com.pedidovenda.repositorios.ClienteRepositorio;
import com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Cliente.class)
public class ClienteConverter implements Converter {

	private ClienteRepositorio repCliente;
	
	public ClienteConverter() {
		repCliente = (ClienteRepositorio) CDIServiceLocator.getBean(ClienteRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Cliente retorno = null;
		if(value!=null){
			retorno = (Cliente)repCliente.proId(new Long(value));
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value!=null){
			Cliente cliente = (Cliente)value;
			return cliente.getId() == null? null:cliente.getId().toString();
		}
		return "";
	}

}
