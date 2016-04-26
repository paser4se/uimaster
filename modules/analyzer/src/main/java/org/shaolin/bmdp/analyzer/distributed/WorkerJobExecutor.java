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

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.be.JavaCCJobImpl;
import org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.distributed.api.IJobExecutor;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;

/**
 * @author lizhiwe
 *
 */
public class WorkerJobExecutor implements IJobExecutor<IJavaCCJob> {
    private Logger logger = Logger.getLogger(getClass());
    private ZooKeeper zookeeper;

    private List<Long> executingJobList = Collections.synchronizedList(new ArrayList<Long>());

    private ExecutorService executor = new ThreadPoolExecutor(6, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));

    private String path;

    private DefaultWatcher watcher;

    /**
     * 
     */
    public WorkerJobExecutor(ZooKeeper zookeeper, DefaultWatcher watcher, String path) {
        this.zookeeper = zookeeper;
        this.path = path;
        this.watcher = watcher;

        zookeeper.exists(path, watcher, new StatCallback() {

            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                //
            }

        }, null);
    }

    @Override
    public void init() {

    }

    @Override
    public void executeJob(IJavaCCJob job) {
        executingJobList.add(job.getId());
    }

    @Override
    public synchronized void onJobListUpdate(List<String> jobIds) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("Get latest jobIds ["+ Arrays.toString(jobIds.toArray())+"]");
    	}
        for (String id : jobIds) {
            if ("".equals(id)) {
                continue;
            }
            if (executingJobList.contains(Long.parseLong(id))) {
                continue;
            }
            // /new job added
            JavaCCJobImpl job = new JavaCCJobImpl();
            job.setId(Long.parseLong(id));
            final IJavaCCJob task = AanlysisModel.INSTANCE.searchJavaCCJob(job, null, 0, 1).get(0);
            if (task.getStatus() == JavaCCJobStatusType.STOP) {
            	return;
            }
            
            executingJobList.add(task.getId());
          //  logger.info("----get latest jobIds ["+ Arrays.toString(executingJobList.toArray())+"]");
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
                    try {
                    	if (logger.isDebugEnabled()) {
                    		logger.debug("executing job ["+ task.getId()+"]");
                    	}
                        OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
                        DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
                        ooeeContext.setDefaultEvaluationContext(evaContext);
                        ExpressionType expr = new ExpressionType();
                        expr.setExpressionString(task.getScript());
                        expr.parse(ooeeContext);
                        expr.evaluate(ooeeContext);

                        task.setCount(task.getCount() + 1);
                        task.setRealExecutedTime(new Date(System.currentTimeMillis()));
                        //AanlysisModel.INSTANCE.updateWithTx(task);
                        
                       // synchronized( AanlysisModel.INSTANCE) {                            
                            AanlysisModel.INSTANCE.update(task,true);
                       // }
                        if (logger.isDebugEnabled()) {
                    		logger.debug("done execute job ["+ task.getId()+"]");
                        }
                    } catch (Exception e) {
                        logger.warn("Error occurred while executing JavaCC job!", e);
                    }finally {
                        executingJobList.remove(task.getId());
                        notifyJobFinished(task);
                    }

                }

//            });
//        }

    }

    protected void notifyJobFinished(final IJavaCCJob task) {
        zookeeper.getData(path, watcher, new DataCallback() {

            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                try {
                    String txt = new String(data);
                    txt = txt.replace(task.getId() + "", "");
                    txt = txt.replace(";;", ";");
                    if (logger.isDebugEnabled()) {
                    	logger.debug("Notify the leader's job ["+ task.getId()+"] finished");
                    }
                    zookeeper.setData(path, txt.getBytes(), stat.getVersion());
                } catch (KeeperException | InterruptedException e) {
                    logger.warn("error update data", e);
                }

            }

        }, null);

    }

}
