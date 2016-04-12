package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lizhiwe on 3/31/2016.
 */
public class GlobalWatcher extends BaseZookeeperWatcher {

    private Map<String, Watcher> smartWatchers = new ConcurrentHashMap<String, Watcher>();

    public GlobalWatcher(ZooKeeper pZooKeeper) {
        super(pZooKeeper);
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("received event" + event);
        }

        if (event.getPath() != null) {
            processEventChange(event);
        } else {
            processConnectionEvent(event);
        }
    }

    public SmartWatcher getSmartWatcher(String znode) {
        SmartWatcher watcher = (SmartWatcher) smartWatchers.get(znode);
        if (watcher == null) {
            watcher = new SmartWatcher(zooKeeper, znode);
            smartWatchers.put(znode, watcher);
        }
        return watcher;
    }

}
