package org.shaolin.bmdp.persistence.internal;

import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InitialPersistenceService implements ILifeCycleProvider {

	private static final Logger logger = LoggerFactory.getLogger(InitialPersistenceService.class);
	
	@Override
	public void configService() {
		
	}
	
	@Override
	public void startService() {
		// share the session object of the master node to all applications.
//		if (IServerServiceManager.INSTANCE.getMasterNodeName().equals(AppContext.get().getAppName())) {
//			IServerServiceManager.INSTANCE.setHibernateSessionFactory(
//					HibernateUtil.getSessionFactory());
//			IServerServiceManager.INSTANCE.setHibernateConfiguration(
//					HibernateUtil.getConfiguration());
//			logger.info("The session object of the master node is ready for sharing.");
//		}
//		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) 
//				IServerServiceManager.INSTANCE.getSpringContext()).getBeanFactory();
//		PersistentConfig pconfig = IServerServiceManager.INSTANCE.getService(PersistentConfig.class);
//		beanFactory.registerSingleton(javax.sql.DataSource.class.getCanonicalName(), dataSource(pconfig));
//		beanFactory.registerSingleton(LocalSessionFactoryBean.class.getCanonicalName(), sessionFactory(pconfig));
//		beanFactory.registerSingleton(HibernateTransactionManager.class.getCanonicalName(), transactionManager(pconfig));
	}
	
	@Override
	public boolean readyToStop() {
		return true;
	}

	@Override
	public void stopService() {
//		HibernateUtil.getSessionFactory().close();
		logger.info("shutdown hibernate connection.");
	}

	@Override
	public void reload() {
		
	}

	@Override
	public int getRunLevel() {
		return 0;
	}

}
