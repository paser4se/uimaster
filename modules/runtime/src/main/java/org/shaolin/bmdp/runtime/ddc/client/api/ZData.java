package org.shaolin.bmdp.runtime.ddc.client.api;

import java.util.List;

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
    
    List<String> getChildren();
    void setChildren(List<String> children);
}
