/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.javacc.test.multithread;

//imports
//junit
import org.shaolin.javacc.Expression;
import org.shaolin.javacc.ExpressionEvaluator;
import org.shaolin.javacc.ExpressionParser;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//ooee

/**
 * This test case test variable operation for numbers
 *
 */
public class MultiThreadTest extends TestCase
{
    public static TestSuite suite()
    {
        return new TestSuite(MultiThreadTest.class);
    }
    
    private static Expression expression;
    private static int count = 0;
    
    static
    {
		DefaultParsingContext parsingContext = new DefaultParsingContext();
		parsingContext.setVariableClass("a", MultiThreadTestUtil.class);
		
		try {
			String expressionString = "a.getNo()";
    		expression = ExpressionParser.parse(expressionString, parsingContext);
    	}
    	catch(Exception ex)
    	{
    	}
    }
        
    protected void setUp()
    {
    	
    }
    
    protected void tearDown()
    {
    }
    
    /**
     *  Test array initialization
     */
    public void testCase1() throws Exception
    {
		for (int i=0; i<200; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						DefaultEvaluationContext evaluationContext = new DefaultEvaluationContext();
			    		int aValue = count++;
				    	evaluationContext.initVariable("a");
				    	evaluationContext.setVariableValue("a", new MultiThreadTestUtil(aValue));
				    	Object expressionValue = ExpressionEvaluator.evaluate(expression, evaluationContext);
				    	assertEquals(new Integer(aValue), expressionValue);
				    	System.out.println(expressionValue);
					} catch (Exception e) {
						Assert.fail(e.getMessage());
					}
				}
			}).start();
		}
		Thread.sleep(3000);
    }

}
