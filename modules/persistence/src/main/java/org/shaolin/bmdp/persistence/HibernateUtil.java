package org.shaolin.bmdp.persistence;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
	private static final ThreadLocal<UserTransaction> userTransactionTL = new ThreadLocal<UserTransaction>();
	
	private static UserTransaction getUserTransaction0() throws NamingException {
		Hashtable<String, String> prop = new Hashtable<String, String>();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
		prop.put(Context.URL_PKG_PREFIXES, "bitronix.tm.jndi");
		Context context = new InitialContext(prop);
		return (UserTransaction) context.lookup("java:comp/UserTransaction");
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
	 * Open session without transaction directly.
	 * 
	 * @return
	 */
	public static Session openSession() {
		SessionFactory sessionFactory = IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		return sessionFactory.openSession(); 
	}
	
	/**
	 * Get user transaction directly.
	 * @return
	 */
	public static UserTransaction getUserTransaction() {
		if (userTransactionTL.get() != null) {
			//TODO: check whether transaction is committed or not.
			return userTransactionTL.get();
		}
		try {
			UserTransaction tx = getUserTransaction0();
			tx.begin();
			userTransactionTL.set(tx);
			return tx;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get DB session with user transaction.
	 * 
	 * @return
	 */
	@Transactional
	public static Session getSession() {
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
		}
		getUserTransaction();
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
			if (userTransactionTL.get() != null) {
				try {
					userTransactionTL.get().rollback();
				} catch (Exception e) {
				}
				userTransactionTL.set(null);
			}
			return;
		}
		if (userTransactionTL.get() == null) {
			sessionFactoryTL.set(null);
			return;
		}
		Session session = sessionFactoryTL.get();
		if (logger.isDebugEnabled()) {
			logger.debug("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		try {
			if (userTransactionTL.get() != null 
					&& userTransactionTL.get().getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					// JTASessionContext being used with JDBCTransactionFactory; 
					// auto-flush will not operate correctly with getCurrentSession()
					session.flush();
					userTransactionTL.get().commit();
				} else {
					userTransactionTL.get().rollback();
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			sessionFactoryTL.set(null);
			userTransactionTL.set(null);
		}
	}
	
	public static void releaseSession(Session session, boolean isCommit) {
		if (logger.isDebugEnabled()) {
			logger.debug("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		try {
			if (userTransactionTL.get() != null 
					&& userTransactionTL.get().getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					// JTASessionContext being used with JDBCTransactionFactory; 
					// auto-flush will not operate correctly with getCurrentSession()
					session.flush();
					userTransactionTL.get().commit();
				} else {
					userTransactionTL.get().rollback();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sessionFactoryTL.set(null);
			userTransactionTL.set(null);
		}
	}
	
	public static void releaseTransaction(boolean isCommit) {
		if (userTransactionTL.get() == null) {
			return;
		}
		try {
//			if (userTransactionTL.get() instanceof BitronixTransactionManager) {
//				BitronixTransactionManager txManager = (BitronixTransactionManager)userTransactionTL.get();
//				txManager.getStatus();
//			}
			if (userTransactionTL.get().getStatus() != Status.STATUS_NO_TRANSACTION) {
				if (isCommit) {
					userTransactionTL.get().commit();
				} else {
					userTransactionTL.get().rollback();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			userTransactionTL.set(null);
		}
	}
	
}
