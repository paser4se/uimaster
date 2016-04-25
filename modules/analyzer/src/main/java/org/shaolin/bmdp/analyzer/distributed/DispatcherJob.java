/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed;

import java.util.Queue;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.distributed.api.IJobDispatcher;

/**
 * @author lizhiwe
 * management job which would put the business job which should be invoke immediately into the pendingJobQueue.
 */
public class DispatcherJob implements Job {

    private Logger logger = Logger.getLogger(getClass());

    public static final String JOB_DISPATCHER = "_scheduler_";
    public static final String JOB_INFO = "_jobInfo_";

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        IJobDispatcher dispatcher =  (IJobDispatcher)ctx.getMergedJobDataMap().get(JOB_DISPATCHER);
        IJavaCCJob javaCCJob = (IJavaCCJob) ctx.getMergedJobDataMap().get(JOB_INFO);
        dispatcher.dispatchJobs(javaCCJob);
    }

}
