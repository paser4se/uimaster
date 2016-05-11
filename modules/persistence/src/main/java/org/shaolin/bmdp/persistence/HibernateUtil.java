package org.shaolin.bmdp.persistence;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	
	// thread control session factory by out side.
	private static final ThreadLocal<Session> sessionFactoryTL = new ThreadLocal<Session>();
	
	@SuppressWarnings("deprecation")
	private synchronized static SessionFactory buildSessionFactory() {
		try {
			Configuration config = new Configuration();
			List<String> loadedPaths = new ArrayList<String>();
			Enumeration<URL> urls1 = AppContext.get().getAppClassLoader().getResources("hbm/");
			while (urls1.hasMoreElements()) {
				URL url = urls1.nextElement();
				String strPath = url.toString();
				if (loadedPaths.contains(strPath)) {
					continue;
				}
				if (logger.isInfoEnabled()) {
					logger.info("loading hibernate mapping file folder: " + strPath);
				}
				loadedPaths.add(strPath);
				
				File path = new File(url.toURI());
				final String files[] = path.list();
				for (final String file : files) {
					config.addResource("hbm/" + file);
				}
			}
			config.configure();
			((AppServiceManagerImpl)AppContext.get()).setHibernateConfiguration(config);
			// Create the SessionFactory from hibernate.cfg.xml
			return config.buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Error occurrs while initializing the hibernate configuration!!!", ex);
			// Make sure you log the exception, as it might be swallowed
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Configuration getConfiguration() {
		return (Configuration)AppContext.get().getHibernateConfiguration();
	}
	
	public static Session getSession() {
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
		}
		
		/*
		 * getCurrentSession() creates the session and bound to the current thread. OpenSession() won't.
		 * getCurrentSession()'s session will be closed after either doing 'commit' or 'rollback'. OpenSession() won't.
		 */
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// hibernate transaction is thread bound or thread safe.
			// we must to promise the transaction being 'commit' or 'rollback' in the same thread where begins.
			if (session.getTransaction() == null || !session.getTransaction().isActive()) {
				session.beginTransaction();
			}
		} catch (TransactionException e) {
			logger.warn("Hibernate TransactionException error: " + e.getMessage());
			logger.warn("Hibernate Session Info: collections-{},entities-{},transaction-{}", 
					new Object[] { session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount(),
									session.getTransaction().getLocalStatus()});
			try {
				session.getTransaction().rollback();
			} catch (Exception e0) {}
			// break transaction before rebuild.
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
		}
		sessionFactoryTL.set(session);
		if (logger.isDebugEnabled()) {
			logger.debug("Start Hibernate Transaction: collections-{},entities-{}", 
					new Object[] { session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		return session;
	}

	
	public static void releaseSession(Session session, boolean isCommit) {
		if (sessionFactoryTL.get() != null) {
			 sessionFactoryTL.set(null);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		if (!session.isOpen() || session.getTransaction() == null 
				|| !session.getTransaction().isActive()) {
			return;
		}
		
		if (isCommit) {
			session.getTransaction().commit();
		} else {
			session.getTransaction().rollback();
		}
		// no need close session manually. it close automatically.
	}
	
	public static SessionFactory getSessionFactory() {
		if (AppContext.get().getHibernateSessionFactory() == null) {
			((AppServiceManagerImpl)AppContext.get()).setHibernateSessionFactory(buildSessionFactory());
		}
		return (SessionFactory)AppContext.get().getHibernateSessionFactory();
	}
	
}
