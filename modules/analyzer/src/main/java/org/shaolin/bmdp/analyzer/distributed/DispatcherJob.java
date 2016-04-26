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
