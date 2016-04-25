/**
 * 
 */
package org.shaolin.bmdp.analyzer.distributed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.shaolin.bmdp.analyzer.distributed.api.IJobExecutor;
import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * @author lizhiwe
 *
 */
public class NodeDataListener implements DataListener {

  //  private DistributedJobEngine engine;
    
    private IJobExecutor jobExecutor;
    /**
     * 
     */
    public NodeDataListener(IJobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    /* (non-Javadoc)
     * @see org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeUpdate(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeUpdate(ZData data) {
        
        List<String> taskIds = new ArrayList<String>();
        byte[] raw = data.getData();
        
        if (raw != null || raw.length > 0) {
           
            String s = new String(raw);
            String[] ids = s.split(";");
            taskIds.addAll(Arrays.asList(ids));
        }
        
        
        jobExecutor.onJobListUpdate(taskIds);
        

    }

    /* (non-Javadoc)
     * @see org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeDelete(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeDelete(ZData data) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onNodeCreated(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onNodeCreated(ZData data) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.shaolin.bmdp.runtime.ddc.client.api.DataListener#onChildChanged(org.shaolin.bmdp.runtime.ddc.client.api.ZData)
     */
    @Override
    public void onChildChanged(ZData data) {
        // TODO Auto-generated method stub

    }

}
