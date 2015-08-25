package com.pedidovenda.controller;

import com.pedidovenda.model.Pedido;

public class PedidoAlteradoEvent {

	//Classe que encapsula dentro de um evento um pedido.
	
	private Pedido pedido;
	
	public PedidoAlteradoEvent(Pedido pedido) {
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}
}
