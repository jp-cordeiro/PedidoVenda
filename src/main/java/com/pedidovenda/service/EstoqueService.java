package com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.pedidovenda.model.ItemPedido;
import com.pedidovenda.model.Pedido;
import com.pedidovenda.repositorios.PedidoRepositorio;
import com.pedidovenda.util.jpa.Transactional;

public class EstoqueService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepositorio repPedido;
	
	@Transactional
	public void baixarItensEstoque(Pedido pedido){
		//Consulta para recuperar o pedido no BD.
		pedido = this.repPedido.getById(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}

	public void retornarItensEstoque(Pedido pedido) {
		pedido = this.repPedido.getById(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
		
	}
}
