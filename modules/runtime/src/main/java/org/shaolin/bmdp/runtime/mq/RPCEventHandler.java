package org.shaolin.bmdp.runtime.mq;

public interface RPCEventHandler {

	public void notifyAcked(String collId, byte[] raw);
	
	public void notifyDiscard(String collId, byte[] raw);
	
}
