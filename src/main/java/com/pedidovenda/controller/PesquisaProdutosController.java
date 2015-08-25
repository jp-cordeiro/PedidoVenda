package com.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.ProdutoRepositorio;
import com.pedidovenda.repositorios.filter.ProdutoFilter;
import com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProdutosController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepositorio produtoRep;

	private ProdutoFilter filtro;
	private List<Produto> produtosFiltrados;
	
	private Produto produtoSelecionado;
	
	public PesquisaProdutosController() {
		filtro = new ProdutoFilter();
//		produtoSelecionado = new Produto();
	}

	public void pesquisar() {

		produtosFiltrados = produtoRep.filtrados(filtro);
	}
	
//	public void teste(){
////		produtoSelecionado = new Produto();
////		produtoSelecionado.setNome("Teste");
//		System.out.println(produtoSelecionado.getNome());
//	}
	
	public Produto selecionar(){
		produtoSelecionado = produtoRep.getById(produtoSelecionado.getId());
		return produtoSelecionado;
	}

	public void excluir() {
		produtoRep.remove(produtoSelecionado);
		produtosFiltrados.remove(produtoSelecionado);

		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getSku()
				+ " excluído com sucesso.");
	}

	// Getters and Setters
	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

}
