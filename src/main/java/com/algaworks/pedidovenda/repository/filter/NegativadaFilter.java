package com.algaworks.pedidovenda.repository.filter;

import java.io.Serializable;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.model.Pessoa;

public class NegativadaFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	
	private Empresa empresa;
	private String nomeEmpresa;
	private Pessoa pessoa;
	private String nomePessoa;

	//@SKU
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}	

	public String getNomeEmpresa() {
		return nomeEmpresa;
		//return empresa.getFantasia();
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
		//this.empresa.setFantasia(nomeEmpresa);
	}
		
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
		//return pessoa.getNome();
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
		//this.pessoa.setNome(nomePessoa);
	}
	

}