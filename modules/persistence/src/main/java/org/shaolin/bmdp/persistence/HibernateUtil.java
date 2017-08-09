package org.shaolin.bmdp.persistence;

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
	
	
	@Transactional
	public static Session getSession() {
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
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
	 * the session must be released manually with JTA support.
	 * 
	 * @param session
	 * @param isCommit
	 */
	public static void releaseSession(Session session, boolean isCommit) {
		if (sessionFactoryTL.get() != null) {
			 sessionFactoryTL.set(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		if (isCommit) {
			// JTASessionContext being used with JDBCTransactionFactory; auto-flush will not operate correctly with getCurrentSession()
			session.flush();
		}
		session.close();
	}
	
}
