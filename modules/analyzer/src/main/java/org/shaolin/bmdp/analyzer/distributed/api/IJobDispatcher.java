/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed.api;

import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;

/**
 * @author lizhiwe
 *
 */
public interface IJobDispatcher<T extends IJavaCCJob> extends ILifeCycleProvider {
    
    public void loadAllJobs();
    
    public void scheduleJobs();
    
    public void dispatchJobs(T jobInfo);
    
    public String getCronExpressionFromJobInfo(T jobInfo) ;
    public String getJobNameFromJobInfo(T jobInfo);
    public String getTriggerNameFromJobInfo(T jobInfo);

    public void startScheduleJob(IJavaCCJob job);
    
    public void cancelScheduleJob(IJavaCCJob job);
    

}
