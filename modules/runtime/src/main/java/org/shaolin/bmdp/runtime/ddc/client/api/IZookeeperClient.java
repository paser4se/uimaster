package org.shaolin.bmdp.runtime.ddc.client.api;

import org.shaolin.bmdp.runtime.cache.ICache;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public interface IZookeeperClient {
    public static final String ROOT = "/uimaster";
    public String createNode(String path,byte[] nodeValue,boolean persist);

    public void removeNode(String path);

    public void loadData(ICache cache);


    public void cleanCache(String cacheName);
}
