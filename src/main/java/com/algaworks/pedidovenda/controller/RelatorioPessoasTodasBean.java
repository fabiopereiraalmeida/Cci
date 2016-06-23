package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class RelatorioPessoasTodasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
			
	
	
	public void emitir() {
		
		Map<String, Object> parametros = new HashMap<>();
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatoio_todas_pessoas.jasper",
				this.response, parametros, "TodasPessoas.pdf");
				
		Session session = manager.unwrap(Session.class);
		//Session session = (Session) entitymanager.getDelegate(); 
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}		
	}

	
}