package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.service.CadastroEmpresaService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Inject
	private CadastroEmpresaService cadastroEmpresaService;
	
	private Empresa empresa;
		
	public CadastroEmpresaBean() {
		limpar();
	}
	
	public void inicializar() {
		if (this.empresa == null) {
			limpar();
		}
		
	}
	
	private void limpar() {
		empresa = new Empresa();
		
	}
	
	public void salvar() {
		
		empresa.setDataCadastro(dataAtual());
		
		try {
			this.empresa = cadastroEmpresaService.salvar(this.empresa);
			limpar();
			
			FacesUtil.addInfoMessage("Empresa salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
		
	}
		
	public boolean isEditando() {
		return this.empresa.getId() != null;
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}

}