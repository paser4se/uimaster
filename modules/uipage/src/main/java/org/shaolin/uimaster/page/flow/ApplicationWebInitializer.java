package org.shaolin.uimaster.page.flow;

import java.util.List;

import javax.servlet.ServletContext;

import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.ce.ConstantServiceImpl;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager.State;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.StatementParser;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.statement.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationWebInitializer {

	private static Logger logger = LoggerFactory.getLogger(ApplicationWebInitializer.class);
	
	private final String appName;
	
	public ApplicationWebInitializer(String appName) {
		this.appName = appName;
	}
	
	public void start(ServletContext servletContext) {
		logger.info("Initializing application instance " + appName + "...");
		IServerServiceManager serverManager = IServerServiceManager.INSTANCE;
		if (!appName.equals(serverManager.getMasterNodeName())) {
			IAppServiceManager appManager = serverManager.getApplication(serverManager.getMasterNodeName());
			while (appManager.getState() == State.START) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			if (appManager.getState() == State.FAILURE) {
				logger.error("The state of the master node is failed, please check the master node!");
				return;
			}
		}
		
		AppServiceManagerImpl appServiceManager = new AppServiceManagerImpl(appName, servletContext.getClassLoader());
		try {
			AppContext.register(appServiceManager);
			// add application to the server manager.
			serverManager.addApplication(appName, appServiceManager);
			// bind the app context with the servlet context.
			servletContext.setAttribute(IAppServiceManager.class.getCanonicalName(), appServiceManager);
			
			// initialize DB.
	    	HibernateUtil.getSession();
			
			IEntityManager entityManager = appServiceManager.getEntityManager();
			MasterInstanceListener.addEntityListeners(entityManager);
			//TODO: load all customized entities from the application folder.
			//entityManager.reloadDir(path);
			//load all customized constant items from DB table.
			entityManager.addEventListener((ConstantServiceImpl)appServiceManager.getConstantService());
			//load all customized workflow from DB table in WorkflowLifecycleServiceImpl.
			
	    	// wire all services.
	    	OOEEContext context = OOEEContextFactory.createOOEEContext();
	    	List<String> serviceNodes = Registry.getInstance().getNodeChildren("/System/services");
        	for (String path: serviceNodes) {
        		String expression = Registry.getInstance().getExpression("/System/services/" + path);
        		logger.debug("Evaluate module initial expression: " + expression);
        		CompilationUnit compliedUnit = StatementParser.parse(expression, context);
        		compliedUnit.execute(context);
        		
        	}
        	appServiceManager.startLifeCycleProviders();
        	logger.info(appName + " is ready for request.");
	    	
        	appServiceManager.setState(State.ACTIVE);
        	HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
        	
        	entityManager.offUselessCaches();
		} catch (Throwable e) {
			logger.error("Fails to start Config server start! Error: " + e.getMessage(), e);
			appServiceManager.setState(State.FAILURE);
			HibernateUtil.releaseSession(HibernateUtil.getSession(), false);
		} 
		
	}
	
	public void stop(ServletContext servletContext) {
		AppServiceManagerImpl appServiceManager = (AppServiceManagerImpl)
				servletContext.getAttribute(IAppServiceManager.class.getCanonicalName());
		appServiceManager.stopLifeCycleProviders();
		
		IServerServiceManager.INSTANCE.removeApplication(appName);
	}
	
	public void updateCache() {
	}
	
}
