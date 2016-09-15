package org.shaolin.bmdp.runtime.perf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is used for product's performance log. 
 */
public class PerfLogFactory 
{

	private static final Logger logger = LoggerFactory.getLogger(PerfLogFactory.class);
	
    public void log(LogMessage message) {
    	logger.warn(message.toString());
    }
    
    public void logException(LogMessage message, Exception e) {
    	logger.warn(message.toString(), e);
    }
}