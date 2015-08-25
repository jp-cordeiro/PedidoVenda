package com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pedidovenda.model.Categoria;
import com.pedidovenda.model.Produto;
import com.pedidovenda.repositorios.CategoriaRepositorio;
import com.pedidovenda.service.CadastroProdutoService;
import com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutosController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepositorio categoriaRep;

	@Inject
	private CadastroProdutoService cadProdutoService;

	private List<Integer> produtosFiltrados;
	private Produto produto;
	private Categoria categoriaPai;
	private List<Categoria> categoriasRaizes;
	private List<Categoria> subCategorias;

	public CadastroProdutosController() {
		limpar();
		// this.produtosFiltrados = new ArrayList<>();
		// for (int i = 0; i < 50; i++) {
		// produtosFiltrados.add(i);
		// }
	}

	public void inicializar() {
		if (FacesUtil.isNotPostBack()) {
			categoriasRaizes = categoriaRep.getAll();
			
			if(this.categoriaPai != null){
				carregarSubCategorias();
			}
		}
	}

	public void salvar() {
		produto = cadProdutoService.salvar(produto);
		limpar();

		FacesUtil.addInfoMessage("Produto salvo com sucesso.");
	}

	public boolean isEdit(){
		return this.produto.getId() != null;
	}
	
	private void limpar() {
		this.produto = new Produto();
		categoriaPai = null;
		categoriasRaizes = new ArrayList<>();
		subCategorias = new ArrayList<>();
	}

	public void carregarSubCategorias() {
		subCategorias = categoriaRep.getSubCategorias(categoriaPai);
	}

	// Getters and Setters
	public List<Integer> getProdutosFiltrados() {
		return this.produtosFiltrados;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		
		//Navega até a catogoriaPai da categoria do produto
		if(this.produto != null){
			this.categoriaPai = this.produto.getCategoria().getCategoriaPai();
		}
	}

	public List<Categoria> getCategoriasRaizes() {
		return categoriasRaizes;
	}

	@NotNull
	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	@NotNull
	public List<Categoria> getSubCategorias() {
		return subCategorias;
	}

}
