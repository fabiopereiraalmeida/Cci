package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.service.CadastroPessoaService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroPessoaService cadastroPessoaService;
	
	private Seguranca seguranca = new Seguranca();
	
	private Pessoa pessoa;
		
	public CadastroPessoaBean() {
		limpar();
	}
	
	public void inicializar() {
		if (this.pessoa == null) {
			limpar();
		}
		
	}
	
	private void limpar() {
		pessoa = new Pessoa();
		
	}
	
	public void salvar() {
		
		pessoa.setDataCadastro(dataAtual());
		pessoa.setBloqueado(false);
		try {
			pessoa.setEmpresa(seguranca.getUsuarioLogado().getUsuario().getEmpresa());
		} catch (Exception e) {
			
		}		
		pessoa.setContagem(1.0);
		
		try {
			this.pessoa = cadastroPessoaService.salvar(this.pessoa);
			limpar();
			
			FacesUtil.addInfoMessage("Pessoa salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		
	}
		
	public boolean isEditando() {
		return this.pessoa.getId() != null;
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
	

}