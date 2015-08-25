package com.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.pedidovenda.service.NegocioException;

@Table(name = "produto")
@Entity
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String sku;
	private BigDecimal valorUnitario;
	private Integer quantidadeEstoque;
	private Categoria categoria;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(max = 80)
	@Column(nullable = false, length = 80)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotBlank
	// @Pattern(regexp="",message="Deve conter número")
	@Column(nullable = false, length = 20, unique = true)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@NotNull
	@Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@NotNull
	@Min(0)
	@Max(9999)
	@Column(name = "quantidade_estoque", nullable = false, length = 5)
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void baixarEstoque(Integer quantidade) {
		// Atribui uma nova quantidade com base na antiga - quantidade do pedido
		int novaQuantidade = this.getQuantidadeEstoque() - quantidade;

		// Validade se é possível emitir o pedido, pois um pedido não pode ser
		// emitido
		// se a quantidade é menor q 0, ou seja o estoque não pode ser negativo.
		if (novaQuantidade < 0) {
			throw new NegocioException("Não há disponibilidade de "
					+ quantidade + " itens do produto " + this.getSku() + ".");
		}

		this.setQuantidadeEstoque(novaQuantidade);
	}

	public void adicionarEstoque(Integer quantidade) {
		this.setQuantidadeEstoque(getQuantidadeEstoque() + quantidade);
		
	}
}
