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
import javax.swing.ImageIcon;

import org.hibernate.Session;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class CartaComunicado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	
	//############################################
	
	private Negativada negativadaSelecionado;
	private Pessoa pessoa;
	private Seguranca seguranca = new Seguranca();
		
	//############################################	
	
	public void emitir() {
		
		Map<String, Object> parametros = new HashMap<>();
		
		//########################################
		pessoa = negativadaSelecionado.getPessoa();		
		//FacesUtil.addInfoMessage("O nome da pessoa é " + pessoa.getNome() + " com o ID: " + pessoa.getId());
				
		//ImageIcon logo = new ImageIcon(getClass().getResource("/src/main/webapp/resources/images/logo_cci_cabecalho.png"));
		
		parametros.put("NOME_EMPRESA", seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia());
		parametros.put("ID_PESSOA", pessoa.getId().toString());
		//parametros.put("LOGO", logo.getImage());
		//########################################
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/carta1.jasper",
				this.response, parametros, "1ª Carta - " + pessoa.getNome().toUpperCase() + ".pdf");
				
		Session session = manager.unwrap(Session.class);
		//Session session = (Session) entitymanager.getDelegate(); 
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
			
			FacesUtil.addInfoMessage("A 1ª Carta de cobrança para " + pessoa.getNome() + " foi gerada com sucesso!");
			
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}		
	}
	
public void emitir2() {
		
		Map<String, Object> parametros = new HashMap<>();
		
		//########################################
		pessoa = negativadaSelecionado.getPessoa();		
		//FacesUtil.addInfoMessage("O nome da pessoa é " + pessoa.getNome() + " com o ID: " + pessoa.getId());
				
		//ImageIcon logo = new ImageIcon(getClass().getResource("/src/main/webapp/resources/images/logo_cci_cabecalho.png"));
		
		parametros.put("NOME_EMPRESA", seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia());
		parametros.put("ID_PESSOA", pessoa.getId().toString());
		//parametros.put("LOGO", logo.getImage());
		//########################################
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/carta2.jasper",
				this.response, parametros, "2ª Carta - " + pessoa.getNome().toUpperCase() + ".pdf");
				
		Session session = manager.unwrap(Session.class);
		//Session session = (Session) entitymanager.getDelegate(); 
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
			
			FacesUtil.addInfoMessage("A 2ª Carta de cobrança para " + pessoa.getNome() + " foi gerada com sucesso!");
			
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}		
	}
	
	public Negativada getNegativadaSelecionado() {
		return negativadaSelecionado;
	}

	public void setNegativadaSelecionado(Negativada negativadaSelecionado) {
		this.negativadaSelecionado = negativadaSelecionado;
	}


}