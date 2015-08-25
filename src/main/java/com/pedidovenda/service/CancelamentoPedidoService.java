package com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.StatusPedido;
import com.pedidovenda.repositorios.PedidoRepositorio;
import com.pedidovenda.util.jpa.Transactional;

public class CancelamentoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepositorio repPedido;

	@Inject
	private EstoqueService estoqueService;

	@Transactional
	public Pedido cancelar(Pedido pedido) {
		pedido = this.repPedido.getById(pedido.getId());

		if (pedido.isNaoCancelavel()) {
			throw new NegocioException(
					"Pedido não pode ser cancelado no status "
							+ pedido.getStatus().getDescricao() + ".");
		}
		
		if(pedido.isEmitido()){
			this.estoqueService.retornarItensEstoque(pedido);
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		
		pedido = this.repPedido.add(pedido);
		
		return pedido;
	}

}
