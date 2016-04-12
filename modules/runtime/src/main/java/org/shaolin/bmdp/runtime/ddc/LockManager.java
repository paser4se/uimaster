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
package org.shaolin.bmdp.runtime.ddc;

import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.DDCFacade;
import org.shaolin.bmdp.utils.SerializeUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A in-memory lock manager to emulate key-based lock.
 * 
 * @param <T>
 *            Key type
 */
public final class LockManager<T> {
	private static final class LockWithCounter implements Serializable {
		private final Lock lock;
		private final AtomicInteger counter;

		public LockWithCounter() {
			lock = new ReentrantLock();
			counter = new AtomicInteger(2);
			lock.lock();
		}

		public int decrementAndGet() {
			return counter.decrementAndGet();
		}

		public int incrementAndGet() {
			return counter.incrementAndGet();
		}

		public void lock() {
			lock.lock();
		}

		public void unlock() {
			lock.unlock();
		}
	}

	private String name;
	private boolean needSynch;


	public LockManager(String name,boolean needSynch) {
		this.name = name;
		this.needSynch = needSynch;

		if (needSynch) {
			try {
				lockMap = CacheManager.getInstance().getCache(name,-1,true,String.class,LockWithCounter.class);
			}catch(Exception e) {
				e.printStackTrace();
			}

		}
	}

	public String getName() {
		return name;
	}


	private  ICache<String, LockWithCounter> lockMap ;




	/**
	 * Acquire the lock of the key.
	 * 
	 * @param key
	 */
	public void acquireLock(T key) {
		LockWithCounter lock = lockMap.get(key.toString());
		if (lock == null) {
			lock = new LockWithCounter();
			LockWithCounter existedLock = lockMap.putIfAbsent(key.toString(), lock);
			if (existedLock == null) {
				lock.decrementAndGet();
				return;
			} else {
				lock.unlock();
				lock = existedLock;
			}
		}
		lock.incrementAndGet();
		lock.lock();
		if (lockMap.get(key.toString()) != lock) {
			lock.unlock();
			acquireLock(key);
		}
	}

	/**
	 * Attach lock with current thread.
	 * 
	 * The method can only be used with the tryLock method.
	 * 
	 * @param key
	 */
	public void attachLock(T key) {
		lockMap.get(key.toString()).lock();
	}

	/**
	 * Detach the lock from the thread.
	 * 
	 * The method can only be used with the tryLock method.
	 * 
	 * @param key
	 */
	public void detachLock(T key) {
		lockMap.get(key.toString()).unlock();
	}

	/**
	 * Check the key is locked or not.
	 * 
	 * @param key
	 * @return
	 */
	public boolean isLocked(T key) {
		return lockMap.containsKey(key.toString());
	}

	/**
	 * Release the lock of the key.
	 * 
	 * @param key
	 */
	public void releaseLock(T key) {
		LockWithCounter lock = lockMap.get(key.toString());
		if (lock.decrementAndGet() == 0) {
			lockMap.remove(key.toString());
		}
		lock.unlock();
	}

	public int size() {
		return lockMap.size();
	}

	/**
	 * Try to lock the key.
	 * 
	 * Return true if locked, otherwise return false.
	 * 
	 * @param key
	 * @return
	 */
	public boolean tryLock(T key) {
		LockWithCounter lock = lockMap.get(key.toString());
		if (lock != null) {
			return false;
		} else {
			lock = new LockWithCounter();
			LockWithCounter existedLock = lockMap.putIfAbsent(key.toString(), lock);
			if (existedLock == null) {
				lock.decrementAndGet();
				return true;
			} else {
				lock.unlock();
				return false;
			}
		}
	}
}
