package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.event.SelectEvent;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.repository.Negativadas;
import com.algaworks.pedidovenda.repository.Pessoas;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.service.CadastroNegativadaService;
import com.algaworks.pedidovenda.service.CadastroPessoaService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroNegativadaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Pessoas pessoas;

	@Inject
	private CadastroNegativadaService cadastroNegativadaService;

	@Inject
	private CadastroPessoaService cadastroPessoaService;

	private Negativada negativada;

	private Pessoa pessoa;

	private Seguranca seguranca = new Seguranca();

	public CadastroNegativadaBean() {
		limpar();
	}

	public void inicializar() {
		if (this.negativada == null) {
			limpar();
		}

	}

	public void pessoaSelecionada(SelectEvent event) {
		negativada.setPessoa((Pessoa) event.getObject());
		pessoa = negativada.getPessoa();
	}

	private void limpar() {
		negativada = new Negativada();

	}

	public void salvar() {

		negativada.setDataNegativada(dataAtual());
		negativada.setAtivo(true);
		negativada.setPago(false);
		negativada.setUsuario(seguranca.getUsuarioLogado().getUsuario());
		negativada.setEmpresa(seguranca.getUsuarioLogado().getUsuario().getEmpresa());
		
		//imprimirCarta(negativada.getPessoa());
		
		try {
			this.negativada = cadastroNegativadaService.salvar(this.negativada);
			limpar();

			// pessoas.negativar(negativada.getPessoa());

			FacesUtil.addInfoMessage("Negativada salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}

		pessoa.setBloqueado(true);

		try {
			this.pessoa = cadastroPessoaService.salvar(this.pessoa);
			limpar();

			FacesUtil.addInfoMessage("Pessoa salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Negativada getNegativada() {
		return negativada;
	}

	public void setNegativada(Negativada negativada) {
		this.negativada = negativada;

	}

	public boolean isEditando() {
		return this.negativada.getId() != null;
	}

	@NotBlank
	public String getNomePessoa() {
		return negativada.getPessoa() == null ? null : negativada.getPessoa().getNome();
	}

	public void setNomePessoa(String nome) {
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
/*
	private void imprimirCarta(Pessoa p) {

		System.out.println(p.getApelido());
		System.out.println(p.getApelido());
		System.out.println(p.getApelido());
		System.out.println(p.getApelido());
		System.out.println(p.getApelido());

		CartaComunicadoBean cartaComunicado = new CartaComunicadoBean();

		cartaComunicado.emitir(p);
	}
*/
}