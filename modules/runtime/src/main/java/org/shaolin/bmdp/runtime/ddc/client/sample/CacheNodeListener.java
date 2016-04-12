package org.shaolin.bmdp.runtime.ddc.client.sample;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;
import org.shaolin.bmdp.utils.SerializeUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class CacheNodeListener implements DataListener {

    private Logger logger = Logger.getLogger(getClass());

    private String nodeName;

    private ICache cache;

    private ZooKeeper zooKeeper;

    private Watcher watcher;

    //private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    public CacheNodeListener(String nodeName, ZooKeeper zooKeeper, Watcher watcher) {
        this.nodeName = nodeName;
        this.zooKeeper = zooKeeper;
        this.watcher = watcher;
    }

    @Override
    public void onNodeUpdate(ZData data) {
        //
    }

    @Override
    public void onNodeDelete(ZData data) {
        logger.info("node delete:" + data);
        String key = DDCHelper.getNodeNameFromPath(data.getPath());

        getCache().remove(key);

        logger.info("after delete:" + data + "\n cache:" + cache);
    }

    @Override
    public void onNodeCreated(ZData data) {
        logger.info("node created:" + data);
        String key = DDCHelper.getNodeNameFromPath(data.getPath());

        if (getCache().containsKey(key)) {
            return;
        }

        if (key.split("/").length < 3) {
            return;
        }

        try {
            getCache().put(key, SerializeUtil.readData(data.getData(), getCache().getValueType()));
        } catch (ClassNotFoundException e) {
            logger.warn("error recover data on node :" + data.getPath(), e);
        } catch (IOException e) {
            logger.warn("error recover data on node :" + data.getPath(), e);
        }
        logger.info("after create:" + data + "\n cache:" + cache);
    }

    public void onChildChanged(final ZData data) {

        zooKeeper.getChildren(data.getPath(), false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int i, String s, Object o, List<String> list) {
                for (final String childName : list) {
                    logger.info("---------> checking child " + childName);
                    Object v = getCache().get(childName);
                    if (v != null) {
                        continue;
                    }
                    final String path = DDCHelper.getPathFromCacheNameAndKey(getCache().getName(), childName);

                    zooKeeper.getData(path, watcher, new DataCallback() {
                        @Override
                        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                            try {
                                getCache().put(childName, SerializeUtil.readData(bytes, getCache().getValueType()));
                            } catch (Exception e) {
                                logger.warn("error recover data on node :" + s, e);
                            }

                        }
                    }, null);
                }
            }
        }, null);

    }

    public ICache getCache() {

        if (cache == null) {
            this.setCache(CacheManager.getInstance().getCache(DDCHelper.getNodeNameFromPath(nodeName), null, null));
        }
        return cache;
    }

    public void setCache(ICache cache) {
        this.cache = cache;
    }

}
