package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by lizhiwe on 3/31/2016.
 */
public class ZooKeeperFactory {

    private static ZooKeeperFactory INS = new ZooKeeperFactory();

    private BlockingQueue<ZooKeeper> zkQueue = new ArrayBlockingQueue<ZooKeeper>(2);

    public static ZooKeeperFactory getInstance() {
        return INS;
    }

    /**
     *
     */
    private ZooKeeperFactory() {
        super();
    }

    /**
     *
     * @return
     */
    public ZooKeeper getCachedZookeeper() {
        try {
            return  zkQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param connectString
     * @param sessionTimeout
     * @param watcher
     * @return
     * @throws IOException
     */
    public ZooKeeper getZooKeeper(String connectString,int sessionTimeout,Watcher watcher) throws IOException {
        ZooKeeper zk = new ZooKeeper(connectString,sessionTimeout,watcher);
        zkQueue.offer(zk);
        return zk;
    }
}
