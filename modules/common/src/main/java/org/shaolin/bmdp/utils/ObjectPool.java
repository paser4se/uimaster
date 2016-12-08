package org.shaolin.bmdp.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ObjectPool<T> {

	private final BlockingQueue<T> pool;
    private final AtomicLong createdObjects = new AtomicLong(0);
    private final int maxSize;
    private final int minSize;
 
    public ObjectPool(int minSize, int maxSize) {
    	if (maxSize < 0) {
    		pool = new LinkedBlockingQueue<T>();
    	} else {
    		pool = new ArrayBlockingQueue<T>(maxSize, true);
    	}
    	this.minSize = minSize;
        this.maxSize = maxSize;
        
        createPool();
    }
 
    private void createPool() {
        for (int i = 0; i < minSize; ++i) {
            pool.add(createObject());
            createdObjects.incrementAndGet();
        }
    }
    
    public T acquire() {
    	T element = pool.poll();
    	if (element != null) {
    		return element;
    	}
    	if (maxSize > 0 && pool.size() >= maxSize) {
    		throw new IllegalStateException("Pool size is excceded! maxSize: " + maxSize);
    	}
    	T n = createObject();
    	pool.add(n);
    	createdObjects.incrementAndGet();
    	return n;
    }
    
    public T acquireWithBlock() throws InterruptedException {
        return pool.take();
    }
 
    public void recycle(T resource) {
        pool.add(resource);
        createdObjects.decrementAndGet();
    }
 
    public int created() {
    	return pool.size();
    }
    
    public long used() {
    	return createdObjects.get();
    }
    
    protected abstract T createObject();
    
}
