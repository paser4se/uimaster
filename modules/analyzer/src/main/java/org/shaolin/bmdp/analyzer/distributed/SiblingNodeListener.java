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

import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * @author lizhiwe
 *
 */
public class SiblingNodeListener implements DataListener {

    private DistributedJobEngine engine;

    /**
     * 
     */
    public SiblingNodeListener(DistributedJobEngine engine) {
        this.engine = engine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeUpdate(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeUpdate(ZData data) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeDelete(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeDelete(ZData data) {

        engine.elect();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeCreated(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeCreated(ZData data) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onChildChanged(org.shaolin.bmdp.runtime.ddc.client.api.ZData
     * )
     */
    @Override
    public void onChildChanged(ZData data) {
        // TODO Auto-generated method stub

    }

}
