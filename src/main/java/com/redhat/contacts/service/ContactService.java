package com.redhat.contacts.service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.redhat.contacts.model.Contact;

/**
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resource(name = "contact-service")
public class ContactService {

	@Inject
	private EntityManager em;

	@Resource
	SessionContext sctx;

	@Inject
	Logger logger;

	public Contact create(Contact entity) throws Exception {
		em.persist(entity);
		return entity;
	}

	public void deleteById(Long id) {
		Contact entity = em.find(Contact.class, id);
		em.remove(entity);
	}

	public Contact findById(Long id) {
		TypedQuery<Contact> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Contact c WHERE c.id = :entityId ORDER BY c.id",
						Contact.class);
		findByIdQuery.setParameter("entityId", id);
		Contact ret;
		try {
			ret = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			ret = null;
		}
		return ret;
	}

	public List<Contact> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Contact> findAllQuery = em
				.createQuery("SELECT DISTINCT c FROM Contact c ORDER BY c.id",
						Contact.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public Contact update(Long id, Contact entity) {

		if (em.find(Contact.class, id) == null) {
			return null;
		}

		return em.merge(entity);
	}

	public List<Contact> searchWildcard(String query) throws Exception {

		QueryBuilder qb = ((FullTextEntityManager) em).getSearchFactory()
				.buildQueryBuilder().forEntity(Contact.class).get();

		Query hql = qb.keyword().wildcard()
				.onFields("nom", "prenom", "email", "ville").matching(query)
				.createQuery();

		return internalSearch(hql);
	}

	public List<Contact> searchQuery(String query) throws Exception {

		QueryBuilder qb = ((FullTextEntityManager) em).getSearchFactory()
				.buildQueryBuilder().forEntity(Contact.class).get();

		Query hql = qb.phrase().onField("nom").sentence(query).createQuery();

		return internalSearch(hql);
	}

	public List<Contact> searchLucene(String query) throws Exception {
		Query luceneQuery=null;

		QueryParser parser = new QueryParser("nom",
				((FullTextEntityManager) em).getSearchFactory().getAnalyzer(
						Contact.class));

		parser.setDefaultOperator(org.apache.lucene.queryparser.classic.QueryParser.Operator.AND);

		try {
			luceneQuery = parser.parse(query);
		} catch (Exception e) {
			return Collections.<Contact>emptyList();
		}

		return internalSearch(luceneQuery);
	}

	private List<Contact> internalSearch(Query query) {
		javax.persistence.Query persistenceQuery = ((FullTextEntityManager) em)
				.createFullTextQuery(query, Contact.class);

		List<Contact> ret = (List<Contact>) persistenceQuery.getResultList();

		logger.log(Level.INFO, "+++++ Found ''{0}'' for ''{1}''", new Object[] {
				ret.size(), query });

		return ret;
	}

	@PostConstruct
	public void postConstruct() {
		try {
			((FullTextEntityManager) em).createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
