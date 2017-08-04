package org.shaolin.bmdp.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public static Session getSession() {
		if (sessionFactoryTL.get() != null) {
			return sessionFactoryTL.get();
		}
		SessionFactory sessionFactory = IServerServiceManager.INSTANCE.getService(SessionFactory.class);
		Session session = sessionFactory.getCurrentSession();
		
		sessionFactoryTL.set(session);
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
		// no need close session manually. it close automatically.
	}
	
}
