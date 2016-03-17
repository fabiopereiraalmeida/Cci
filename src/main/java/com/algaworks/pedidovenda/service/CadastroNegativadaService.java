package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.repository.Negativadas;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroNegativadaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Negativadas negativadas;
	
	@Transactional
	public Negativada salvar(Negativada negativada) throws NegocioException {
		//Negativada negativadaExistente = negativadas.porPessoa(negativada.getPessoa());

		//RESPONSAVEL POR VERIFICAR A DUPLICIDADE DE USUARIOS
/*		
		if (negativadaExistente != null && !negativadaExistente.equals(negativada)) {
			throw new NegocioException("Já existe um negativada com o PESSOA informado.");
		}
		*/
		
		return negativadas.guardar(negativada);
		
	}
	
}
