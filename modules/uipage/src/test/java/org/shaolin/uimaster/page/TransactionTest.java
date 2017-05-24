package org.shaolin.uimaster.page;

import org.junit.BeforeClass;
import org.junit.Test;
import org.shaolin.bmdp.exceptions.BusinessOperationException;
import org.shaolin.javacc.exception.EvaluationException;

public class TransactionTest {
	
	@BeforeClass
	public static void setup() {
	}
	
	@Test
	public void testCommit() throws EvaluationException, BusinessOperationException {
		TransOpsExecuteContext transaction = new TransOpsExecuteContext();
		
		transaction.beginTransaction();
		
		System.out.println("TODO:");
		
		transaction.commitTransaction();
	}
	
	@Test
	public void testRollback() throws EvaluationException, BusinessOperationException {
		TransOpsExecuteContext transaction = new TransOpsExecuteContext();
		
		transaction.beginTransaction();
		
		System.out.println("TODO:");
		
		transaction.rollbackTransaction();
	}
}
