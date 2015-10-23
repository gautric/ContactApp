package com.redhat.contacts;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.search.jpa.FullTextEntityManager;

public class Resources {

	// -------------------------------------------------------------------------------||
	// Required resources
	// ------------------------------------------------------------||
	// -------------------------------------------------------------------------------||

	@PersistenceUnit
	private EntityManagerFactory emf;

	private Logger logger = Logger.getLogger(Resources.class.getName());

	@Produces
	public Logger produceLogger(InjectionPoint ip) {
		return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
	}

	@Produces
	@RequestScoped
	public FullTextEntityManager produceFullTextEntityManager() {
		logger.info("Producing Entity Manager");
		return org.hibernate.search.jpa.Search.getFullTextEntityManager(emf.createEntityManager());
	}

	public void destroyEntityManager(@Disposes EntityManager em) {
		logger.info("Disposing Entity Manager");
		if (em.isOpen()) {
			em.close();
		}
	}
}
