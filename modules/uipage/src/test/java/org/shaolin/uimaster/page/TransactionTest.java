package org.shaolin.uimaster.page;

import org.junit.Test;
import org.shaolin.bmdp.exceptions.BusinessOperationException;
import org.shaolin.javacc.exception.EvaluationException;

public class TransactionTest {
	
	public void setup() {
	}
	
	@Test
	public void test() {}
	
	public void testCommit() throws EvaluationException, BusinessOperationException {
		TransOpsExecuteContext transaction = new TransOpsExecuteContext();
		
		transaction.beginTransaction();
		
		System.out.println("TODO:");
		
		transaction.commitTransaction();
	}
	
	public void testRollback() throws EvaluationException, BusinessOperationException {
		TransOpsExecuteContext transaction = new TransOpsExecuteContext();
		
		transaction.beginTransaction();
		
		System.out.println("TODO:");
		
		transaction.rollbackTransaction();
	}
}
