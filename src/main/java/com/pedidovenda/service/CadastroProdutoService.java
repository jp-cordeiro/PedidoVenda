package com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.ProdutoRepositorio;
import com.pedidovenda.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepositorio produtoRep;
	
	@Transactional
	public Produto salvar(Produto produto){
		Produto produtoExistente = produtoRep.getSku(produto.getSku());
		
		//Se o produtoexistente não for nulo e for diferente do produto em questão(edição)
		if(produtoExistente !=  null && !produtoExistente.equals(produto)){
			throw new NegocioException("Já exite um produto com o SKU informado.");
		}
		
		return produtoRep.Add(produto);
	}

}
