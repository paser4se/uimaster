package org.shaolin.bmdp.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * We use JTA Solution: Springboot + Hibernate + Bitronix.
 * 
 * @author wushaol
 * 
 */
public class HibernateUtil {

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	
	private static final HashMap<String, AtomicInteger> threadCounters = new HashMap<String, AtomicInteger>();
	private static final HashMap<String, SessionFactory> sessionFactories = new HashMap<String, SessionFactory>();
	public static final String DEFAULT_SESSION = "DEFAULT_SESSION";
	
	// we used sessionFactoryTL to improve the 'CurrentSession' behaviours.
	private static final ThreadLocal<HashMap<String, Session>> sessionFactoryTL = new ThreadLocal<HashMap<String, Session>>();
	// the user transaction is a singleton object in bitronix. so we only access once at here.
	private static UserTransaction userTansaction = getUserTransaction();
	
	static void addSessionFactory(String packageDomain, SessionFactory factory) {
		if (sessionFactories.containsKey(packageDomain)) {
			throw new IllegalArgumentException("This domain "+ packageDomain +" of session factory has already been existing!");
		}
		sessionFactories.put(packageDomain, factory);
	}
	
	/**
	 * Get user transaction directly.
	 * @return
	 */
	public static UserTransaction getUserTransaction() {
		if (userTansaction != null) {
			//TODO: check whether transaction is committed or not.
			return userTansaction;
		}
		try {
			Hashtable<String, String> prop = new Hashtable<String, String>();
			prop.put(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
			prop.put(Context.URL_PKG_PREFIXES, "bitronix.tm.jndi");
			Context context = new InitialContext(prop);
			userTansaction = (UserTransaction) context.lookup("java:comp/UserTransaction");
//			if (userTansaction instanceof BitronixTransactionManager) {
//			BitronixTransactionManager txManager = (BitronixTransactionManager)userTransactionTL.get();
//			txManager.getStatus();
//		}
			return userTansaction;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void releaseTransaction(boolean isCommit) {
		try {
			if (userTansaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					userTansaction.commit();
				} else {
					userTansaction.rollback();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
		}
	}

	/**
	 * Get the session of the read only database for boosting access performance.
	 * 
	 * @return
	 */
	public static Session getReadOnlySession() {
		return getSession(DEFAULT_SESSION);
	}
	
	public static Session getSession() {
		return getSession(DEFAULT_SESSION);
	}
	
	/**
	 * Get DB session with user transaction.
	 * 
	 * @return
	 */
	@Transactional
	public static Session getSession(String packageDomain) {
		if (!threadCounters.containsKey(Thread.currentThread().getName())) {
			threadCounters.put(Thread.currentThread().getName(), new AtomicInteger());
		}
		threadCounters.get(Thread.currentThread().getName()).incrementAndGet();
		
		if (sessionFactoryTL.get() != null && sessionFactoryTL.get().containsKey(packageDomain)) {
			return sessionFactoryTL.get().get(packageDomain);
		}
		if (sessionFactoryTL.get() == null) {
			try {
				userTansaction.begin();
			} catch (Exception e) {
				printThreadCounter();
				throw new RuntimeException(e);
			}
		}
		SessionFactory sessionFactory = (sessionFactories.containsKey(packageDomain)) ? 
				sessionFactories.get(packageDomain) : sessionFactories.get(DEFAULT_SESSION); 
		// IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		if (sessionFactory == null) {
			logger.warn(packageDomain + " does not exist in key sets: " + sessionFactories.keySet().toString());
		}
		Session session = sessionFactory.getCurrentSession(); 
		if (sessionFactoryTL.get() == null) {
			sessionFactoryTL.set(new HashMap<String, Session>());
		}
		sessionFactoryTL.get().put(packageDomain, session);
		if (logger.isDebugEnabled()) {
			logger.debug("Start Hibernate Transaction: collections-{},entities-{}", 
					new Object[] { session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		return session;
	}

	/**
	 * the session must be flushed with JTA support.
	 * 
	 * @param session
	 * @param isCommit
	 */
	public static void releaseSession(boolean isCommit) {
		if (sessionFactoryTL.get() == null) {
			try {
				if (userTansaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
					userTansaction.rollback();
				}
			} catch (Exception e) {
				printThreadCounter();
				throw new RuntimeException(e);
			} 
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Transaction: isCommit-{}, user transaction-{}", 
					new Object[] { isCommit, userTansaction.toString()});
		}
		try {
			if (userTansaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					Collection<Session> sessions = sessionFactoryTL.get().values();
					// JTASessionContext being used with JDBCTransactionFactory; 
					// auto-flush will not operate correctly with getCurrentSession()
					for (Session s: sessions) {
						s.flush();
					}
					userTansaction.commit();
				} else {
					userTansaction.rollback();
				}
			}
		} catch (Throwable e) {
			printThreadCounter();
			throw new RuntimeException(e);
		} finally {
			sessionFactoryTL.set(null);
			if (threadCounters.containsKey(Thread.currentThread().getName())) {
				threadCounters.get(Thread.currentThread().getName()).set(0);
			}
		}
	}
	
	/**
	 * Open session without transaction directly.
	 * 
	 * @return
	 */
	public static Session openSession() {
		SessionFactory sessionFactory = IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		return sessionFactory.openSession(); 
	}

	public static void releaseSession(Session session, boolean isCommit) {
		if (logger.isDebugEnabled()) {
			logger.debug("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		try {
			if (userTansaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					// JTASessionContext being used with JDBCTransactionFactory; 
					// auto-flush will not operate correctly with getCurrentSession()
					session.flush();
					userTansaction.commit();
				} else {
					userTansaction.rollback();
				}
			}
		} catch (Exception e) {
			printThreadCounter();
			throw new RuntimeException(e);
		} finally {
			sessionFactoryTL.set(null);
			if (threadCounters.containsKey(Thread.currentThread().getName())) {
				threadCounters.get(Thread.currentThread().getName()).set(0);
			}
		}
	}
	
	public static void printThreadCounter() {
		//bitronix.tm.BitronixTransaction
		logger.info("-------------Print Thread Counters-------------");
		Set<Entry<String, AtomicInteger>> entries = threadCounters.entrySet();
		for (Entry<String, AtomicInteger> entry: entries) {
			logger.info(entry.getKey() + "-- counts: " + entry.getValue().get());
		}
		logger.info("-------------Print Thread Counters End-------------");
	}
}
