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
package org.shaolin.bmdp.utils;

/**
 * Load class from the previously set class loader
 */
public class ClassLoaderUtil
{
	private static ClassLoader currentClassLoader;
	
    /**
     * Load class from the previously set class loader. If not set, use
     * default class loader
     *
     * @param       className       class name
     * @return      the loaded class
     */
	public static Class<?> loadClass(String className)
			throws ClassNotFoundException {
		ClassLoader loader = currentClassLoader;
		if (loader == null) {
			loader = Thread.currentThread().getContextClassLoader();
		}
		try {
			return Class.forName(className, false, loader);
		} catch (ClassNotFoundException e) {
			ClassLoader contextLoader = Thread.currentThread()
					.getContextClassLoader();
			if (contextLoader != loader) {
				return Class.forName(className, false, contextLoader);
			}
			throw e;
		}
	}

    /**
     * Set the class loader
     *
     * @param       classLoader     the class loader
     */
	public static void setCurrentClassLoader(ClassLoader classLoader) {
		currentClassLoader = classLoader;
	}

}
