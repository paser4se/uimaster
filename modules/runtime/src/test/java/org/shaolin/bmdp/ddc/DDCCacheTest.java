package org.shaolin.bmdp.ddc;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.ZookeeperClient;
import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;

import java.util.UUID;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class DDCCacheTest {
    private Logger logger = Logger.getLogger(getClass());
    
    @Test
    public void testEmpty() {}
    
    public void testDDCCache() throws Exception{
        ZookeeperClient zc = new ZookeeperClient();
        zc.setConnectString("127.0.0.1:2182");
        zc.setTimeout(3000000);
        AppContext.register(new AppServiceManagerImpl("test",Thread.currentThread().getContextClassLoader()));
        AppContext.get().register(zc);

        zc.startService();


        ICache<String, String> cache = CacheManager.getInstance()
                .getCache("orderCache", 10240, true, String.class, String.class);

        cache.put("Q12347657", UUID.randomUUID().toString());

       // cache.put("Q12347657", UUID.randomUUID().toString());

        cache.put("Q88888888", UUID.randomUUID().toString());
        //cache.put("Q88888888", UUID.randomUUID().toString());

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cache.remove("Q88888888");

        Thread.sleep(6000);

        Assert.assertTrue(cache.get("Q99999999")!= null);
        logger.info("Q99999999="+cache.get("Q99999999"));

        Thread.sleep(600000000);

    }
}
