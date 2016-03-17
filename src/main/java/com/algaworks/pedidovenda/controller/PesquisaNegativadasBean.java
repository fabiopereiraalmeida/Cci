package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.repository.Negativadas;
import com.algaworks.pedidovenda.repository.filter.NegativadaFilter;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaNegativadasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Negativadas negativadas;
		
	private NegativadaFilter filtro;
	private List<Negativada> negativadasFiltrados;
	
	private Negativada negativadaSelecionado;
	
	private Seguranca seguranca = new Seguranca();
	
	public PesquisaNegativadasBean() {
		filtro = new NegativadaFilter();
	}
		
	public void excluir() {
		try {
			negativadas.remover(negativadaSelecionado);
			negativadasFiltrados.remove(negativadaSelecionado);
			
			FacesUtil.addInfoMessage("Negativada " + negativadaSelecionado.getPessoa() 
					+ " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void baixa() {
	
		try {
			negativadas.salvarBaixa(negativadaSelecionado);
			FacesUtil.addInfoMessage("A negativada de " + negativadaSelecionado.getPessoa().getNome() 
					+ " foi dado baixa com sucesso.");
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
	}
	
	public void baixaDefinitiva() {
		
		try {
			negativadas.salvarBaixaDefinitiva(negativadaSelecionado);
			FacesUtil.addInfoMessage("A negativada de " + negativadaSelecionado.getPessoa().getNome() 
					+ " foi dado baixa com sucesso.");
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
	}
	
	public void pesquisar() {
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 001");
		filtro.setEmpresa(seguranca.getUsuarioLogado().getUsuario().getEmpresa());
		//filtro.setEmpresa(null);
		negativadasFiltrados = negativadas.filtrados(filtro);
	}
	
	public void pesquisarBaixados() {
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 001");
		filtro.setEmpresa(seguranca.getUsuarioLogado().getUsuario().getEmpresa());
		//filtro.setEmpresa(null);
		negativadasFiltrados = negativadas.filtradosBaixados(filtro);
	}
	
	public void pesquisarSemEmpresa() {
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 001");
		//filtro.setEmpresa(seguranca.getUsuarioLogado().getUsuario().getEmpresa());
		filtro.setEmpresa(null);
		negativadasFiltrados = negativadas.filtrados(filtro);
	}
	
	public List<Negativada> getNegativadasFiltrados() {
		return negativadasFiltrados;
	}

	public NegativadaFilter getFiltro() {
		return filtro;
	}

	public Negativada getNegativadaSelecionado() {
		return negativadaSelecionado;
	}

	public void setNegativadaSelecionado(Negativada negativadaSelecionado) {
		this.negativadaSelecionado = negativadaSelecionado;
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
	
}