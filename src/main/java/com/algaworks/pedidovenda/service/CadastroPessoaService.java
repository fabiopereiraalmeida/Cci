package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.repository.Pessoas;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroPessoaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Pessoas pessoas;
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa) throws NegocioException {
		/*
		Pessoa pessoaExistente = pessoas.porCpf(pessoa.getCpf());
		
		if (pessoaExistente != null && !pessoaExistente.equals(pessoa)) {
			throw new NegocioException("Já existe uma pessoa com o CPF informado.");
		}
		*/
		return pessoas.guardar(pessoa);
	}
	
}
