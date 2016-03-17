package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.model.vo.DataNegativada;
import com.algaworks.pedidovenda.model.vo.DataPessoa;
import com.algaworks.pedidovenda.model.vo.DataValor;
import com.algaworks.pedidovenda.repository.filter.PessoaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Pessoas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pessoa guardar(Pessoa pessoa) {
		return manager.merge(pessoa);
	}
	
	@Transactional
	public void remover(Pessoa pessoa) throws NegocioException {
		try {
			pessoa = porId(pessoa.getId());
			manager.remove(pessoa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Pessoa não pode ser excluído.");
		}
	}
	
	@Transactional
	public void negativar(Pessoa pessoa) throws NegocioException {
		try {
			pessoa = porId(pessoa.getId());
			pessoa.setBloqueado(true);
			manager.merge(pessoa);
			
		} catch (PersistenceException e) {
			throw new NegocioException("Pessoa não pode ser excluído.");
		}
	}

	public Pessoa porCpf(String cpf) {
		try {
			return manager.createQuery("from Pessoa where upper(cpf) = :cpf", Pessoa.class)
				.setParameter("cpf", cpf.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Pessoa> filtrados(PessoaFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Pessoa.class);
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			criteria.add(Restrictions.eq("cpf", filtro.getCpf()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(filtro.getApelido())) {
			criteria.add(Restrictions.ilike("apelido", filtro.getApelido(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(filtro.getCidade())) {
			criteria.add(Restrictions.ilike("cidade", filtro.getCidade(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Pessoa porId(Long id) {
		return manager.find(Pessoa.class, id);
	}

	public List<Pessoa> porNome(String nome) {
		return this.manager.createQuery("from Pessoa where upper(nome) like :nome", Pessoa.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}
	
	public List<Pessoa> porApelido(String apelido) {
		return this.manager.createQuery("from Pessoa where upper(apelido) like :apelido", Pessoa.class)
				.setParameter("apelido", apelido.toUpperCase() + "%").getResultList();
	}
	
	public List<Pessoa> porCidade(String cidade) {
		return this.manager.createQuery("from Pessoa where upper(cidade) like :cidade", Pessoa.class)
				.setParameter("cidade", cidade.toUpperCase() + "%").getResultList();
	}
	
	//####################################################################################
	//#####################################################################################################
	
		@SuppressWarnings({ "unchecked" })
		public Map<Date, Double> valoresTotaisPorData(Integer numeroDeDias) {
			Session session = manager.unwrap(Session.class);
			
			numeroDeDias -= 1;
			
			Calendar dataInicial = Calendar.getInstance();
			dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
			dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
			
			Map<Date, Double> resultado = criarMapaVazio(numeroDeDias, dataInicial);
			
			Criteria criteria = session.createCriteria(Pessoa.class);
			
			// select date(data_criacao) as data, sum(valor_total) as valor 
			// from pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor 
			// group by date(data_criacao)
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.sqlGroupProjection("date(data_cadastro) as data", 
							"date(data_cadastro)", new String[] { "data" }, 
							new Type[] { StandardBasicTypes.DATE } ))
					.add(Projections.sum("contagem").as("contagem"))
				)
				.add(Restrictions.ge("dataCadastro", dataInicial.getTime()));
					
			List<DataPessoa> valoresPorData; 
			
			valoresPorData = criteria
					.setResultTransformer(Transformers.aliasToBean(DataPessoa.class)).list();
						
			for (DataPessoa dataValor : valoresPorData) {
				resultado.put(dataValor.getData(), dataValor.getContagem());
			}
			
			return resultado;
		}

		private Map<Date, Double> criarMapaVazio(Integer numeroDeDias,
				Calendar dataInicial) {
			dataInicial = (Calendar) dataInicial.clone();
			Map<Date, Double> mapaInicial = new TreeMap<>();

			for (int i = 0; i <= numeroDeDias; i++) {
				mapaInicial.put(dataInicial.getTime(), 0.0);
				dataInicial.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			return mapaInicial;
		}
	
}
