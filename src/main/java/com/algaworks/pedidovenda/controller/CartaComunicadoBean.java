package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
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

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.util.ConectaBanco;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class CartaComunicadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	//private Negativada negativadaSelecionado;
	private  Pessoa pessoa;
	
	//private Date dataInicio;
	//private Date dataFim;

	@Inject
	private  FacesContext facesContext;

	@Inject
	private  HttpServletResponse response;

	@Inject
	private  EntityManager manager;
	
	private  Seguranca seguranca = new Seguranca();

	public  void emitir() {
		
		//FacesUtil.addInfoMessage("A Carta de " + pessoa.getNome() 
		//+ " foi gerada com sucesso!!!.");
		
		Map<String, Object> parametros = new HashMap<>();
		
		String nome = "";
		String endereco = "";
		String numero = "";
		String cidade = "";
		String CEP = "";
		
		if (pessoa.getNome() != null) {
			nome = pessoa.getNome();
		}else if (pessoa.getApelido() != null) {
			nome = pessoa.getApelido();
		}
		
		parametros.put("NOME_PESSOA", nome);
		
		if (pessoa.getEndereco() != null) {
			endereco = pessoa.getEndereco();
		}
		
		if (pessoa.getEnderecoNumero() != null) {
			numero = pessoa.getEnderecoNumero();
		}
		
		parametros.put("ENDERECO", endereco + " Nº " + numero);
		
		if (pessoa.getCidade() != null) {
			cidade = pessoa.getCidade();
		}
		
		parametros.put("CIDADE", cidade);
		
		if (pessoa.getCep() != null) {
			CEP = pessoa.getCep();
		}
		parametros.put("CEP", CEP);
		
		parametros.put("NOME_EMPRESA", seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia());
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/comunicado.jasper",
				this.response, parametros, "Comunicado.pdf");
		
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	
	
}