package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;
import org.shaolin.bmdp.runtime.ddc.client.sample.CacheNodeListener;
import org.shaolin.bmdp.runtime.ddc.client.sample.DDCHelper;
import org.shaolin.bmdp.utils.SerializeUtil;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class DDCFacade {

    private Logger logger = Logger.getLogger(getClass());

    private IZookeeperClient zkClient;

    private ICache cache;

   // private CacheNodeListener cacheNodeListener =  new CacheNodeListener();



    public IZookeeperClient getZookeeperClient() {
        if (zkClient == null) {
            zkClient =  AppContext.get().getService(IZookeeperClient.class);
        }
        return zkClient;
    }

    public void put(DDCData data) {
        try {
            getZookeeperClient().createNode(DDCHelper.getPathFromCacheNameAndKey(data.getCacheName(), data.getKey().toString()),
                    SerializeUtil.serializeData((Serializable) data.getValue()),false);
        } catch (IOException e) {
            logger.warn("",e);
        }
    }
    public void remove(DDCData data) {
        getZookeeperClient().removeNode(DDCHelper.getPathFromCacheNameAndKey(data.getCacheName(), data.getKey().toString()));
    }
    public <K, V> void setCache(ICache<K,V> cache) {
        this.cache = cache;
        //cacheNodeListener.setCache(cache);

    }

    public static DDCData newData(Object key, Object value, String cacheName) {
        return new DDCData(key,value,cacheName);

    }

    public  void initCache(ICache cache) {
        getZookeeperClient().createNode(DDCHelper.getPathFromCacheName(cache.getName()),
                cache.getValueType().getCanonicalName().getBytes(),true);

        getZookeeperClient().loadData(cache);
    }

    public static class DDCData {
        private Object key;

        public DDCData(Object key, Object value, String cacheName) {
            this.key = key;
            this.value = value;
            this.cacheName = cacheName;
        }

        private Object value;
        private String cacheName;

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public String getCacheName() {
            return cacheName;
        }
    }
}
