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
