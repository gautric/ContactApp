package com.redhat.contacts.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;

public class Interceptor {

	@PersistenceContext
	EntityManager em;

	/**
	 * We'll use it for checking user's credentials.
	 */
	@Resource
	SessionContext sctx;

	@Resource
	TransactionSynchronizationRegistry tsk;

	@Inject
	Logger logger;

	@AroundInvoke
	Object mdbInterceptor(InvocationContext ctx) throws Exception {

		String userName = sctx.getCallerPrincipal().getName();
		String className = ctx.getTarget().getClass().getName();
		String methodName = ctx.getMethod().getName();

		logger.log(
				Level.INFO,
				"##### User ''{0}'' intercepted while calling ''{1}#{2}'' Tx {3}",
				new Object[] { userName, className, methodName,
						tsk.getTransactionKey() });

		Object ret = null;
		try {
			ret = ctx.proceed();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "catch ", e);
		}

		
		
		logger.log(
				Level.INFO,
				">>>>> User ''{0}'' intercepted while calling ''{1}#{2}'' Tx {3}",
				new Object[] { userName, className, methodName,
						tsk.getTransactionKey() });

		return ret;
	}
}
