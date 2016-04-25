/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed;

import org.apache.zookeeper.ZooKeeper;

/**
 * @author lizhiwe
 *
 */
public class DefaultWatcher extends BaseWatcher {

    /**
     * 
     */
    public DefaultWatcher(ZooKeeper zookeeper) {
       super(zookeeper);
    }

   
}
