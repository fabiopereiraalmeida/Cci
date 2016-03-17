package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.repository.Negativadas;

@FacesConverter(forClass = Negativada.class)
public class NegativadaConverter implements Converter {

	@Inject
	private Negativadas negativadas;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Negativada retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = negativadas.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Negativada negativada = (Negativada) value;
			return negativada.getId() == null ? null : negativada.getId().toString();
		}
		
		return "";
	}

}
