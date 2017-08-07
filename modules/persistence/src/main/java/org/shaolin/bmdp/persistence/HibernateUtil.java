package org.shaolin.bmdp.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * We use Springboot + Hibernate solution.
 * 
 * @author wushaol
 * 
 */
public class HibernateUtil {

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	
	// thread control session factory by out side.
	private static final ThreadLocal<Session> sessionFactoryTL = new ThreadLocal<Session>();
	
	/**
	 * Get the session of the read only database for boosting access performance.
	 * 
	 * @return
	 */
	public static Session getReadOnlySession() {
		return getSession();
	}
	
	@Transactional
	public static Session getSession() {
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
		}
		SessionFactory sessionFactory = IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		// openSession is recommended in JTA mode instead of using sessionFactory.getCurrentSession();
		// we used sessionFactoryTL to simulate 'CurrentSession' behaviour.
		Session session = sessionFactory.openSession(); 
		sessionFactoryTL.set(session);
		if (logger.isInfoEnabled()) {
			logger.info("Start Hibernate Transaction: collections-{},entities-{}", 
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
		if (logger.isInfoEnabled()) {
			logger.info("End Hibernate Transaction: isCommit-{},collections-{},entities-{}", 
					new Object[] { isCommit, session.getStatistics().getCollectionCount(), 
									session.getStatistics().getEntityCount()});
		}
		if (isCommit) {
			session.flush();
		}
		session.close();
	}
	
}
