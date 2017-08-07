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

import java.util.List;

/**
 * The application service made for a single application
 * 
 * @author wushaol
 *
 */
public interface IAppServiceManager {

	enum State {NONE, START, ACTIVE, FAILURE}
	
	enum Env {Testing, Production}
	
	/**
	 * the name of current application.
	 * 
	 * @return
	 */
	String getAppName();
	
	State getState();
	
	Env getRunningEnv();
	
	/**
	 * The life cycle provider only invokes when the system is up.
	 * 
	 * @param provider
	 */
	void registerLifeCycleProvider(ILifeCycleProvider provider);
	
	/**
	 * Register a normal service.
	 * 
	 * @param service
	 */
	void register(IServiceProvider service);
	
	/**
	 * Get service.
	 * 
	 * @param serviceClass
	 * @return
	 */
	<T> T getService(Class<T> serviceClass);
	
	boolean hasService(Class<?> serviceClass);
	
	/**
	 * Get the name of all life cycle service.
	 * 
	 * @return
	 */
	public List<String> getLifeCycleServiceList();
	
	/**
	 * Reload a life cycle service.
	 * 
	 * @param serviceName
	 */
	public void reloadLifeCycleService(String serviceName);
	
	/**
	 * Application owners' entity manager. this purposes on the application entity customization.
	 * 
	 * @return
	 */
	IEntityManager getEntityManager();
	
	/**
	 * Get constant service
	 * 
	 * @return
	 */
	IConstantService getConstantService();
	
	/**
	 * Get application class loader(ServletClassLoader)
	 * 
	 * @return
	 */
	ClassLoader getAppClassLoader();

}
