package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Empresa;
import com.algaworks.pedidovenda.repository.filter.EmpresaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Empresa guardar(Empresa empresa) {
		return manager.merge(empresa);
	}
	
	@Transactional
	public void remover(Empresa empresa) throws NegocioException {
		try {
			empresa = porId(empresa.getId());
			manager.remove(empresa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Empresa não pode ser excluído.");
		}
	}

	public Empresa porCnpj(String cnpj) {
		try {
			return manager.createQuery("from Empresa where cnpj = :cnpj", Empresa.class)
				.setParameter("cnpj", cnpj.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Empresa> empresas() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.manager.createQuery("from Empresa", Empresa.class)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Empresa> filtrados(EmpresaFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Empresa.class);
				
		if (StringUtils.isNotBlank(filtro.getRazaoSocial())) {
			criteria.add(Restrictions.ilike("cnpj", filtro.getCnpj(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(filtro.getRazaoSocial())) {
			criteria.add(Restrictions.ilike("razaoSocial", filtro.getRazaoSocial(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(filtro.getFantasia())) {
			criteria.add(Restrictions.ilike("fantasia", filtro.getFantasia(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(filtro.getCidade())) {
			criteria.add(Restrictions.ilike("cidade", filtro.getCidade(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("razaoSocial")).list();
	}

	public Empresa porId(Long id) {
		return manager.find(Empresa.class, id);
	}

	public List<Empresa> porRazaoSocial(String razaoSocial) {
		return this.manager.createQuery("from Empresa where razaoSocial like :razaoSocial", Empresa.class)
				.setParameter("razaoSocial", razaoSocial.toUpperCase() + "%").getResultList();
	}
	
	public List<Empresa> porFantasia(String fantasia) {
		return this.manager.createQuery("from Empresa where fantasia like :fantasia", Empresa.class)
				.setParameter("fantasia", fantasia.toUpperCase() + "%").getResultList();
	}
	
	public List<Empresa> porCidade(String cidade) {
		return this.manager.createQuery("from Empresa where cidade like :cidade", Empresa.class)
				.setParameter("cidade", cidade.toUpperCase() + "%").getResultList();
	}
	
}
