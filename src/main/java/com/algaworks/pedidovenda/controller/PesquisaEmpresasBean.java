package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.repository.Empresas;
import com.algaworks.pedidovenda.repository.filter.EmpresaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Empresas empresas;
	
	private EmpresaFilter filtro;
	private List<Empresa> empresasFiltrados;
	
	private Empresa empresaSelecionado;
	
	public PesquisaEmpresasBean() {
		filtro = new EmpresaFilter();
	}
	
	public void excluir() {
		try {
			empresas.remover(empresaSelecionado);
			empresasFiltrados.remove(empresaSelecionado);
			
			FacesUtil.addInfoMessage("Empresa " + empresaSelecionado.getRazaoSocial() 
					+ " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		empresasFiltrados = empresas.filtrados(filtro);
	}
	
	public List<Empresa> getEmpresasFiltrados() {
		return empresasFiltrados;
	}

	public EmpresaFilter getFiltro() {
		return filtro;
	}

	public Empresa getEmpresaSelecionado() {
		return empresaSelecionado;
	}

	public void setEmpresaSelecionado(Empresa empresaSelecionado) {
		this.empresaSelecionado = empresaSelecionado;
	}
	
}