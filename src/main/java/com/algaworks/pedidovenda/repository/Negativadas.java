package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.algaworks.pedidovenda.model.Negativada;
import com.algaworks.pedidovenda.model.Pessoa;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.model.vo.DataNegativada;
import com.algaworks.pedidovenda.repository.filter.NegativadaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Negativadas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Negativada guardar(Negativada negativada) {
		
		return manager.merge(negativada);
	}
	
	@Transactional
	public void salvar(Negativada negativada) throws NegocioException {
		try {
			negativada = porId(negativada.getId());
			negativada.setAtivo(true);
			negativada.setPago(false);
			manager.merge(negativada);
			manager.flush();
			
			Pessoas pessoas = new Pessoas();
			
			pessoas.negativar(negativada.getPessoa());
/*			
			Pessoa pessoa = negativada.getPessoa();
			
			pessoa.setBloqueado(true);
			manager.merge(pessoa);
*/
		} catch (PersistenceException e) {
			throw new NegocioException("Negativada não pode ser salva.");
		}
	}
	
	@Transactional
	public void salvarBaixa(Negativada negativada) throws NegocioException {
		try {
			negativada = porId(negativada.getId());
			
			negativada.setBaixa(dataAtual());
			//negativada.setAtivo(false);
			negativada.setPago(true);
			
			manager.merge(negativada);
			manager.flush();
/*			
			if (existeNegativada(negativada.getPessoa().getId())) {
				
				Pessoa pessoa = negativada.getPessoa();
				
				pessoa.setBloqueado(true);
				manager.merge(pessoa);
				
			}else{
				Pessoa pessoa = negativada.getPessoa();
				
				pessoa.setBloqueado(false);
				manager.merge(pessoa);
				
			}
*/			
		} catch (PersistenceException e) {
			throw new NegocioException("Negativada não pode ser salva.");
		}
	}

	@Transactional
	public void salvarBaixaDefinitiva(Negativada negativada) throws NegocioException {
		try {
			negativada = porId(negativada.getId());
			
			//negativada.setBaixa(dataAtual());
			negativada.setAtivo(false);
			//negativada.setPago(true);
			
			manager.merge(negativada);
			manager.flush();
			
			if (existeNegativada(negativada.getPessoa().getId())) {
				
				Pessoa pessoa = negativada.getPessoa();
				
				pessoa.setBloqueado(true);
				manager.merge(pessoa);
				
			}else{
				Pessoa pessoa = negativada.getPessoa();
				
				pessoa.setBloqueado(false);
				manager.merge(pessoa);
				
			}
			
		} catch (PersistenceException e) {
			throw new NegocioException("Negativada não pode ser salva.");
		}
	}
	
	@Transactional
	public void remover(Negativada negativada) throws NegocioException {
		try {
			negativada = porId(negativada.getId());
			manager.remove(negativada);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Negativada não pode ser excluído.");
		}
	}

	public Negativada porPessoa(Pessoa pessoa) {
		try {
			return manager.createQuery("from Negativada where pessoa = :pessoa", Negativada.class)
				.setParameter("pessoa", pessoa)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Negativada> filtrados(NegativadaFilter filtro) {
		
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 002");
		
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Negativada.class);

		if (filtro.getEmpresa() != null) {
			criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));			
		}
		
		if (filtro.getPessoa() != null) {
			criteria.add(Restrictions.eq("pessoa", filtro.getPessoa()));
		}

		//JOptionPane.showMessageDialog(null, "PASSEI AKI 01");
		
		if (StringUtils.isNotBlank(filtro.getNomeEmpresa())) {
			criteria.add(Restrictions.ilike("negativada.empresa.fantasia", filtro.getEmpresa().getFantasia().toString(), MatchMode.ANYWHERE));
		}
		
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 02");
		
		if (StringUtils.isNotBlank(filtro.getNomePessoa())) {
			criteria.add(Restrictions.ilike("negativada.pessoa.nome", filtro.getPessoa().getNome().toString(), MatchMode.ANYWHERE));
		}
		
		criteria.add(Restrictions.eq("ativo", true));
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 03");
		
		//return criteria.addOrder(Order.asc("negativada.pessoa.nome")).list();
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Negativada> filtradosBaixados(NegativadaFilter filtro) {
		
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 002");
		
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Negativada.class);

		if (filtro.getEmpresa() != null) {
			criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));			
		}
		
		if (filtro.getPessoa() != null) {
			criteria.add(Restrictions.eq("pessoa", filtro.getPessoa()));
		}

		//JOptionPane.showMessageDialog(null, "PASSEI AKI 01");
		
		if (StringUtils.isNotBlank(filtro.getNomeEmpresa())) {
			criteria.add(Restrictions.ilike("negativada.empresa.fantasia", filtro.getEmpresa().getFantasia().toString(), MatchMode.ANYWHERE));
		}
		
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 02");
		
		if (StringUtils.isNotBlank(filtro.getNomePessoa())) {
			criteria.add(Restrictions.ilike("negativada.pessoa.nome", filtro.getPessoa().getNome().toString(), MatchMode.ANYWHERE));
		}
		
		criteria.add(Restrictions.eq("ativo", true));
		criteria.add(Restrictions.eq("pago", true));
		//JOptionPane.showMessageDialog(null, "PASSEI AKI 03");
		
		//return criteria.addOrder(Order.asc("negativada.pessoa.nome")).list();
		return criteria.list();
	}

	public Negativada porId(Long id) {
		return manager.find(Negativada.class, id);
	}

	public List<Negativada> porNomePessoa(String nome) {
		return this.manager.createQuery("from Negativada where pessoa_id (select id from Pessoa where upper(nome) like :nome)", Negativada.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}
	
	public List<Negativada> porNomeEmpresa(String nome) {
		return this.manager.createQuery("from Negativada where empresa_id (select id from Empresa where upper(fantasia) like :fantasia)", Negativada.class)
				.setParameter("fantasia", nome.toUpperCase() + "%").getResultList();
	}
	
	//#####################################################################################################
	
	@SuppressWarnings({ "unchecked" })
	public Map<Date, Double> valoresTotaisPorData(Integer numeroDeDias) {
		Session session = manager.unwrap(Session.class);
		
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, Double> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(Negativada.class);
		
		// select date(data_criacao) as data, sum(valor_total) as valor 
		// from pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor 
		// group by date(data_criacao)
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(data_negativada) as data", 
						"date(data_negativada)", new String[] { "data" }, 
						new Type[] { StandardBasicTypes.DATE } ))
				.add(Projections.sum("valor").as("valor"))
			)
			.add(Restrictions.ge("dataNegativada", dataInicial.getTime()));
				
		List<DataNegativada> valoresPorData = criteria
				.setResultTransformer(Transformers.aliasToBean(DataNegativada.class)).list();
		
		for (DataNegativada dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
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
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
	
	//#############################################################################
	
	public Boolean existeNegativada(Long id) {
		
		Boolean retorno = false;
		
		List<Negativada> listaNegativadas = this.manager.createQuery("from Negativada where ativo like '1' and pessoa_id like '" + id +"'").getResultList();
		
		if (listaNegativadas.isEmpty()) {
			retorno = false;
		}else{
			retorno = true;
		}
		return retorno;
	}
	
	
}
