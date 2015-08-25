package com.pedidovenda.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.pedidovenda.model.Usuario;
import com.pedidovenda.repositorios.PedidoRepositorio;
import com.pedidovenda.security.Seguranca;
import com.pedidovenda.security.UsuarioLogado;
import com.pedidovenda.security.UsuarioSistema;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static DateFormat DATE_FORMAT = new SimpleDateFormat();
	
	@Inject
	private PedidoRepositorio pedidoRep;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	// Modelo de dados para gráficos em plano cartesiano
	private LineChartModel model;

	public void preRender() {
		this.model = new LineChartModel();
		this.model.setAnimate(true);
		this.model.setLegendPosition("e");

		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setMin(0);

		adicionarSerie("Todos os pedidos",null);
		adicionarSerie("Meus pedidos",usuarioLogado.getUsuario());
	}

	private void adicionarSerie(String rotulo, Usuario criadoPor) {
		Map<Date,BigDecimal > valoresPorData = this.pedidoRep
				.valoresTotaisPorData(15, usuarioLogado.getUsuario());
		
		ChartSeries series = new ChartSeries(rotulo);
		
		for(Date data : valoresPorData.keySet()){
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}
		
		this.model.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}
}
