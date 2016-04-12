package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by lizhiwe on 3/31/2016.
 */
public class ZooKeeperFactory {

    private static ZooKeeperFactory INS = new ZooKeeperFactory();

    public static ZooKeeperFactory getInstance() {
        return INS;
    }


    private ZooKeeperFactory() {
        super();
    }

    public ZooKeeper getZooKeeper(String connectString,int sessionTimeout,Watcher watcher) throws IOException {
        return new ZooKeeper(connectString,sessionTimeout,watcher);
    }
}
