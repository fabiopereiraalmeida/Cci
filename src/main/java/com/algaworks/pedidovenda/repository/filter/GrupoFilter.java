package com.algaworks.pedidovenda.repository.filter;

import java.io.Serializable;

//import com.algaworks.pedidovenda.validation.SKU;

public class GrupoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;

	//@SKU
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome == null ? null : nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}