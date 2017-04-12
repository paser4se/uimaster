package org.shaolin.uimaster.page.flow.nodes;

import org.shaolin.bmdp.datamodel.pagediagram.LogicNodeType;
import org.shaolin.uimaster.page.exception.WebFlowException;
import org.shaolin.uimaster.page.javacc.WebFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartNode extends LogicNode {

	private static Logger logger = LoggerFactory.getLogger(StartNode.class);
	
	public StartNode(LogicNodeType type) {
		super(type);
	}

	public WebNode execute(WebFlowContext inContext) throws WebFlowException {
		if (logger.isInfoEnabled())
			logger.info("execute() start node {}", toString());

		return super.execute(inContext);
	}

}
