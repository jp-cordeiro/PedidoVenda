import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.pedidovenda.model.Pedido;
import com.pedidovenda.model.Usuario;
import com.pedidovenda.model.vo.DataValor;

public class TesteConsulta {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("pedidovendadb");
		EntityManager manager = factory.createEntityManager();
		Session session = manager.unwrap(Session.class);

		Usuario usuario = new Usuario();
		usuario.setId(2L);
		
		Map<Date, BigDecimal> valores = valoresTotaisPorData(15, usuario, session);

		for (Date data : valores.keySet()) {
			System.out.println(data + " = " + valores.get(data));
		}

		manager.close();
	}

	@SuppressWarnings("unchecked")
	public static Map<Date, BigDecimal> valoresTotaisPorData(
			Integer numeroDeDias, Usuario criadoPor, Session session) {

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

	private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
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
}
