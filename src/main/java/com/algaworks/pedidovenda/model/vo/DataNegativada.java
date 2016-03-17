package com.algaworks.pedidovenda.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DataNegativada implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date data;
	private Double valor;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

}
