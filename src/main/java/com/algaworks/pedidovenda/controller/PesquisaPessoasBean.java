package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.repository.Pessoas;
import com.algaworks.pedidovenda.repository.filter.PessoaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaPessoasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Pessoas pessoas;
	
	private PessoaFilter filtro;
	private List<Pessoa> pessoasFiltrados;
	
	private Pessoa pessoaSelecionado;
	
	public PesquisaPessoasBean() {
		filtro = new PessoaFilter();
	}
	
	public void excluir() {
		try {
			pessoas.remover(pessoaSelecionado);
			pessoasFiltrados.remove(pessoaSelecionado);
			
			FacesUtil.addInfoMessage("Pessoa " + pessoaSelecionado.getCpf() 
					+ " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		pessoasFiltrados = pessoas.filtrados(filtro);
	}
	
	public List<Pessoa> getPessoasFiltrados() {
		return pessoasFiltrados;
	}

	public PessoaFilter getFiltro() {
		return filtro;
	}

	public Pessoa getPessoaSelecionado() {
		return pessoaSelecionado;
	}

	public void setPessoaSelecionado(Pessoa pessoaSelecionado) {
		this.pessoaSelecionado = pessoaSelecionado;
	}
	
}