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
package org.shaolin.uimaster.page.od;

import java.util.Map;

import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.ODObject;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.ODException;

public class ODTabPaneContext extends ODContext 
{
	private final DefaultParsingContext localPContext = new DefaultParsingContext();
	
    public ODTabPaneContext(UserRequestContext htmlContext, OOEEContext ooeeContext)
    {
		super(htmlContext, false);
        
    	DefaultEvaluationContext defaultEContext = new DefaultEvaluationContext();
    	this.setEvaluationContextObject(GLOBAL_TAG, defaultEContext);
    	
    	EvaluationContext evalContext = ooeeContext.getEvaluationContextObject(LOCAL_TAG);
    	try {
	    	evalContext.setVariableValue("odContext", this);
	    	evalContext.setVariableValue("context", htmlContext);
    	} catch (Exception e) {}
		this.setDefaultEvaluationContext(evalContext);
    	this.setEvaluationContextObject(LOCAL_TAG, evalContext);
    	this.setExternalEvaluationContext(this);
    	
    	Map<String, Object> inputVars = ((DefaultEvaluationContext)evalContext).getVariableObjects();
    	if (inputVars != null) {
    		for (Map.Entry<String, Object> entry : inputVars.entrySet()) {
    			if (entry.getValue() != null) {
    				localPContext.setVariableClass(entry.getKey(), entry.getValue().getClass());
    			}
    		}
    	}
    	localPContext.setVariableClass("context", UserRequestContext.class);
    	localPContext.setVariableClass("odContext", ODContext.class);
    	this.setDefaultParsingContext(localPContext);
    	this.setParsingContextObject(ODContext.LOCAL_TAG, localPContext);
    	this.setExternalParseContext(this);
    }
	
	public String evalDataLocale() throws EvaluationException
	{
		return "";
	}
	
	public void executeAllMappings() throws ODException {
		//
	}
	
	public void executeMapping(String name) throws ODException {
		//
	}
	
	public String getUIParamName() {
		return "";
	}
	
	public DefaultParsingContext getLocalPContext() 
    {
		return localPContext;
    }
    
	public ODObject getODObject() 
	{
		throw new UnsupportedOperationException();
	}
	
	public UIFormObject getUIFormObject() {
		throw new UnsupportedOperationException();
	}
}
