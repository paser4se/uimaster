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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * @author lizhiwe
 *
 */
public class WorkersNodeListener implements DataListener {

    private final Logger logger = Logger.getLogger(getClass());
    
    private List<String> knownWorkers;

    private ZooKeeper zookeeper;

    private Watcher watcher;

    /**
     * 
     */
    public WorkersNodeListener( ZooKeeper zookeeper, Watcher watcher) {
        knownWorkers = Collections.synchronizedList(new ArrayList<String>());
        this.zookeeper = zookeeper;
        this.watcher = watcher;
    }
    
    public void init() {
        try {
            knownWorkers = zookeeper.getChildren(ZKDistributedJobEngine.NODES_PATH, watcher);
            logger.info("get workers in init :"+Arrays.toString(knownWorkers.toArray()));
        } catch (KeeperException | InterruptedException e) {
            logger.warn("error get node children", e);
        }
    }
    
    public void refreshWorkers() {
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onChildChanged(org.shaolin.bmdp.runtime.ddc.client.api.ZData
     * )
     */
    @Override
    public void onChildChanged(ZData zData) {

        List<String> newWorkers = zData.getChildren();
        logger.info("get workers in init :"+Arrays.toString(newWorkers.toArray()));
        if (newWorkers == null) {
            return;
        }

        List<String> workersTobeRemove = new ArrayList<String>();
        for (String knownWorker : knownWorkers) {
            if (newWorkers.contains(knownWorker)) {
                continue;
            }
            workersTobeRemove.add(knownWorker);
        }

        List<String> workersTobeAdd = new ArrayList<String>();
        for (String newWorker : newWorkers) {
            if (knownWorkers.contains(newWorker)) {
                continue;
            }

            workersTobeAdd.add(newWorker);
        }
        // workersTobeRemoved

        for (String worker : workersTobeRemove) {
            // remove
            removeWorkerNodeInJobs(worker);
        }

        // workersNewlyAdded

        for (String worker : workersTobeAdd) {
            // remove
            addWorkerNodeInJobs(worker);
        }

    }

    private void addWorkerNodeInJobs(String worker) {
        final String workerPath = ZKDistributedJobEngine.NODES_PATH + "/" + worker;
        try {
            if (zookeeper.exists(workerPath, watcher) == null) {
                zookeeper.create(workerPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }
        } catch (KeeperException e) {
            logger.warn("create node error", e);
        } catch (InterruptedException e) {
            logger.warn("create node error", e);
        }

    }

    private void removeWorkerNodeInJobs(String worker) {
        final String workerPath = ZKDistributedJobEngine.NODES_PATH + "/" + worker;
        zookeeper.getData(workerPath, false, new DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                if (rc == KeeperException.Code.OK.intValue())  {                    
                    try {
                        zookeeper.delete(workerPath, stat.getVersion());
                    } catch (Exception e) {
                        logger.warn("exception delete node", e);
                    }
                }
            }
        }, null);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeCreated(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeCreated(ZData zData) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeDelete(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeDelete(ZData arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeUpdate(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeUpdate(ZData zData) {
        // TODO Auto-generated method stub

    }

    public List<String> getKnownWorkers() {
        return knownWorkers;
    }

}
