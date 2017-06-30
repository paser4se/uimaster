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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.be.JavaCCJobImpl;
import org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.distributed.api.IJobDispatcher;

/**
 * @author lizhiwe
 */
public class LeaderJobScheduler implements IJobDispatcher {

    private static final Logger logger = Logger.getLogger(LeaderJobScheduler.class);

    public static final String DATA = "data";
    public static final String PATH = "path";
    public static final String STAT2 = "Stat";

    // TODO: set properties configuration.
    private final SchedulerFactory scheduleFactory = new StdSchedulerFactory();

    private final Queue<IJavaCCJob> jobQueue = new ConcurrentLinkedQueue<IJavaCCJob>();

    public static final String DEFAULT_GROUP = "default_group";

    private final Scheduler scheduler;

    private WorkersNodeListener workerNodeListener;

    private DefaultWatcher watcher;

    private ZooKeeper zookeeper;

    private final AtomicInteger counter = new AtomicInteger(-1);

    private Map<Long, Trigger> scheduledJobCache = new ConcurrentHashMap<Long, Trigger>();

    /**
     * @throws Exception
     */
    public LeaderJobScheduler(ZooKeeper zookeeper, DefaultWatcher watcher) throws Exception {
        scheduler = scheduleFactory.getScheduler();
        this.watcher = watcher;
        this.zookeeper = zookeeper;
        workerNodeListener = new WorkersNodeListener(zookeeper, watcher);
        this.watcher.addListener(workerNodeListener);

    }

    public void init() {
        workerNodeListener.init();
    }

    @Override
    public void startService() {

        zookeeper.getChildren(ZKDistributedJobEngine.NODES_PATH, watcher, new ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                // TODO Auto-generated method stub
            }

        }, null);

        // / load job information
        loadAllJobs();

        // run scheduler for all jobs
        scheduleJobs();

    }

    @Override
    public boolean readyToStop() {
        return true;
    }

    @Override
    public void stopService() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.warn(e);
        }
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
        }
    }

    @Override
    public void reload() {
        jobQueue.clear();
        this.scheduledJobCache.clear();
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.warn(e);
        }

        loadAllJobs();

        scheduleJobs();

    }

    @Override
    public int getRunLevel() {
        return 10;
    }

    @Override
    public void loadAllJobs() {
        logger.info("loading all jobs.");
        JavaCCJobImpl job = new JavaCCJobImpl();
        job.setEnabled(true);
        job.setStatus(JavaCCJobStatusType.START);
        List<IJavaCCJob> jobs = AanlysisModel.INSTANCE.searchJavaCCJob(job, null, 0, -1);
        jobQueue.addAll(jobs);

        logger.info("totally " + jobs.size() + " jobs loaded.");

    }

    @Override
    public void scheduleJobs() {
        IJavaCCJob jobInfo = null;
        do {
            jobInfo = jobQueue.poll();
            if (jobInfo == null) {
                break;
            }

            scheduleAJob(jobInfo);
        } while (jobInfo != null);

        try {
            scheduler.start();
        } catch (Exception e) {
            logger.warn("", e);
        }

    }

    private void scheduleAJob(IJavaCCJob jobInfo) {
        logger.info("scheduling job [" + jobInfo.getId() + "]");

        if (scheduledJobCache.containsKey(jobInfo.getId())) {
            logger.info("JavaCCJob [" + jobInfo.getId() + "] already scheduled. ignore this schedule request!");
            return;
        }

        JobDetail job = JobBuilder.newJob(DispatcherJob.class)
                .withIdentity(getJobNameFromJobInfo(jobInfo), DEFAULT_GROUP).build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerNameFromJobInfo(jobInfo), DEFAULT_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(getCronExpressionFromJobInfo(jobInfo))).build();

        trigger.getJobDataMap().put(DispatcherJob.JOB_DISPATCHER, this);
        trigger.getJobDataMap().put(DispatcherJob.JOB_INFO, jobInfo);

        try {
            scheduler.scheduleJob(job, trigger);
            scheduledJobCache.put(jobInfo.getId(), trigger);
        } catch (SchedulerException e) {
            logger.warn("error schedule job", e);
        }

    }

    @Override
    public void dispatchJobs(IJavaCCJob jobInfo) {
        counter.compareAndSet(Integer.MAX_VALUE, 0);

        List<String> workers = workerNodeListener.getKnownWorkers();
        int i = counter.incrementAndGet() % workers.size();
        String workerName = workers.get(i);
        if (logger.isDebugEnabled()) {
            logger.debug("dispatching job [" + jobInfo.getId() + "] to [" + workerName + "]");
        }
        notifyWorkerToExecuteJob(workerName, jobInfo);
    }

    private void notifyWorkerToExecuteJob(final String workerName, final IJavaCCJob jobInfo) {
        final String workerPath = ZKDistributedJobEngine.NODES_PATH + "/" + workerName;
        zookeeper.getData(workerPath, watcher, new DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

                if (logger.isDebugEnabled()) {
                    logger.debug("notifing worker [" + workerName + "] to execute job [" + jobInfo.getId() + "],stat ="
                            + stat + "\n rc=" + rc + "\n path=" + path);
                }
                Map<String, Object> context = new HashMap<String, Object>();
                context.put(DATA, data);
                context.put(PATH, path);
                context.put(STAT2, stat);

                Operation operation = Operation.get(rc);
                if (operation != null) {
                    operation.apply(workerName, jobInfo, LeaderJobScheduler.this, context);
                    return;
                }
                logger.warn("Fail notify worker '" + workerName + "' to execute job '" + jobInfo.getId()
                        + "'   errorCode = " + rc);
            }
        }, null);
    }

    @Override
    public String getCronExpressionFromJobInfo(IJavaCCJob jobInfo) {
        return jobInfo.getCronExp();
    }

    @Override
    public String getJobNameFromJobInfo(IJavaCCJob jobInfo) {
        return jobInfo.getId() + "";
    }

    @Override
    public String getTriggerNameFromJobInfo(IJavaCCJob jobInfo) {
        return jobInfo.getId() + "";
    }

    @Override
    public void startScheduleJob(IJavaCCJob job) {
        scheduleAJob(job);
    }

    @Override
    public void cancelScheduleJob(IJavaCCJob job) {
        Trigger trigger = scheduledJobCache.get(job.getId());
        if (trigger != null) {
            try {
                scheduler.unscheduleJob(trigger.getKey());
                scheduledJobCache.remove(job.getId());
            } catch (SchedulerException e) {
                logger.warn("Failed to cancel the job:" + e.getMessage(), e);
            }
        }

    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    enum Operation {
        OK(KeeperException.Code.OK.intValue()) {

            @Override
            void apply(String workerName, IJavaCCJob jobInfo, LeaderJobScheduler schelder, Map<String, Object> ctx) {
                long id = jobInfo.getId();
                byte[] data = (byte[]) ctx.get(DATA);
                Stat stat = (Stat) ctx.get(STAT2);
                String workerPath = (String) ctx.get(PATH);
                try {
                    String s = "";

                    if (data != null && data.length > 0) {
                        s = new String(data);
                    }
                    if (s.length() == 0) {
                        s = id + "";
                    } else {
                        s = ";" + id;
                    }

                    schelder.getZookeeper().setData(workerPath, s.getBytes(), stat.getVersion());
                } catch (KeeperException | InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        logger.warn("error set data", e);
                        return;
                    }
                    switch (((KeeperException) e).code()) {
                    case BADVERSION:
                        schelder.notifyWorkerToExecuteJob(workerName, jobInfo);
                        break;
                    case NONODE:
                        schelder.dispatchJobs(jobInfo);
                    default:
                        logger.warn("error set data", e);

                    }

                } catch (Exception e) {
                    logger.warn("error set data", e);
                }

            }

        },

        BADVERSION(KeeperException.Code.BADVERSION.intValue()) {
            @Override
            void apply(String workerName, IJavaCCJob jobInfo, LeaderJobScheduler schelder, Map<String, Object> ctx) {
                schelder.notifyWorkerToExecuteJob(workerName, jobInfo);
            }
        },
        
        NONODE(KeeperException.Code.NONODE.intValue()) {
            @Override
            void apply(String workerName, IJavaCCJob jobInfo, LeaderJobScheduler schelder, Map<String, Object> ctx) {
                schelder.getWorkerNodeListener().refreshWorkers();
                schelder.dispatchJobs(jobInfo);
            }
        };

        private static final Map<Integer, Operation> lookup = new HashMap<Integer, Operation>();

        static {
            for (Operation c : EnumSet.allOf(Operation.class))
                lookup.put(c.code, c);
        }

        private int code;

        Operation(int code) {
            this.code = code;
        }

        public static Operation get(int code) {
            return lookup.get(code);
        }

        abstract void apply(String workerName, IJavaCCJob jobInfo, LeaderJobScheduler schelder, Map<String, Object> ctx);

    }

    public WorkersNodeListener getWorkerNodeListener() {
        return workerNodeListener;
    }

}
