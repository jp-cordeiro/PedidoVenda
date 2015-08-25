package com.pedidovenda.model;

public enum FormaPagamento {

	DINHEIRO("Dinheiro"),
	CARTAO_CREDITO("Cartão de crédito"),
	CARTAO_DEBITO("Cartão de débito"),
	CHEQUE("Cheque"),
	BOLETO("Boleto"),
	DEPOSITO("Depósito bancário");
	
	private String descricao;
	
	private FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
