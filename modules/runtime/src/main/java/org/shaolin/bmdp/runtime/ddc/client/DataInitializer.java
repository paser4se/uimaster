package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lizhiwe on 3/31/2016.
 */
public class DataInitializer {

    private final ZooKeeper zooKeeper;

    public DataInitializer(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    private String[] znodes = new String[] { IZookeeperClient.ROOT};

    public void init(Watcher globalWatcher) {
        List<String> zlist = Arrays.asList(znodes);

        for (String znode : zlist) {
            try {
                if (zooKeeper.exists(znode, false) == null) {
                    zooKeeper.create(znode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {
                    zooKeeper.exists(znode, globalWatcher);
                }

                zooKeeper.getChildren(znode,globalWatcher);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
