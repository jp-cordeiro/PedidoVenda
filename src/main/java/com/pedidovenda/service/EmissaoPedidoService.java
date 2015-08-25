package com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.StatusPedido;
import com.pedidovenda.repositorios.PedidoRepositorio;
import com.pedidovenda.util.jpa.Transactional;

public class EmissaoPedidoService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	@Inject
	private PedidoRepositorio repPedido;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Transactional
	public Pedido emitir(Pedido pedido){
		pedido = this.cadastroPedidoService.salvar(pedido);
		
		if(pedido.isNaoEmissivel()){
			throw new NegocioException("Pedido não pode ser emitido com status "
		+pedido.getStatus().getDescricao()+".");
		}
		
		this.estoqueService.baixarItensEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		
		pedido = this.repPedido.add(pedido);
		
		return pedido;
	}
	
}
