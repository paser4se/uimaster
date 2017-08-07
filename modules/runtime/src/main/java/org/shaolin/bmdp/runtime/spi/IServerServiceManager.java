/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.runtime.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.ce.ConstantServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Here is only one server service manager while running on multiple
 * applications. To reduce our cost of memory consuming.
 * 
 */
@Service("serviceManager")
public class IServerServiceManager implements IAppServiceManager, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(IServerServiceManager.class);

	private String masterNodeName = "uimaster";
	
	private State state = State.NONE;
	
	private Env env = Env.Production;
	
	private final Registry registry;

	// use spring service instead.
	// private final Map<Class<?>, IServiceProvider> services = new HashMap<Class<?>, IServiceProvider>();

	public static IServerServiceManager INSTANCE;
	
	private static ApplicationContext springContext;
	
	private transient ClassLoader appClassLoader;
	
	@Autowired
	private IEntityManager entityManager;
	
	@Autowired
	private IConstantService constantService;
	
	@Autowired
	private ISchedulerService schedulerService;
	
	public IServerServiceManager() {
		this.registry = Registry.getInstance();
	}
	
	// for entity generator only which could not rely on Spring boot container.
//	public static IServerServiceManager createMockServiceManager() {
//		IServerServiceManager instance = new IServerServiceManager();
//		instance.entityManager = new EntityManager();
//		instance.constantService = new ConstantServiceImpl();
//		instance.schedulerService = new ISchedulerService();
//		return instance;
//	}
	
	public static void setSpringContext(ApplicationContext springContext) {
		IServerServiceManager.springContext = springContext;
		INSTANCE = IServerServiceManager.springContext.getBean(IServerServiceManager.class);
	}
	
	public ApplicationContext getSpringContext() {
		return IServerServiceManager.springContext;
	}
	
	public String getMasterNodeName() {
		return masterNodeName;
	}

	public void setMasterNodeName(String masterNodeName) {
		this.masterNodeName = masterNodeName;
	}

	public IRegistry getRegistry() {
		return registry;
	}

	public IEntityManager getEntityManager() {
		return entityManager;
	}
	
	public IConstantService getConstantService() {
		return constantService;
	}
	
	public ISchedulerService getSchedulerService() {
		return schedulerService;
	}
	
	@SuppressWarnings("unchecked")
	public void register(IServiceProvider service) {
		//BeanFactoryPostProcessor 
		logger.info("Register service: " + service.getServiceInterface());
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) 
				IServerServiceManager.INSTANCE.getSpringContext()).getBeanFactory();
		beanFactory.registerSingleton(service.getServiceInterface().getCanonicalName(), service);
	}

	public boolean hasService(Class<?> serviceClass) {
		try {
			Object o = IServerServiceManager.INSTANCE.getSpringContext().getBean(serviceClass);
			return o != null;
		} catch (NoSuchBeanDefinitionException e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceClass) {
		T t= IServerServiceManager.INSTANCE.getSpringContext().getBean(serviceClass);
		if (t == null) {
			logger.warn("The service " + serviceClass.getName() 
					+ " is not existed! Are you sure it has registed or made a mistake while registering this service?");
			return null;
		}
		return t;
	}
	
	public int getServiceSize() {
		Map<String, IServiceProvider> services = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(IServiceProvider.class);
		return services.size();
	}
	
	public void shutdown() {
		this.schedulerService.stopService();
		((ConstantServiceImpl)this.constantService).stopService();
	}

	@Override
	public String getAppName() {
		return "uimaster";
	}

	public void setState(State s) {
		this.state = s;
	}
	
	@Override
	public State getState() {
		return state;
	}

	public void setRunningEnv(Env s) {
		this.env = s;
	}
	
	@Override
	public Env getRunningEnv() {
		return env;
	}
	
	@Override
	public void registerLifeCycleProvider(ILifeCycleProvider provider) {
		logger.info("Register life cycle service: {} with running level: {}", new Object[]{provider, provider.getRunLevel()});
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) 
				IServerServiceManager.INSTANCE.getSpringContext()).getBeanFactory();
		beanFactory.registerSingleton(provider.getClass().getCanonicalName(), provider);
		
		if (provider instanceof IServiceProvider) {
			register((IServiceProvider)provider);
		}
	}

	@Override
	public List<String> getLifeCycleServiceList() {
		Map<String, ILifeCycleProvider> lifeCycleProviders = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(ILifeCycleProvider.class);
		Collection<ILifeCycleProvider> values = lifeCycleProviders.values();
		List<String> temp = new ArrayList<String>();
		for (ILifeCycleProvider provider : values) {
			temp.add(provider.getClass().toString());
		}
		return temp;
	}

	@Override
	public void reloadLifeCycleService(String serviceName) {
		Map<String, ILifeCycleProvider> lifeCycleProviders = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(ILifeCycleProvider.class);
		Collection<ILifeCycleProvider> values = lifeCycleProviders.values();
		for (ILifeCycleProvider provider : values) {
			if (serviceName.equals(provider.getClass().toString())) {
				provider.reload();
				return;
			}
		}
	}

	/**
	 * The configuration of life-cycled service happens before IEntityManager.initRuntime
	 * 
	 */
	public void configureLifeCycleProviders() {
		Map<String, ILifeCycleProvider> lifeCycleProviders = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(ILifeCycleProvider.class);
		Collection<ILifeCycleProvider> values = lifeCycleProviders.values();
		List<ILifeCycleProvider> temp = new ArrayList<ILifeCycleProvider>(values);
		temp.sort(new Comparator<ILifeCycleProvider>() {
			@Override
			public int compare(ILifeCycleProvider o1, ILifeCycleProvider o2) {
				return (o1.getRunLevel() > o2.getRunLevel()) ? 1 : -1;
			}
		});
		for (ILifeCycleProvider p : temp) {
			logger.info("Configure life cycle service: {} with running level {}", new Object[]{p.getClass(), p.getRunLevel()});
			p.configService();
		}
	}
	
	/**
	 * The start stage of life-cycled service happens after a web context initialized event is fired.
	 * 
	 */
	public void startLifeCycleProviders() {
		Map<String, ILifeCycleProvider> lifeCycleProviders = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(ILifeCycleProvider.class);
		Collection<ILifeCycleProvider> values = lifeCycleProviders.values();
		List<ILifeCycleProvider> temp = new ArrayList<ILifeCycleProvider>(values);
		temp.sort(new Comparator<ILifeCycleProvider>() {
			@Override
			public int compare(ILifeCycleProvider o1, ILifeCycleProvider o2) {
				return (o1.getRunLevel() > o2.getRunLevel()) ? 1 : -1;
			}
		});
		for (ILifeCycleProvider p : temp) {
			logger.info("Start life cycle service: {} with running level {}", new Object[]{p.getClass(), p.getRunLevel()});
			p.startService();
		}
	}

	/**
	 * The stop stage of life-cycled service happens after a web context distroyed event is fired.
	 * 
	 */
	public void stopLifeCycleProviders() {
		logger.info("Stopping UIMaster Application...");
		Map<String, ILifeCycleProvider> lifeCycleProviders = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(ILifeCycleProvider.class);
		Collection<ILifeCycleProvider> values = lifeCycleProviders.values();
		List<ILifeCycleProvider> temp = new ArrayList<ILifeCycleProvider>(values);
		temp.sort(new Comparator<ILifeCycleProvider>() {
			@Override
			public int compare(ILifeCycleProvider o1, ILifeCycleProvider o2) {
				return (o1.getRunLevel() > o2.getRunLevel()) ? 1 : -1;
			}
		});
		for (ILifeCycleProvider p : temp) {
			logger.info("Stop life cycle service: {} with running level {}", new Object[]{p.getClass(), p.getRunLevel()});
			try {
				p.stopService();
			} catch (Exception e) {
				logger.warn("failed to stop this service!", e);
			}
		}
		logger.info("Stopped UIMaster Application!");
	}
	
	@Override
	public ClassLoader getAppClassLoader() {
		return appClassLoader;
	}

	public void setAppClassLoader(ClassLoader loader) {
		appClassLoader = loader;
	}

}
