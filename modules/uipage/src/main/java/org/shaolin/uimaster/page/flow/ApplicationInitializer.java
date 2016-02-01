package org.shaolin.uimaster.page.flow;

import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager.State;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationInitializer {

	private static Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);
	
	private final String appName;
	
	public ApplicationInitializer(String appName) {
		this.appName = appName;
	}
	
	public void start() {
		logger.info("=========Registering application instance " + appName + "...");
		IServerServiceManager serverManager = IServerServiceManager.INSTANCE;
		IAppServiceManager masterApp = serverManager.getApplication(serverManager.getMasterNodeName());
		AppServiceManagerImpl appServiceManager = new AppServiceManagerImpl(appName, this.getClass().getClassLoader(), masterApp);
		try {
			appServiceManager.setHibernateConfiguration(masterApp.getHibernateConfiguration());
			appServiceManager.setHibernateSessionFactory(masterApp.getHibernateSessionFactory());
			
			// add application to the server manager.
			serverManager.addApplication(appName, appServiceManager);
			
			appServiceManager.setState(State.ACTIVE);
        	logger.info("==========Application " + appName + " is ready for request.");
		} catch (Throwable e) {
			appServiceManager.setState(State.FAILURE);
			logger.error("Fails to start Config server start! Error: " + e.getMessage(), e);
		} finally {
		}
	}
	
	public void stop() {
		IServerServiceManager.INSTANCE.removeApplication(appName);
	}
	
	public void updateCache() {
		//TODO: re-generatie all UI relevant such js, css.
		//1. broadcast the update to all application node.
		//2. the application ask for updating.
	}
	
}
