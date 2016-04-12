package org.shaolin.bmdp.runtime.ddc.client.sample;

import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;

/**
 * Created by lizhiwe on 4/7/2016.
 */
public class DDCHelper {

    public static String getNodeNameFromPath(String path) {
        String[] nodes = path.split("/");
        return nodes[nodes.length-1];
    }

    public static String getPathFromCacheName(String cacheName) {
        return IZookeeperClient.ROOT+"/"+cacheName;
    }
    public static String getPathFromCacheNameAndKey(String cacheName,String key) {
        return IZookeeperClient.ROOT+"/"+cacheName+"/"+key;
    }
}
