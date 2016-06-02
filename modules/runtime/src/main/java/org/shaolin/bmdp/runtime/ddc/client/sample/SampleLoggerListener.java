package org.shaolin.bmdp.runtime.ddc.client.sample;


import org.apache.log4j.Logger;
import org.shaolin.bmdp.runtime.ddc.client.api.DataListener;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public class SampleLoggerListener implements DataListener {

    private Logger logger = Logger.getLogger(getClass());


    public void onNodeUpdate(ZData data){
    	if (logger.isDebugEnabled()) {
    		logger.debug("znode ["+data.getPath()+"] has data ["+new String(data.getData())+"]");
    	}
    }

    public void onNodeDelete(ZData data){
    	if (logger.isDebugEnabled()) {
    		logger.debug("znode ["+data.getPath()+"] has data ["+new String((data.getData()== null || data.getData().length == 0 ? new byte[] {1}: data.getData()))+"] was deleted");
    	}
    }

    public void onNodeCreated(ZData data){
    	if (logger.isDebugEnabled()) {
    		logger.debug("znode ["+data.getPath()+"] has data ["+new String(data.getData())+"] was created");
    	}
    }

    public void onChildChanged(ZData data){
    	if (logger.isDebugEnabled()) {
    		logger.debug("znode ["+data.getPath()+"] has child changed ["+new String(data.getData())+"] was created");
    	}
    }

}
