package com.pedidovenda.repositorios;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.Usuario;
import com.pedidovenda.model.vo.DataValor;
import com.pedidovenda.repositorios.filter.PedidoFilter;

@Named
public class PedidoRepositorio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Pedido add(Pedido pedido){
		return this.manager.merge(pedido);
	}
	
	public Pedido getById(Long id) {
		return this.manager.find(Pedido.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Date, BigDecimal> valoresTotaisPorData(
			Integer numeroDeDias, Usuario criadoPor) {

		Session session = manager.unwrap(Session.class);
		
		numeroDeDias -= 1;
		// Gera a data atual
		Calendar dataInical = Calendar.getInstance();
		// Gera a data inicial da consulta
		dataInical = DateUtils.truncate(dataInical, Calendar.DAY_OF_MONTH);
		// Tem data como parametro e subtrai 15 dela
		dataInical.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias,
				dataInical);

		Criteria criteria = session.createCriteria(Pedido.class);

		criteria.setProjection(
				Projections.projectionList()
						// Projeção do GroupBy
						.add(Projections.sqlGroupProjection(
								"date(data_criacao) as data",
								"date(data_criacao)", new String[] { "data" },
								new Type[] { StandardBasicTypes.DATE }))
						// Projeção da soma
						.add(Projections.sum("valorTotal").as("valor")))
		// Parametro dinâmico
				.add(Restrictions.ge("dataCriacao", dataInical.getTime()));

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}

		// Modo do hibernate converter o conjunto de dados trazidos do banco
		// para um objeto DataValor
		List<DataValor> valoresPorData = criteria.setResultTransformer(
				Transformers.aliasToBean(DataValor.class)).list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}
		
		return resultado;
	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInical) {
		// Clona a variável para que ela não seja subescrita na iteração
		dataInical = (Calendar) dataInical.clone();

		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			// Cria um mapa vazio com data e valores zerados
			// Para quando não houver pedidos com valores a linha descer para o
			// 0
			mapaInicial.put(dataInical.getTime(), BigDecimal.ZERO);
			// Adiciona um dia
			dataInical.add(Calendar.DAY_OF_MONTH, 1);
		}
		return mapaInicial;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro){
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Pedido.class)
		//fazemos uma associação (join) com cliente e nomeamos como "c";
		.createAlias("cliente", "c")
		//fazemos uma associação (join) com vendedor e nomeamos como "v";
		.createAlias("vendedor", "v");
		
		if(filtro.getNumeroDe() != null){
			//id deve ser maior ou igual (ge= greater or equals) a filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}
		
		if(filtro.getNumeroAte() != null){
			//id deve ser menor ou igual (le = lower or equals) a filtro.numeroAte
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}
		
		if(filtro.getDataCriacaoDe() != null){
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
		}
		
		if(filtro.getDataCriacaoAte() != null){
			criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
		}
		
		if(StringUtils.isNotBlank(filtro.getNomeCliente())){
			//acessamos o nome do cliente associado ao pedido pelo alias "c", criado anteriormente
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filtro.getNomeVendedor())){
			//acessamos o nome do vendedor associado ao pedido pelo alias "v",criado anteriormente
			criteria.add(Restrictions.ilike("v.login", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
		}
		
		if(filtro.getStatus() !=null && filtro.getStatus().length > 0){
			//adicionamos uma restrição "in", passando um array de constantes da enum StatusPedido
			criteria.add(Restrictions.in("status", filtro.getStatus()));
		}
		
		return criteria.addOrder(Order.asc("id")).list();
	}

}
