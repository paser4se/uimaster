package org.shaolin.bmdp.runtime.be;

public interface ITaskEntity extends IPersistentEntity {

	public long getTaskId();

	public void setTaskId(long taskId);
	
	public String getSessionId();

	public void setSessionId(String session);
	
}
