package org.shaolin.bmdp.runtime.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.Jedis;

class RedisListCache implements ICache<String, String> {

	private static final long serialVersionUID = 1L;
	
	private final Jedis jedis;
	
	private volatile boolean needStatistics = true;
	private String name;
    private AtomicLong readCount = new AtomicLong();
    private AtomicLong writeCount = new AtomicLong();
    private AtomicLong readHitCount = new AtomicLong();
    private AtomicLong writeHitCount = new AtomicLong();
    
	public RedisListCache(String cacheName, Jedis jedis) {
		this.name = cacheName;
		this.jedis = jedis;
	}
	
	public String get(String key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String put(String key, String value) {
		fireWriteEvent();
		if (jedis.exists(this.name + key))
            plusWriteHitCount();
		
		jedis.lpush(this.name + key, value);
		return value;
	}
	
	@Override
	public synchronized String putIfAbsent(String key, String value) {
		fireWriteEvent();
		if (jedis.exists(this.name + key)) {
			throw new IllegalStateException(key + " is duplicated!");
		}
		put(key, value);
		return value;
	}

	@Override
	public boolean containsKey(String key) {
		return jedis.exists(this.name + key);
	}
	
	@Override
	public String remove(String key) {
		fireWriteEvent();
		return localRemove(this.name + key);
	}
	
	@Override
	public String localRemove(String key) {
		List<String> v = null;
		if (jedis.exists(this.name + key)) {
			v = jedis.lrange(this.name + key, 0, -1);
		}
		jedis.del(this.name + key); 
		return v.toString();
	}

	@Override
	public void setMaxSize(int maxSize) {
		
	}

	@Override
	public int getMaxSize() {
		return -1;
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
		Set<String> keys = jedis.keys(this.name + "*");
		Iterator<String> it=keys.iterator() ;   
		while(it.hasNext()){   
			String key = it.next();   
			jedis.del(this.name + key); 
		}
	}

	@Override
	public int size() {
		return jedis.keys(this.name + "*").size();
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
