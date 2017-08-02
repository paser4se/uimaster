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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.distributed.api.IJobDispatcher;
import org.shaolin.bmdp.analyzer.distributed.api.IJobExecutor;
import org.shaolin.bmdp.runtime.ddc.client.ZooKeeperFactory;
import org.shaolin.bmdp.runtime.ddc.client.api.IZookeeperClient;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;

/**
 * @author lizhiwe
 */
public class ZKDistributedJobEngine implements DistributedJobEngine, ILifeCycleProvider {
    private Logger logger = Logger.getLogger(getClass());

    private ZooKeeper zookeeper;

    public static final String NODES_PATH = IZookeeperClient.ROOT + "/" + NODE_NAME + "/nodes";
    public static final String LEADER_PATH = IZookeeperClient.ROOT + "/" + NODE_NAME + "/leaders";
   // public static final String JOBS_PATH = IZookeeperClient.ROOT + "/" + NODE_NAME + "/jobs";

    private final String[] initPaths = { IZookeeperClient.ROOT,IZookeeperClient.ROOT + "/" + NODE_NAME,NODES_PATH, LEADER_PATH };

    private Role role = Role.WORKER;

    private String name;

    private DefaultWatcher leaderElectionWatcher = null;
    private DefaultWatcher jobWatcher = null;
    private DefaultWatcher workerNodesWatcher = null;

    //private List<String> jobList = new ArrayList<String>();

    private IJobDispatcher leader;
    private IJobExecutor worker;

    /**
     *
     */
    public ZKDistributedJobEngine(ZooKeeper zookeeper, String name) {
        this.zookeeper = zookeeper;

        this.name = name;
        //
        leaderElectionWatcher = new DefaultWatcher(zookeeper);
        leaderElectionWatcher.addListener(new SiblingNodeListener(this));
        ///
        jobWatcher = new DefaultWatcher(zookeeper);
        worker = new WorkerJobExecutor(zookeeper, jobWatcher, NODES_PATH + "/" + name);
        jobWatcher.addListener(new NodeDataListener(worker));
        
        initCfgIfNeed();

    }

    @Override
	public void configService() {
		
	}
    
    @Override
    public void startService() {
        // elect
        elect();
    }

    private void initCfgIfNeed() {
        try {
            for (String path : initPaths) {
                Stat stat = zookeeper.exists(path, false);
                if (stat == null) {
                    zookeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }

        } catch (Exception e) {
            logger.warn("Error:", e);
        }

        // /create host node
        try {
            zookeeper.create(NODES_PATH + "/" + name, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("created node ["+NODES_PATH + "/" + name+"] automatically.");
        } catch (Exception e) {
            logger.warn("Error:", e);
        }

    }

    public void elect() {
        try {
            String path = zookeeper
                    .create(LEADER_PATH + "/"
                            + LEADER, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> children = zookeeper.getChildren(LEADER_PATH, false);

            Collections.sort(children, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int id1 = Integer.parseInt(o1.replace(LEADER, ""));
                    int id2 = Integer.parseInt(o2.replace(LEADER, ""));
                    return id1 - id2;
                }
            });

            int j = path.lastIndexOf('/');

            path = path.substring(j+1);

            int i = children.indexOf(path);
            if (i == -1) {
                throw new RuntimeException("unrecoverable error!");
            } else if (i == (children.size()-1)) {
                //TODO this should be improved!
                setLeader();
            } else {

                // monitor previous sibling node
                String target = children.get(i - 1);

                zookeeper.exists(LEADER_PATH + "/" + target, leaderElectionWatcher);
            }

        } catch (KeeperException | InterruptedException e) {
            logger.warn("Error:", e);
        }
    }


    public void setLeader() {
        role = Role.LEADER;

        logger.info("node ["+name+"] was elected as leader.");
        try {
            workerNodesWatcher = new DefaultWatcher(zookeeper);
            // / 1 set watcher to nodes path
            // / 2 get workers
            leader = new LeaderJobScheduler(zookeeper, workerNodesWatcher);

            ((LeaderJobScheduler) leader).init();

            //load jobs and start to schedule jobs
            logger.info("leader node ["+name+"] start to schedule and dispatch jobs.");
            leader.startService();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean readyToStop() {
        return true;
    }

    @Override
    public void stopService() {
    	leader.stopService();
    }

    @Override
    public void reload() {
    	if (leader != null) {
    		leader.reload();
    	}
    }

    @Override
    public int getRunLevel() {
        return 11;
    }

    @Override
    public void startJob(IJavaCCJob job) {
        leader.startScheduleJob(job);
        
    }

    @Override
    public void stopJob(IJavaCCJob job) {
        leader.cancelScheduleJob(job);
        
    }

}
