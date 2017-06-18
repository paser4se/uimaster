package org.shaolin.bmdp.workflow.exception;


public class WorkflowVarException extends WorkflowException {

	private static final long serialVersionUID = 1L;

	public WorkflowVarException(String message) {
		super(message);
	}
	
	public WorkflowVarException(String message, Throwable t) {
		super(message, t);
	}
	
}
