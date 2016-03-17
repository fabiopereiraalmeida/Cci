package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.repository.Empresas;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroEmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;
	
	@Transactional
	public Empresa salvar(Empresa empresa) throws NegocioException {
		Empresa empresaExistente = empresas.porCnpj(empresa.getCnpj());
		
		if (empresaExistente != null && !empresaExistente.equals(empresa)) {
			throw new NegocioException("Já existe um empresa com o CNPJ informado.");
		}
		
		return empresas.guardar(empresa);
	}
	
}
