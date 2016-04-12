package org.shaolin.bmdp.ddc;

import junit.framework.Assert;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.LockManager;
import org.shaolin.bmdp.runtime.ddc.client.ZookeeperClient;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;

import java.util.UUID;

/**
 * Created by lizhiwe on 4/11/2016.
 */
public class DDCLockManagerTest {

    @Test
    public void testSynchLockManager() throws Exception{
        ZookeeperClient zc = new ZookeeperClient();
        zc.setConnectString("127.0.0.1:2182");
        zc.setTimeout(3000000);
        AppContext.register(new AppServiceManagerImpl("test",Thread.currentThread().getContextClassLoader()));
        AppContext.get().register(zc);

        zc.startService();


      LockManager<String> lk = new LockManager<>("lock1",true);

       boolean locked = lk.tryLock("Q12347657");
        Assert.assertTrue(locked);

        Thread.sleep(20000);
        locked = lk.tryLock("Q88888888");
        Assert.assertTrue(locked);

        Thread.sleep(20000);

        lk.releaseLock("Q88888888");

        Thread.sleep(100000l);
    }
}
