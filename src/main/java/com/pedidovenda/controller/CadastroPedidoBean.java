package com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.pedidovenda.model.Cliente;
import com.pedidovenda.model.EnderecoEntrega;
import com.pedidovenda.model.FormaPagamento;
import com.pedidovenda.model.ItemPedido;
import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.Produto;
import com.pedidovenda.model.Usuario;
import com.pedidovenda.repositorios.ClienteRepositorio;
import com.pedidovenda.repositorios.ProdutoRepositorio;
import com.pedidovenda.repositorios.UsuarioRepositorio;
import com.pedidovenda.service.CadastroPedidoService;
import com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Integer> itens;
	private List<Usuario> vendedores;
	
	//Através dessa anotação o bean CadastroPedido irá produzir um pedido que estará disponível
	//para ser injetado em quem utilizar a anotação PedidoEdição
	//com isso terá acesso ao pedido produzido.
	@Produces
	@PedidoEdicao
	private Pedido pedido;
	
	private EnderecoEntrega enderecoEntrega;

	@Inject
	private UsuarioRepositorio repUsuario;

	@Inject
	private ClienteRepositorio repCliente;

	@Inject
	private CadastroPedidoService cadastroPedidoService;

	@Inject
	private ProdutoRepositorio repProduto;

	// Produto que será carregado para editar no autocomplete
	private Produto produtoLinhaEditavel;

	// Atributo utilizado para pesquisa por SKU
	private String sku;

	public CadastroPedidoBean() {
		limpar();
	}

	public List<Cliente> completarCliente(String nome) {
		return repCliente.porNome(nome);
	}

	public boolean isEdit() {
		return this.pedido.getId() != null;
	}

	public void recalcularPedido() {
		this.pedido.recalcularValorTotal();
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public List<Integer> getItens() {
		return itens;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostBack()) {
			this.vendedores = this.repUsuario.vendedores();

			this.pedido.addItemVazio();

			this.recalcularPedido();
		}
	}

	public List<Produto> completarProduto(String nome) {
		return this.repProduto.porNome(nome);
	}

	public void carregarProdutoLinhaEditavel() {
		// Pega o primeiro item da linha editável
		ItemPedido item = this.pedido.getItens().get(0);

		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil
						.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel
						.getValorUnitario());

				// Adiciona um item vazio acima do produto inserido na linha
				// editável
				this.pedido.addItemVazio();
				// Limpa o objeto para ser adicionado um novo valor
				this.produtoLinhaEditavel = null;
				// Limpa o SKU
				this.sku = null;

				// Recalcula o valor do pedido
				this.pedido.recalcularValorTotal();
			}
		}
	}

	public void atualizarQuantidade(ItemPedido item, int linha) {
		// Se um item tiver quantida menor que 1 remova a linha
		if (item.getQuantidade() < 1) {
			// Se a linha for a primeira apague
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}

		this.pedido.recalcularValorTotal();
	}

	public boolean existeItemComProduto(Produto produto) {
		// Dado um produto como parametro,
		// verifica na lista de itens se existe algum item que tenha esse
		// produto
		boolean existeItem = false;

		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.repProduto.getSku(sku);
			this.carregarProdutoLinhaEditavel();
		}
	}

	public void teste() {
		for (Usuario u : vendedores) {
			System.out.println(u.getLogin());
		}
	}

	public void limpar() {
		pedido = new Pedido();
		vendedores = new ArrayList<>();
		enderecoEntrega = new EnderecoEntrega();
		pedido.setEnderecoEntrega(enderecoEntrega);
	}

	public void salvar() {
		this.pedido.removerItemVazio();
		// throw new NegocioException(
		// "Pedido não pode ser salvo pois ainda não foi implementado.");
		try{
		this.cadastroPedidoService.salvar(this.pedido);

		FacesUtil.addInfoMessage("Pedido salvo com sucesso.");
		}finally{
			this.pedido.addItemVazio();
		}
	}
	
	//O método obeserva a classe PedidoAlteradoEvent e quando a mesma for acionada
	//o método é ativado.
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event){
		this.pedido = event.getPedido();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public List<Usuario> getVendedores() {
		return this.vendedores;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
