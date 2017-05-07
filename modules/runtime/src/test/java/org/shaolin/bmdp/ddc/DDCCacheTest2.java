package org.shaolin.bmdp.ddc;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.ZookeeperClient;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;

import java.util.UUID;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class DDCCacheTest2 {

    private Logger logger = Logger.getLogger(getClass());
    public void testDDCCache()throws Exception {
        ZookeeperClient zc = new ZookeeperClient();
        zc.setConnectString("127.0.0.1:2183");
        zc.setTimeout(3000000);
        AppContext.register(new AppServiceManagerImpl("test",Thread.currentThread().getContextClassLoader()));
        AppContext.get().register(zc);

        zc.startService();


        ICache<String, String> cache = CacheManager.getInstance()
                .getCache("orderCache", 10240, true, String.class, String.class);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(cache.get("Q12347657")!= null);
        logger.info(cache.get("Q12347657"));


        cache.put("Q99999999", UUID.randomUUID().toString());
        //cache.put("Q99999999", UUID.randomUUID().toString());

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cache.remove("Q99999999");

        Thread.sleep(600000);

    }
}
