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
        
        final ZookeeperServiceLauncher launcher0 = new ZookeeperServiceLauncher();

        URL in = ZookeeperLauncherTest.class.getResource("zoo.cfg");
        
        launcher0.setConfigFileLocation(in.getPath());

        final ZookeeperServiceLauncher launcher1 = new ZookeeperServiceLauncher();
        launcher1.setConfigFileLocation(ZookeeperLauncherTest.class.getResource("zoo1.cfg").getPath());

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
