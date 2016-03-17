package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Empresas;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Inject
	private Empresas empresas;
	
	@Inject
	private Grupos grupos;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	
	private Usuario usuario;
	
	private List<Empresa> listEmpresas;
	private List<Grupo> listGrupos;
		
	public CadastroUsuarioBean() {
		limpar();
	}
	
	public void inicializar() {
		if (this.usuario == null) {
			limpar();
		}
		
		this.listEmpresas = this.empresas.empresas();
		this.listGrupos = this.grupos.grupos();
		
	}
	
	private void limpar() {
		usuario = new Usuario();
		
	}
	
	public void salvar() {
		try {
			this.usuario = cadastroUsuarioService.salvar(this.usuario);
			limpar();
			
			FacesUtil.addInfoMessage("Usuario salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		
	}
		
	public boolean isEditando() {
		return this.usuario.getId() != null;
	}
	
	public List<Empresa> getEmpresas() {
		return listEmpresas;
	}

	public List<Grupo> getGrupos() {
		return listGrupos;
	}
}