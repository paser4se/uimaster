package org.shaolin.bmdp.runtime.ddc.client.api;

import org.apache.zookeeper.Watcher;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public interface ZData {

     byte[] getData();
     void setData(byte[] data);

    String getPath();
    void setPath(String path);

    int getVersion();
    void setVersion(int version);

    Watcher getWatcher();
    void setWatcher(Watcher watcher);
}
