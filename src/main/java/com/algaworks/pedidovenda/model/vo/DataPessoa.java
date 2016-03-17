package com.algaworks.pedidovenda.model.vo;

import java.io.Serializable;
import java.util.Date;

public class DataPessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	//private Double r = 0.0;

	private Date data;
	private Double contagem;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getContagem() {
		return contagem;
	}

	public void setContagem(Double contagem) {
		this.contagem = contagem;
	}

	
	
}
