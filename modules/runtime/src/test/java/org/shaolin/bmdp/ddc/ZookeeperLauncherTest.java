package org.shaolin.bmdp.ddc;

import java.net.URL;

import org.junit.Test;
import org.shaolin.bmdp.runtime.ddc.ZookeeperServiceLauncher;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class ZookeeperLauncherTest {

    @Test
    public void testZookeeperService() {
        
        final ZookeeperServiceLauncher launcher0 = new ZookeeperServiceLauncher(true);

        URL in = ZookeeperLauncherTest.class.getResource("zoo.cfg");
        
        launcher0.getProperties().setProperty(ZookeeperServiceLauncher.CLIENT_PORT, "2182");
        launcher0.getProperties().setProperty(ZookeeperServiceLauncher.DATADIR, "/tmp/zookeeper");
        launcher0.getProperties().setProperty("server.1", "127.0.0.1:2888:3888");
        launcher0.getProperties().setProperty("server.2", "127.0.0.1:4888:5888");
        
       
        

        final ZookeeperServiceLauncher launcher1 = new ZookeeperServiceLauncher(true);
        launcher1.getProperties().setProperty(ZookeeperServiceLauncher.CLIENT_PORT, "2183");
        launcher1.getProperties().setProperty(ZookeeperServiceLauncher.DATADIR, "/tmp/zookeeper1");
        launcher1.getProperties().setProperty("server.1", "127.0.0.1:2888:3888");
        launcher1.getProperties().setProperty("server.2", "127.0.0.1:4888:5888");

        new Thread(new Runnable() {
            @Override
            public void run() {
                launcher0.startService();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                launcher1.startService();
            }
        }).start();

        try {
            Thread.sleep(300000l);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
