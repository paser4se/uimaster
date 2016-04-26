/**
 *
 */
package org.shaolin.bmdp.analyzer.distributed;

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
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.distributed.api.IJobDispatcher;

/**
 * @author lizhiwe
 */
public class LeaderJobScheduler implements IJobDispatcher {

    private Logger logger = Logger.getLogger(getClass());

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
            public void processResult(int version, String path, Object ctx, List<String> children) {
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
    }

    @Override
    public void reload() {
        jobQueue.clear();
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        logger.info("--------------------------loading all jobs -------------------------");
        JavaCCJobImpl job = new JavaCCJobImpl();
        job.setEnabled(true);
        // job.setStatus(JavaCCJobStatusType.START);
        List<IJavaCCJob> jobs = AanlysisModel.INSTANCE.searchJavaCCJob(job, null, 0, -1);
        jobQueue.addAll(jobs);

        logger.info("--------------------------totally " + jobs.size() + " jobs loaded -------------------------");

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
        logger.info("--------------------------scheduling job [" + jobInfo.getId() + "] -------------------------");
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
        logger.info("--------------------------dispatching job [" + jobInfo.getId() + "] to [" + workerName + "]");
        notifyWorkerToExecuteJob(workerName, jobInfo.getId());
    }

    private void notifyWorkerToExecuteJob(final String workerName, final long id) {
        zookeeper.getData(ZKDistributedJobEngine.NODES_PATH + "/" + workerName, watcher, new DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                logger.info("--------------------------notifing worker [" + workerName + "] to execute job [" + id
                        + "]");
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
                    zookeeper.setData(path, s.getBytes(), stat.getVersion());
                } catch (KeeperException | InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        logger.warn("error set data", e);
                        return;
                    }
                    switch (((KeeperException) e).code()) {
                    case BADVERSION:
                        notifyWorkerToExecuteJob(workerName, id);
                        break;
                    default:
                        logger.warn("error set data", e);

                    }

                }
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
            } catch (SchedulerException e) {
                logger.warn("Error:", e);
            }
        }

    }

}
