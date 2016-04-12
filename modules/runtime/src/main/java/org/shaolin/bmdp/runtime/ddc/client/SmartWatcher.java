package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.ddc.client.sample.CacheNodeListener;
import org.shaolin.bmdp.runtime.ddc.client.sample.DDCHelper;

/**
 * Created by lizhiwe on 3/31/2016.
 */
public class SmartWatcher extends BaseZookeeperWatcher {

    private String znode;

    public SmartWatcher(ZooKeeper zooKeeper, String znode) {
        super(zooKeeper);
        this.znode = znode;
        CacheNodeListener listener = new CacheNodeListener(znode, zooKeeper,this);

        this.addListener(listener);
        if (logger.isDebugEnabled()) {
            logger.debug("set node  watcher for path:" + znode);
        }
        zooKeeper.exists(znode, this, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int i, String s, Object o, Stat stat) {
                ///
            }
        }, null);

    }

    public void init() {

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("set children change watcher for path:" + znode);
            }

            zooKeeper.getChildren(znode, this);
        } catch (Exception e) {
            logger.warn(e);
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug(event.getPath() + " :" + event.getState() + ":" + event.getType() + ":\n" + event);
        }

        if (event.getPath() != null && event.getPath().startsWith(znode)) {
            processEventChange(event);
        }
    }

}
