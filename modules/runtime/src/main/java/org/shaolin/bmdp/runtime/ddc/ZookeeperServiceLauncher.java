package org.shaolin.bmdp.runtime.ddc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;

/**
 * Created by lizhiwe on 4/6/2016.
 */
public class ZookeeperServiceLauncher implements IServiceProvider, ILifeCycleProvider, IZookeeperServiceLauncher {
    // private static final String DEFAULT_CFG = "/opt/uimaster/zkcfg/zookeeper.cfg";
    // private String configFileLocation = DEFAULT_CFG;

    private boolean started = false;

    private Properties properties = new Properties();

    private Logger logger = Logger.getLogger(getClass());

    public static final String CLIENT_PORT = "clientPort";

    public static final String DATADIR = "dataDir";

    private boolean isClusterNode = false;

    public ZookeeperServiceLauncher(boolean isClusterNode) {
        super();
        this.isClusterNode = isClusterNode;
        InputStream in = getClass().getResourceAsStream("default_zookeeper.cfg");
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Error loading default properties", e);
        }

    }

    /**
     * Start service.
     * <p/>
     * The order controlled by the startOrder.
     */
    @Override
    public void startService() {
        if (started) {
            return;
        }
        final QuorumPeerConfig qCfg = new QuorumPeerConfig();
        try {
            qCfg.parseProperties(properties);
        } catch (IOException | ConfigException e) {
            logger.error("Error starting Zookeeper server serivce", e);
        }

        if (isClusterNode) {
            final QuorumPeerMain peer = new QuorumPeerMain();

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        peer.runFromConfig(qCfg);

                        started = true;
                    } catch (IOException e) {
                        logger.error("Error starting Zookeeper server serivce", e);
                    }

                }

            }).start();

            return;
        }

        final ZooKeeperServerMain serverMain = new ZooKeeperServerMain();
        final ServerConfig cfg = new ServerConfig();
        cfg.readFrom(qCfg);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    serverMain.runFromConfig(cfg);
                } catch (IOException e) {
                    logger.error("Error starting Zookeeper server serivce", e);
                }

            }

        }).start();

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
        // nothing to do
    }

    /**
     * Notify the service to reload or refresh the data it cached.
     */
    @Override
    public void reload() {
        // nothing to do
    }

    /**
     * Indicate the service will be run on which level. By default is 0, which means the highest priority.
     */
    @Override
    public int getRunLevel() {
        return 1;
    }

    @Override
    public Class getServiceInterface() {
        return IZookeeperServiceLauncher.class;
    }

    public Properties getProperties() {
        return properties;
    }

}
