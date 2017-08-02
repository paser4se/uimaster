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
package org.shaolin.bmdp.runtime.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.IConstantService;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Here is only one app service manager for one application.
 * 
 * Please use {@link IServerServiceManager} directly.
 */
@Deprecated
public class AppServiceManagerImpl implements IAppServiceManager, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(AppServiceManagerImpl.class);

	private final String appName;
	
	private State state = State.START;
	
	/**
	 * Design time constructor.
	 * 
	 * @param appName
	 * @param appClassLoader
	 */
	public AppServiceManagerImpl(String appName, ClassLoader appClassLoader) {
		this.appName = appName;
	}
	
	/**
	 * Runtime constructor.
	 * 
	 * @param appName
	 * @param appClassLoader
	 * @param uimasterApp
	 */
	public AppServiceManagerImpl(String appName, ClassLoader appClassLoader, IAppServiceManager uimasterApp) {
		this.appName = appName;
	}
	
	
	public String getAppName() {
		if (UserContext.getUserContext() != null) {
			return UserContext.getUserContext().getOrgCode();
		}
		return this.appName;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State s) {
		this.state = s;
	}
	
	public IEntityManager getEntityManager() {
		return IServerServiceManager.INSTANCE.getEntityManager();
	}
	
	public Object getHibernateConfiguration() {
		return null;
	}

	public Object getHibernateSessionFactory() {
		return null;
	}
	
	public ClassLoader getAppClassLoader() {
		return null;
	}
	
	@Override
	public IConstantService getConstantService() {
		return IServerServiceManager.INSTANCE.getConstantService();
	}

	@Override
	public void registerLifeCycleProvider(ILifeCycleProvider provider) {
		logger.info("Register life cycle service: {0}, this: {1}", new Object[]{provider, this.hashCode()});
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
	}

	@Override
	public void register(IServiceProvider service) {
		//BeanFactoryPostProcessor 
		logger.info("Register service: " + service.getServiceInterface());
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) 
				IServerServiceManager.INSTANCE.getSpringContext()).getBeanFactory();
		beanFactory.registerSingleton(service.getServiceInterface().getCanonicalName(), service);
	}

	@Override
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
	
	public boolean hasService(Class<?> serviceClass) {
		try {
			Object o = IServerServiceManager.INSTANCE.getSpringContext().getBean(serviceClass);
			return o != null;
		} catch (NoSuchBeanDefinitionException e) {
			return false;
		}
	}

	public int getServiceSize() {
		Map<String, IServiceProvider> services = IServerServiceManager.INSTANCE.getSpringContext()
				.getBeansOfType(IServiceProvider.class);
		return services.size();
	}
}
