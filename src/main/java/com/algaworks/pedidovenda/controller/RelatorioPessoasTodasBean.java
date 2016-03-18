package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import com.algaworks.pedidovenda.util.jpa.EntityManagerProducer;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class RelatorioPessoasTodasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	//private Date dataInicio;
	//private Date dataFim;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	
	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	
	//private Connection conn = com.algaworks.pedidovenda.util.ConectaBanco.getConnection();

	public void emitir() {
		//manager = entityManagerProducer.createEntityManager();
		
		Map<String, Object> parametros = new HashMap<>();
		//parametros.put("data_inicio", this.dataInicio);
		//parametros.put("data_fim", this.dataFim);
		//parametros.put("ID_EMPRESA", "1");		
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatoio_todas_pessoas.jasper",
				this.response, parametros, "TodasPessoas.pdf");
				
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
		
	}
/*
	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
*/
}