package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.repository.filter.GrupoFilter;

public class Grupos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public Grupo guardar(Grupo grupo) {
		return manager.merge(grupo);
	}
	
	public List<Grupo> grupos() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.manager.createQuery("from Grupo", Grupo.class)
				.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Grupo> filtrados(GrupoFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Grupo.class);
				
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Grupo porId(Long id) {
		return manager.find(Grupo.class, id);
	}
	
	public List<Grupo> porNome(String nome) {
		return this.manager.createQuery("from Grupo where nome like :nome", Grupo.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}
	
	public List<Grupo> porDescricao(String descricao) {
		return this.manager.createQuery("from Grupo where descricao like :descricao", Grupo.class)
				.setParameter("descricao", descricao.toUpperCase() + "%").getResultList();
	}
	
}
