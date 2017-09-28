package org.shaolin.bmdp.persistence;

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
	
	// we used sessionFactoryTL to improve the 'CurrentSession' behaviours.
	private static final ThreadLocal<Session> sessionFactoryTL = new ThreadLocal<Session>();
	// the user transaction is a singleton object in bitronix. so we only access once at here.
	private static UserTransaction userTansaction = getUserTransaction();
	
	private static final HashMap<String, AtomicInteger> threadCounters = new HashMap<String, AtomicInteger>();
	
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
		return getSession();
	}
	
	/**
	 * Get DB session with user transaction.
	 * 
	 * @return
	 */
	@Transactional
	public static Session getSession() {
		if (!threadCounters.containsKey(Thread.currentThread().getName())) {
			threadCounters.put(Thread.currentThread().getName(), new AtomicInteger());
		}
		threadCounters.get(Thread.currentThread().getName()).incrementAndGet();
		
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
		}
		try {
			userTansaction.begin();
		} catch (Exception e) {
			printThreadCounter();
			throw new RuntimeException(e);
		}
		SessionFactory sessionFactory = IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		Session session = sessionFactory.getCurrentSession(); 
		sessionFactoryTL.set(session);
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
		Session session = sessionFactoryTL.get();
		if (logger.isDebugEnabled()) {
			logger.debug("Transaction: isCommit-{}, user transaction-{}", 
					new Object[] { isCommit, userTansaction.toString()});
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
