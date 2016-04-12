package org.shaolin.bmdp.runtime.ddc;

import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
/**
 * Created by lizhiwe on 4/6/2016.
 */
public class ZookeeperServiceLauncher implements IServiceProvider, ILifeCycleProvider, IZookeeperServiceLauncher {
    private static final String DEFAULT_CFG = "/opt/uimaster/zkcfg/zookeeper.cfg";
    private String configFileLocation = DEFAULT_CFG;

    private boolean started = false;

    public ZookeeperServiceLauncher() {
        super();
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
        QuorumPeerMain.main(new String[]{configFileLocation});
        started = true;
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
        //nothing to do
    }

    /**
     * Indicate the service will be run on which level.
     * By default is 0, which means the highest priority.
     */
    @Override
    public int getRunLevel() {
        return 0;
    }

    @Override
    public Class getServiceInterface() {
        return IZookeeperServiceLauncher.class;
    }

    public String getConfigFileLocation() {
        return configFileLocation;
    }

    public void setConfigFileLocation(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }
}
