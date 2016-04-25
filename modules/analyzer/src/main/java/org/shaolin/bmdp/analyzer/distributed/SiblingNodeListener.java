/**
 * 
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
