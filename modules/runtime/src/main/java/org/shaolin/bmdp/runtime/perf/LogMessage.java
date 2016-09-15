package org.shaolin.bmdp.runtime.perf;

public class LogMessage {

	private int type;
	
	private String moduleName;
	
	private String invokedAPI;
	
	private Exception e;
	
	private String comment;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getInvokedAPI() {
		return invokedAPI;
	}

	public void setInvokedAPI(String invokedAPI) {
		this.invokedAPI = invokedAPI;
	}

	public Exception getException() {
		return e;
	}

	public void setException(Exception e) {
		this.e = e;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
