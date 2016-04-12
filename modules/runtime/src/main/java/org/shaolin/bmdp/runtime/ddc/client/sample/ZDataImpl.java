package org.shaolin.bmdp.runtime.ddc.client.sample;

import org.apache.zookeeper.Watcher;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

import java.util.Arrays;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public class ZDataImpl implements ZData {
    private byte[] data;
    private String path;
    private int version;
    private Watcher watcher;

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public String toString() {
        return "ZDataImpl{" +
                "data=" + Arrays.toString(data) +
                ", path='" + path + '\'' +
                ", version=" + version +
                ", watcher=" + watcher +
                '}';
    }

    public static ZData newInstance(String path, byte[] data, int version) {
        ZDataImpl fragment = new ZDataImpl();
        fragment.setData(data);
        fragment.setVersion(version);
        fragment.setPath(path);
        return fragment;
    }

}
