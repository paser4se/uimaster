package org.shaolin.bmdp.runtime.ddc.client.sample;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.ddc.client.api.DataAction;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public class ZookeeperHelper {

    private Logger logger = Logger.getLogger(getClass());

    private ZooKeeper zooKeeper;

    public ZookeeperHelper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void updateData(final ZData zData,final DataAction dataAction,final  Watcher watcher) {

        zooKeeper.getData(zData.getPath(), watcher, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                zData.setWatcher(watcher);
                zData.setData(data);
                zData.setVersion(stat.getVersion());
                zData.setPath(path);
                byte[] expected = dataAction.execute(zData);
                try {
                    zooKeeper.setData(path, expected, stat.getVersion());
                } catch (KeeperException e) {
                    processKeeperException(e, watcher, dataAction, true);
                } catch (InterruptedException e) {
                    logger.warn("encounter exception", e);
                }
            }
        }, null);
    }

    protected void processKeeperException(final KeeperException e, final Watcher watcher,final DataAction dataAction,
            boolean asynchFlag) {
        switch (e.code()) {
        case BADVERSION:

            if (asynchFlag) {
                zooKeeper.getData(e.getPath(), watcher, new AsyncCallback.DataCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                        ZData zData = ZDataImpl.newInstance(path, data, stat.getVersion());
                        updateData(zData, dataAction, watcher);
                    }
                }, null);
            } else {

            }

            break;
        default:
            logger.warn("encounter exception", e);
        }
    }
}
