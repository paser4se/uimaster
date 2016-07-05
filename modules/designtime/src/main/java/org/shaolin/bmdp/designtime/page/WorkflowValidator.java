package org.shaolin.bmdp.designtime.page;

import org.shaolin.bmdp.datamodel.common.DiagramType;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.workflow.Workflow;
import org.shaolin.bmdp.designtime.tools.GeneratorOptions;
import org.shaolin.bmdp.runtime.entity.EntityAddedEvent;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityUpdatedEvent;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.od.ODContext;

public class WorkflowValidator implements IEntityEventListener<Workflow, DiagramType> {
	
	protected GeneratorOptions option = null;

	private OOEEContext ooeeContext;
	
	private ExpressionType expr;
	
	public WorkflowValidator(GeneratorOptions option) throws ParsingException {
		this.option = option;
		
		ooeeContext = OOEEContextFactory.createOOEEContext();
		DefaultParsingContext pContext = new DefaultParsingContext();
		pContext.setVariableClass("workflow", Workflow.class);
		ooeeContext.setDefaultParsingContext(pContext);
		ooeeContext.setParsingContextObject(ODContext.LOCAL_TAG, pContext);
		StringBuilder sb = new StringBuilder();
		sb.append("\nimport org.shaolin.bmdp.workflow.internal.cache.FlowObject;\n");
		sb.append("import org.shaolin.bmdp.workflow.internal.type.AppInfo;\n");
		sb.append("import org.shaolin.bmdp.workflow.internal.FlowEngine;\n");
		sb.append("import org.shaolin.bmdp.workflow.internal.FlowContainer;\n");
		sb.append("{\n");
		sb.append("  FlowObject flowInfo = new FlowObject(new AppInfo($workflow));\n");
		sb.append("  FlowEngine engine = new FlowEngine(flowInfo.getAppInfo().getName(), new FlowContainer(\"test\"), false);\n");
		sb.append("  engine.init(flowInfo);\n");
		sb.append("}\n");
		
		this.expr = new ExpressionType();
		expr.setExpressionString(sb.toString());
		expr.parse(ooeeContext);
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		
	}

	@Override
	public void notify(EntityAddedEvent<Workflow, DiagramType> event) {
		
	}

	@Override
	public void notify(EntityUpdatedEvent<Workflow, DiagramType> event) {
		String webflow = event.getNewEntity().getEntityName();
		if (webflow.indexOf(option.getBundleName()) == -1) {
			return;
		}
		try {
			Workflow workflow = event.getNewEntity();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("workflow", workflow);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			
			expr.evaluate(ooeeContext);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyLoadFinish(DiagramType diagram) {
		
	}

	@Override
	public void notifyAllLoadFinish() {
		
	}

	@Override
	public Class<Workflow> getEventType() {
		return Workflow.class;
	}
}
