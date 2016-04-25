/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed.api;

import java.util.List;

import org.shaolin.bmdp.analyzer.be.IJavaCCJob;

/**
 * @author lizhiwe
 *
 */
public interface IJobExecutor <T extends IJavaCCJob> {
    
    void init();
    
    void executeJob(T job);
    
    void onJobListUpdate(List<String> jobIds);

}
