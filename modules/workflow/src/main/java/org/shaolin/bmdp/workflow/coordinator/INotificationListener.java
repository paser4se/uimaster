package org.shaolin.bmdp.workflow.coordinator;

import org.shaolin.bmdp.workflow.be.INotification;

public interface INotificationListener {

	public void received(INotification message);
	
}
