package org.shaolin.bmdp.runtime.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.Jedis;

class RedisMapCache implements ICache<String, String> {

	private static final long serialVersionUID = 1L;
	
	private final Jedis jedis;
	
	private volatile boolean needStatistics = true;
	private String name;
    private AtomicLong readCount = new AtomicLong();
    private AtomicLong writeCount = new AtomicLong();
    private AtomicLong readHitCount = new AtomicLong();
    private AtomicLong writeHitCount = new AtomicLong();
    
	public RedisMapCache(String cacheName, Jedis jedis) {
		this.name = cacheName;
		this.jedis = jedis;
	}
	
	public String get(String key) {
		readCount.incrementAndGet();
		String v = jedis.hget(this.name, key);
		if (v != null) {
            readHitCount.incrementAndGet();
            return v;
		}
		return v;
	}
	
	@Override
	public String put(String key, String value) {
		fireWriteEvent();
		if (jedis.hexists(this.name, key))
            plusWriteHitCount();
		jedis.hsetnx(this.name, key, value);
		return value;
	}
	
	@Override
	public synchronized String putIfAbsent(String key, String value) {
		fireWriteEvent();
		if (jedis.hexists(this.name, key)) {
			throw new IllegalStateException(key + " is duplicated!");
		}
		put(key, value);
		return value;
	}

	@Override
	public boolean containsKey(String key) {
		return jedis.hexists(this.name, key);
	}
	
	@Override
	public String remove(String key) {
		fireWriteEvent();
		return localRemove(key);
	}
	
	@Override
	public String localRemove(String key) {
		if (jedis.hexists(this.name, key)) {
			jedis.hdel(this.name, key); 
		}
		return null;
	}

	@Override
	public void setMaxSize(int maxSize) {
		
	}

	@Override
	public int getMaxSize() {
		return jedis.hlen(this.name).intValue();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Collection<String> getValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Map<String, String> getCacheData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		Set<String> keys = jedis.hkeys(this.name + "*");
		Iterator<String> it=keys.iterator() ;   
		while(it.hasNext()){   
			String key = it.next();   
			jedis.hdel(this.name, key); 
		}
	}

	@Override
	public int size() {
		return jedis.keys(this.name).size();
	}

	@Override
	public void localClear() {
		clear();
	}
	
	protected void fireWriteEvent() {
        writeCount.incrementAndGet();
    }

    protected void plusWriteHitCount() {
        writeHitCount.incrementAndGet();
    }

	@Override
	public void setRefreshInterval(long minutes) {
		
	}

	@Override
	public void setDescription(String description) {
		
	}
	
	@Override
	public CacheInfoImpl getInfo() {
		 return new CacheInfoImpl(name, getMaxSize(), true,
	                needStatistics, size(), readCount.get(), 
	                writeCount.get(), readHitCount.get(),
	                writeHitCount.get(), -1, "Redis cache");
	}

	@Override
	public void setValueType(Class valueType) {
		throw new UnsupportedOperationException("Redis only supported String type");
	}

	@Override
	public Class getValueType() {
		return String.class;
	}

}
