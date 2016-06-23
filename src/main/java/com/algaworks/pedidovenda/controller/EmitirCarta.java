package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.security.Seguranca;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class EmitirCarta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletResponse response;
	
	@Inject
	private EntityManager manager;
	
	private Negativada negativadaSelecionado;
	private Pessoa pessoa;
	
	//private boolean relatorioGerado;
	
	private Seguranca seguranca = new Seguranca();
	
	public void emitir() {
		
			pessoa = negativadaSelecionado.getPessoa();
			
			FacesUtil.addInfoMessage("O nome da pessoa é " + pessoa.getNome() + " com o ID: " + pessoa.getId());

			Map<String, Object> parametros = new HashMap<>();
			
			String nomeEmpresa = "";
			
			try {
				nomeEmpresa = seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia();
			} catch (Exception e) {
				nomeEmpresa = "";
			}
			
			parametros.put("NOME_EMPRESA", nomeEmpresa);
			parametros.put("ID_PESSOA", pessoa.getId().toString());
			
			//ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/comunicado.jasper", this.response, parametros, "Comunicado.pdf");
			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/carta1.jasper", this.response, parametros, "Carta.pdf");
			//ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatoio_todas_pessoas.jasper", this.response, parametros, "Comunicado.pdf");
			
			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();

				FacesUtil.addInfoMessage("A Carta de " + pessoa.getNome() + " foi gerada com sucesso!!.");

			} else {
				FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
			}	
/*
			String nome = "";
			String endereco = "";
			String numero = "";
			String cidade = "";
			String CEP = "";
			String nomeEmpresa = "";

			if (pessoa.getNome() != null) {
				nome = pessoa.getNome();
			} else if (pessoa.getApelido() != null) {
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
			
			try {
				nomeEmpresa = seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia();
			} catch (Exception e) {
				nomeEmpresa = "";negativadas
			}

			parametros.put("NOME_PESSOA", nome);
			parametros.put("ENDERECO", endereco + " Nº " + numero);
			parametros.put("CIDADE", cidade);
			parametros.put("CEP", CEP);
			parametros.put("NOME_EMPRESA", nomeEmpresa);
*/
			
			
			//############################################################3
						
			/*
			
			try {
				JREmptyDataSource ds = new JREmptyDataSource();
				
				InputStream relatorioStream = this.getClass().getResourceAsStream("/relatorios/comunicado.jasper");
				
				JasperPrint print = null;
				print = JasperFillManager.fillReport(relatorioStream, parametros, ds);
				
				Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, 
		    	OutputStreamExporterOutput> exportador = new JRPdfExporter();
			exportador.setExporterInput(new SimpleExporterInput(print));
			
				exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
			
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"" 
					+ "Comunicado.pdf"  + "\"");
			
				exportador.exportReport();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			*/
		
	}
	
	public void emitir3(){
				
		pessoa = negativadaSelecionado.getPessoa();
		
		FacesUtil.addErrorMessage("O nome da pessoa é " + pessoa.getNome());
		
		Map<String, Object> parametros = new HashMap<>();
		
		String nome = "";
		String endereco = "";
		String numero = "";
		String cidade = "";
		String CEP = "";
		String nomeEmpresa = "";

		/*
		if (pessoa.getNome() != null) {
			nome = pessoa.getNome();
		} else if (pessoa.getApelido() != null) {
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
		
		try {
			nomeEmpresa = seguranca.getUsuarioLogado().getUsuario().getEmpresa().getFantasia();
		} catch (Exception e) {
			nomeEmpresa = "";
		}
*/
		
		/*
		
		parametros.put("NOME_PESSOA", nome);
		parametros.put("ENDERECO", endereco + " Nº " + numero);
		parametros.put("CIDADE", cidade);
		parametros.put("CEP", CEP);
		parametros.put("NOME_EMPRESA", nomeEmpresa);
				
		
		
		
//		try {
			Connection conn = com.algaworks.pedidovenda.util.ConectaBanco.getConnection();
			JRDataSource jrds = new JRBeanCollectionDataSource(null);
		
			InputStream relatorioStream = this.getClass().getResourceAsStream("/relatorios/comunicado.jasper");
			
			try {
			JasperPrint print = null;
				print = JasperFillManager.fillReport(relatorioStream, parametros, jrds);
			
			relatorioGerado = print.getPages().size() > 0;
			
			FacesUtil.addErrorMessage("Numero de paginas " + print.getPages().size());
			
			
			
			if (relatorioGerado) {
				Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, 
			    	OutputStreamExporterOutput> exportador = new JRPdfExporter();
				exportador.setExporterInput(new SimpleExporterInput(print));
				
				exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(this.response.getOutputStream()));
				
				
				this.response.setContentType("application/pdf");
				this.response.setHeader("Content-Disposition", "attachment; filename=\"" 
						+ "comunicado.pdf" + "\"");
				
					exportador.exportReport();
				
			}
					
		} catch (Exception e) {
			FacesUtil.addErrorMessage("ERRO! " + e.getMessage());
		}
	}
	
	public Negativada getNegativadaSelecionado() {
		return negativadaSelecionado;
	}

	public void setNegativadaSelecionado(Negativada negativadaSelecionado) {
		this.negativadaSelecionado = negativadaSelecionado;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	*/
/*	
	public void emitir4() throws IOException, JRException{
		
		Connection conn = com.algaworks.pedidovenda.util.ConectaBanco.getConnection();
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletOutputStream responseStream = response.getOutputStream();
		
		Map<String, Object> parametros = new HashMap<>();
		
		InputStream caminho = getClass().getResourceAsStream("/relatorios/comunicado.jasper");
		
		JasperPrint print = null;
		print = JasperFillManager.fillReport(caminho, parametros, conn);
		
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment; filename=\"relatorio.pdf\"");
		JasperExportManager.exportReportToPdfStream(print,responseStream);
		responseStream.flush();
		responseStream.close();
		context.renderResponse();
		context.responseComplete();
		JasperViewer.viewReport(print, false);
	}
	*/
	}
	
	public Negativada getNegativadaSelecionado() {
		return negativadaSelecionado;
	}

	public void setNegativadaSelecionado(Negativada negativadaSelecionado) {
		this.negativadaSelecionado = negativadaSelecionado;
	}
	
}
