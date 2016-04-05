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

import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class CartaComunicadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	//private Negativada negativadaSelecionado;
	//private  Pessoa pessoa;
	
	//private Date dataInicio;
	//private Date dataFim;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	
	private Seguranca seguranca = new Seguranca();

	//public  void emitir(Pessoa p) {
	public void emitir() {
		
		//FacesUtil.addInfoMessage("A Carta de " + pessoa.getNome() 
		//+ " foi gerada com sucesso!!!.");
		
		//Pessoa pessoa = pessoas.porId(p.getId());
		
		Map<String, Object> parametros = new HashMap<>();
		
		String nome = "";
		String endereco = "";
		String numero = "";
		String cidade = "";
		String CEP = "";
		/*
		if (p.getNome() != null) {
			nome = p.getNome();
		}else if (p.getApelido() != null) {
			nome = p.getApelido();
		}
		*/
		parametros.put("NOME_PESSOA", nome);
		//parametros.put("NOME_PESSOA", "PESSOA");
		/*
		if (p.getEndereco() != null) {
			endereco = p.getEndereco();
		}
		
		if (p.getEnderecoNumero() != null) {
			numero = p.getEnderecoNumero();
		}
		*/
		parametros.put("ENDERECO", endereco + " Nº " + numero);
		//parametros.put("ENDERECO", "ENDERECO Nº 00");
		/*
		if (p.getCidade() != null) {
			cidade = p.getCidade();
		}
		*/
		parametros.put("CIDADE", cidade);
		//parametros.put("CIDADE", "CIDADE");
		/*
		if (p.getCep() != null) {
			CEP = p.getCep();
		}
		*/
		
		parametros.put("CEP", CEP);
		//parametros.put("CEP", "XXXX");
		
		//parametros.put("NOME_EMPRESA", seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia());
		parametros.put("NOME_EMPRESA", "EMPRESA TESTE");
		/*
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/comunicado.jasper",
				this.response, parametros, "Comunicado.pdf");

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
			*/
		
		
		//Map<String, Object> parametros = new HashMap<>();
		//parametros.put("ID_EMPRESA", seguranca.getUsuarioLogado().getUsuario().getEmpresa().getId());
		//parametros.put("data_fim", this.dataFim);
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/comunicado.jasper",
				response, parametros, "Comunicado.pdf");
		
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
/*
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
*/
	
	
}