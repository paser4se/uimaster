package org.shaolin.bmdp.runtime.ddc.client;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;
import org.shaolin.bmdp.runtime.ddc.client.sample.DDCHelper;
import org.shaolin.bmdp.runtime.ddc.client.sample.SampleLoggerListener;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.shaolin.bmdp.utils.SerializeUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lizhiwe on 4/6/2016.
 */
public class ZookeeperClient implements ILifeCycleProvider, IZookeeperClient, IServiceProvider {
    private Logger logger = Logger.getLogger(getClass());
    private ZooKeeper zooKeeper;

    private String connectString;
    private int timeout;

    private GlobalWatcher globalWatcher = new GlobalWatcher(null);

    /**
     * Start service.
     * <p/>
     * The order controlled by the startOrder.
     */
    @Override
    public void startService() {

        try {
            zooKeeper = ZooKeeperFactory.getInstance().getZooKeeper(connectString, timeout, globalWatcher);
            globalWatcher.setZooKeeper(zooKeeper);
            globalWatcher.addListener(new SampleLoggerListener());

            new DataInitializer(zooKeeper).init(globalWatcher);

        } catch (IOException e) {
            logger.warn("fail to create ZooKeeper", e);
        }
    }

    /**
     * Return whether the service stopped gracefully.
     *
     * @return true if the service ready to stop.
     */
    @Override
    public boolean readyToStop() {
        return false;
    }

    /**
     * Stop service.
     * <p/>
     * The stopService should stop the service immediately if the option is STOP.
     */
    @Override
    public void stopService() {
        //
    }

    /**
     * Notify the service to reload or refresh the data it cached.
     */
    @Override
    public void reload() {
        //
    }

    /**
     * Indicate the service will be run on which level.
     * By default is 0, which means the highest priority.
     */
    @Override
    public int getRunLevel() {
        return 10;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String createNode(String path, byte[] nodeValue, boolean persist) {
        try {
            CreateMode mode = persist ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL;
            Stat stat = null;
            SmartWatcher watcher = null;
            if (persist) {
                watcher = globalWatcher.getSmartWatcher(path);
                stat = zooKeeper.exists(path,watcher );
            } else {
                stat = zooKeeper.exists(path, false);
            }

            if (stat == null) {
                zooKeeper.create(path, nodeValue, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
            }

            if (watcher != null) {
                watcher.init();
            }

        } catch (KeeperException e) {
            logger.warn("", e);
        } catch (InterruptedException e) {
            logger.warn("", e);
        }
        return path;
    }

    @Override
    public void removeNode(String path) {
        Stat emptyStat = null;
        try {
           emptyStat = zooKeeper.exists(path,false);
            if (emptyStat != null) {
                zooKeeper.getData(path, false, emptyStat);
                if (logger.isDebugEnabled() ) {
                    logger.debug("removing node :"+path);
                }
                zooKeeper.delete(path, emptyStat.getVersion());
                if (logger.isDebugEnabled() ) {
                    logger.debug("removed node :"+path);
                }
            }

        } catch (KeeperException e) {
            logger.warn("", e);
        } catch (InterruptedException e) {
            logger.warn("", e);
        }
    }

    public void loadData(final ICache cache) {
        try {
            List<String> children = zooKeeper.getChildren(DDCHelper.getPathFromCacheName(cache.getName()), false);
            for (final String child : children) {
                zooKeeper.getData(DDCHelper
                        .getPathFromCacheNameAndKey(cache.getName(), child), null, new AsyncCallback.DataCallback() {
                    @Override
                    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {

                        logger.info("loading data for :"+child+" = "+ Arrays.toString(bytes));
                        try {
                            cache.put(child, SerializeUtil.readData(bytes, cache.getValueType()));
                        } catch (Exception e) {
                            logger.warn("", e);
                        }
                    }
                }, null);
            }
        } catch (KeeperException e) {
            logger.warn("", e);
        } catch (InterruptedException e) {
            logger.warn("", e);
        }

    }

    @Override
    public void cleanCache(String cacheName) {
        ICache cache = CacheManager.getInstance().getCache(cacheName, null, null);
        cache.clear();
        Stat stat = new Stat();
        try {
            String path = DDCHelper.getPathFromCacheName(cacheName);
            zooKeeper.getData(path,false,stat);
            zooKeeper.delete(path,stat.getVersion());
            this.createNode(path,new byte[0],true);
        }catch (Exception e) {
            logger.warn("", e);
        }
    }

    @Override
    public Class getServiceInterface() {
        return IZookeeperClient.class;
    }
}
