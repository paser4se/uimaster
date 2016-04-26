/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed;

import java.util.List;

import org.shaolin.bmdp.analyzer.be.IJavaCCJob;

/**
 * @author lizhiwe
 *
 */
public interface DistributedJobEngine {

    public static final String NODE_NAME = "_javacc_job_scheduler_";
    public static final String LEADER = "leader";

    public enum Role {
        LEADER, WORKER
    }

    // 1) elect if current node should be the leader
    
    public void elect();

    // 2)
    
    public void startJob(IJavaCCJob job);
    
    public void stopJob(IJavaCCJob job);
}
