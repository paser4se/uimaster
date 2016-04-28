/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.analyzer.distributed;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;
import org.shaolin.bmdp.runtime.ddc.client.sample.ZDataImpl;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public class BaseWatcher implements Watcher {
    protected Logger logger = Logger.getLogger(getClass());
    protected ZooKeeper zooKeeper;
    protected List<DataListener> listenerList = new ArrayList<DataListener>();

    public BaseWatcher(
            ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        logger.debug("received event"+event);
        if (event.getPath() != null) {
            processEventChange(event);
        } else {
            processConnectionEvent(event);
        }
    }

    public boolean addListener(DataListener listener) {
        if (listener == null) {
            return false;
        }
        return listenerList.add(listener);
    }

    public boolean removeListener(DataListener listener) {
        if (listener == null) {
            return false;
        }
        return listenerList.remove(listener);
    }

    protected void processConnectionEvent(WatchedEvent event) {
        logger.info(event);
    }

    protected void processEventChange(final WatchedEvent event) {

        if (zooKeeper == null) {
            logger.info("no connection to zookeeper server");
            return;
        }


        zooKeeper.getData(event.getPath(), this, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

                ZData zData = ZDataImpl.newInstance(path,data,stat == null ? 0 :stat.getVersion());
              
                try {
                    zooKeeper.exists(path, BaseWatcher.this, new StatCallback() {
                        @Override
                        public void processResult(int i, String s, Object o, Stat stat) {
                            ///
                        }
                    },null);
                }catch (Exception e) {
                    logger.error("Error", e);
                }

                for (int i = 0, j = listenerList.size(); i < j; i++) {
                    switch (event.getType()) {
                    case NodeDataChanged:
                        listenerList.get(i).onNodeUpdate(zData);
                        break;
                    case NodeDeleted:
                        listenerList.get(i).onNodeDelete(zData);
                        break;
                    case NodeCreated:
                        listenerList.get(i).onNodeCreated(zData);
                        break;
                    case NodeChildrenChanged:
                        try {
                        List<String> children =  zooKeeper.getChildren(path,BaseWatcher.this);
                        zData.setChildren(children);
                        listenerList.get(i).onChildChanged(zData);
                        }catch (Exception e) {
                            logger.error("Error", e);
                        }
                        
                       

                        break;
                    default:
                        //Nothing to do
                        ;
                    }
                }
            }
        }, null);
    }
}
