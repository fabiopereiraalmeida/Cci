package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.repository.Negativadas;
import com.algaworks.pedidovenda.repository.Pessoas;
import com.algaworks.pedidovenda.repository.filter.NegativadaFilter;
import com.algaworks.pedidovenda.repository.filter.PessoaFilter;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

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
	
	private Pessoa pessoa;
	//private CartaComunicadoBean cartaComunicadoBean = new CartaComunicadoBean();
	
	
	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
		
	
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
	
	public void emitirCarta() {
		
		pessoa = negativadaSelecionado.getPessoa();
				
		//FacesUtil.addInfoMessage("A Carta de " + negativadaSelecionado.getPessoa().getNome() 
		//		+ " foi gerada com sucesso.");
		
		//FacesUtil.addInfoMessage("A Carta de " + pessoa.getNome() 
		//		+ " foi gerada com sucesso!!.");
				
		//CartaComunicadoBean cartaComunicadoBean = new CartaComunicadoBean();
		//cartaComunicadoBean.setPessoa(pessoa);	
		//cartaComunicadoBean.emitir(pessoa);			
		
		//cartaComunicadoBean.emitir();
		
		
		Map<String, Object> parametros = new HashMap<>();
		
		String nome = "";
		String endereco = "";
		String numero = "";
		String cidade = "";
		String CEP = "";
		
		if (pessoa.getNome() != null) {
			nome = pessoa.getNome();
		}else if (pessoa.getApelido() != null) {
			nome = pessoa.getApelido();
		}
		
		if (pessoa.getEndereco() != null) {
			endereco = pessoa.getEndereco();
		}
		
		if (pessoa.getEnderecoNumero() != null) {
			numero = pessoa.getEnderecoNumero();
		}
		
		if (pessoa.getCidade() != null) {
			cidade = pessoa.getCidade();
		}
		
		if (pessoa.getCep() != null) {
			CEP = pessoa.getCep();
		}
		
		parametros.put("NOME_PESSOA", nome);
		parametros.put("ENDERECO", endereco + " Nº " + numero);
		parametros.put("CIDADE", cidade);
		parametros.put("CEP", CEP);
		parametros.put("NOME_EMPRESA", "EMPRESA TESTE");
		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/comunicado.jasper",
				response, parametros, "Comunicado.pdf");
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
			
			FacesUtil.addInfoMessage("A Carta de " + pessoa.getNome() 
			+ " foi gerada com sucesso!!.");
			
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
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