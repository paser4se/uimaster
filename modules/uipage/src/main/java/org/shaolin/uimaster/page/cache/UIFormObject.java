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
package org.shaolin.uimaster.page.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.common.ParamType;
import org.shaolin.bmdp.datamodel.common.VariableType;
import org.shaolin.bmdp.datamodel.page.ArrayPropertyType;
import org.shaolin.bmdp.datamodel.page.BooleanPropertyType;
import org.shaolin.bmdp.datamodel.page.ClickListenerType;
import org.shaolin.bmdp.datamodel.page.ColorPropertyType;
import org.shaolin.bmdp.datamodel.page.ComponentConstraintType;
import org.shaolin.bmdp.datamodel.page.CustomListenerType;
import org.shaolin.bmdp.datamodel.page.EventListenerType;
import org.shaolin.bmdp.datamodel.page.ExpressionPropertyType;
import org.shaolin.bmdp.datamodel.page.FontPropertyType;
import org.shaolin.bmdp.datamodel.page.FunctionCallType;
import org.shaolin.bmdp.datamodel.page.FunctionReconfigurationType;
import org.shaolin.bmdp.datamodel.page.FunctionType;
import org.shaolin.bmdp.datamodel.page.ImagePropertyType;
import org.shaolin.bmdp.datamodel.page.NullPropertyType;
import org.shaolin.bmdp.datamodel.page.NumericPropertyType;
import org.shaolin.bmdp.datamodel.page.OpCallAjaxType;
import org.shaolin.bmdp.datamodel.page.OpInvokeWorkflowType;
import org.shaolin.bmdp.datamodel.page.OpType;
import org.shaolin.bmdp.datamodel.page.PropertyReconfigurationType;
import org.shaolin.bmdp.datamodel.page.PropertyType;
import org.shaolin.bmdp.datamodel.page.PropertyValueType;
import org.shaolin.bmdp.datamodel.page.ReconfigurablePropertyType;
import org.shaolin.bmdp.datamodel.page.ReconfigurableType;
import org.shaolin.bmdp.datamodel.page.ReconfigurableVariableType;
import org.shaolin.bmdp.datamodel.page.ReconfigurationType;
import org.shaolin.bmdp.datamodel.page.ResourceBundlePropertyType;
import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.datamodel.page.TableLayoutConstraintType;
import org.shaolin.bmdp.datamodel.page.TableLayoutType;
import org.shaolin.bmdp.datamodel.page.UIButtonType;
import org.shaolin.bmdp.datamodel.page.UIChartType;
import org.shaolin.bmdp.datamodel.page.UICheckBoxType;
import org.shaolin.bmdp.datamodel.page.UIChoiceType;
import org.shaolin.bmdp.datamodel.page.UIComboBoxType;
import org.shaolin.bmdp.datamodel.page.UIComponentType;
import org.shaolin.bmdp.datamodel.page.UIContainerType;
import org.shaolin.bmdp.datamodel.page.UICustWidgetType;
import org.shaolin.bmdp.datamodel.page.UIEntity;
import org.shaolin.bmdp.datamodel.page.UIFileType;
import org.shaolin.bmdp.datamodel.page.UIFlowDefaultActionType;
import org.shaolin.bmdp.datamodel.page.UIFlowDiagramType;
import org.shaolin.bmdp.datamodel.page.UIFrameType;
import org.shaolin.bmdp.datamodel.page.UIImageType;
import org.shaolin.bmdp.datamodel.page.UILayoutType;
import org.shaolin.bmdp.datamodel.page.UILinkType;
import org.shaolin.bmdp.datamodel.page.UIListType;
import org.shaolin.bmdp.datamodel.page.UIMatrixType;
import org.shaolin.bmdp.datamodel.page.UIMultiChoiceType;
import org.shaolin.bmdp.datamodel.page.UIPage;
import org.shaolin.bmdp.datamodel.page.UIPanelType;
import org.shaolin.bmdp.datamodel.page.UIPreNextPanelType;
import org.shaolin.bmdp.datamodel.page.UIRadioButtonGroupType;
import org.shaolin.bmdp.datamodel.page.UIReferenceEntityType;
import org.shaolin.bmdp.datamodel.page.UISelectComponentType;
import org.shaolin.bmdp.datamodel.page.UISingleChoiceType;
import org.shaolin.bmdp.datamodel.page.UISkinType;
import org.shaolin.bmdp.datamodel.page.UITabPaneItemType;
import org.shaolin.bmdp.datamodel.page.UITabPaneType;
import org.shaolin.bmdp.datamodel.page.UITableActionType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.datamodel.page.UITableType;
import org.shaolin.bmdp.datamodel.page.UITextAreaType;
import org.shaolin.bmdp.datamodel.page.UITextComponentType;
import org.shaolin.bmdp.datamodel.page.UITextFieldType;
import org.shaolin.bmdp.datamodel.page.UIWebTreeType;
import org.shaolin.bmdp.datamodel.page.ValidatorPropertyType;
import org.shaolin.bmdp.datamodel.page.ValidatorsPropertyType;
import org.shaolin.bmdp.datamodel.page.VariableReconfigurationType;
import org.shaolin.bmdp.datamodel.workflow.MissionActionType;
import org.shaolin.bmdp.datamodel.workflow.MissionNodeType;
import org.shaolin.bmdp.runtime.VariableUtil;
import org.shaolin.bmdp.runtime.be.BEUtil;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.html.layout.HTMLCellLayout;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.OpExecuteContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Table;
import org.shaolin.uimaster.page.ajax.TableConditions;
import org.shaolin.uimaster.page.ajax.TreeConditions;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.od.ODContextHelper;
import org.shaolin.uimaster.page.od.mappings.ComponentMappingHelper;
import org.shaolin.uimaster.page.widgets.HTMLDynamicUIItem;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFormObject implements java.io.Serializable
{
	private static Logger logger = LoggerFactory.getLogger(UIFormObject.class);
	
    private static final long serialVersionUID = 4410034672313404291L;

    private String name = null;

    private String desc = null;
    
    private String pageHintLink = null;
    
    private ExpressionType descExpr = null;
    
    private String bodyName = null;

    private Map<String, Map<String, Object>> componentMap = 
    		new HashMap<String, Map<String, Object>>();

    private Map<String, Object> bundleMap = new HashMap<String, Object>();

    private Map<String, Map<String, ExpressionType>> expressionMap = new HashMap<String, Map<String, ExpressionType>>();

    private Map<String, Object> funcMap = new HashMap<String, Object>();

    //App Name, UIPanel Name, UI Widgets.
    private Map<String, List<HTMLDynamicUIItem>> dynamicItems; 
    
    private List<String> workflowActions; 
    
    private Map<String, List<OpType>> callServerSideOpMap = new HashMap<String, List<OpType>>();

    private HTMLCellLayout bodyLayout = null;

	private List<ParamType> variables = null;

	private Set reconfigurableVarSet = new HashSet();

    private Map reconfigurablePropMap = new HashMap();

    private Map reconfigurationMap = new HashMap();

    private Map uiskinMap = new HashMap();

    private Map includeMap = new HashMap();

    private long lastModifyTime;

    private List refereneEntityList = new ArrayList();

    private Map jsIncludeMap = new HashMap();

    private List jsIncludeList = new ArrayList();

    private Map jsMobIncludeMap = new HashMap();

    private List jsMobIncludeList = new ArrayList();

    private Map jsMobAppIncludeMap = new HashMap();

    private List jsMobAppIncludeList = new ArrayList();
    
	public UIFormObject(String name)
    {
        if (logger.isInfoEnabled())
        {
            logger.info("Load form: " + name);
        }
        this.name = name;
        
        load();
    }

    public UIFormObject(String name, UIPage entity)
    {
        if (logger.isInfoEnabled())
        {
            logger.info("Load page ui: " + name);
        }
        this.name = name;
        this.desc = entity.getDescription();
        
        UIEntity uientity = (UIEntity)entity.getUIEntity();
        OOEEContext parsingContext = parseVariable(entity);
        if (entity.getDescriptionExpr() != null) {
        	try {
        		entity.getDescriptionExpr().parse(parsingContext);
        		descExpr = entity.getDescriptionExpr();
        	} catch (ParsingException e) {
        		logger.error("UI Form description parsing error: " + e.getMessage(), e);
        	}
        }
        parseUI(parsingContext, uientity, null);
        HTMLUtil.includeJsFiles(name, jsIncludeMap, jsIncludeList, !WebConfig.skipCommonJs(name));
        HTMLUtil.includeMobJsFiles(name, jsMobIncludeMap, jsMobIncludeList, !WebConfig.skipCommonJs(name));
        HTMLUtil.includeMobAppJsFiles(name, jsMobAppIncludeMap, jsMobAppIncludeList, !WebConfig.skipCommonJs(name));
    }
    
    private void load()
    {
    	UIEntity entity = IServerServiceManager.INSTANCE.getEntityManager()
    			.getEntity(this.name, UIEntity.class);
        OOEEContext parsingContext = parseVariable(entity);
        parseUI(parsingContext, entity, null);
        HTMLUtil.includeJsFiles(name, jsIncludeMap, jsIncludeList, false);
        HTMLUtil.includeMobJsFiles(name, jsMobIncludeMap, jsMobIncludeList, false);
        HTMLUtil.includeMobAppJsFiles(name, jsMobAppIncludeMap, jsMobAppIncludeList, false);
    }
    
    private void loadForPage()
    {
    	UIEntity entity = (UIEntity)IServerServiceManager.INSTANCE.getEntityManager()
    			.getEntity(this.name, UIPage.class).getUIEntity();
        OOEEContext parsingContext = parseVariable(entity);
        parseUI(parsingContext, entity, null);
        HTMLUtil.includeJsFiles(name, jsIncludeMap, jsIncludeList, !WebConfig.skipCommonJs(name));
        HTMLUtil.includeMobJsFiles(name, jsMobIncludeMap, jsMobIncludeList, !WebConfig.skipCommonJs(name));
        HTMLUtil.includeMobJsFiles(name, jsMobAppIncludeMap, jsMobAppIncludeList, !WebConfig.skipCommonJs(name));
    }
    
    private void parseUI(OOEEContext parsingContext, UIEntity entity, Map extraInfo)
    {
    	this.clear();
		if (logger.isDebugEnabled()) {
			logger.debug("parse reconfigurable for form: " + name);
		}
        parseReconfigurable(entity);

		if (logger.isDebugEnabled()) {
			logger.debug("parse body of form: " + name);
		}
        UIContainerType body = entity.getBody();
        bodyName = body.getUIID();
        parseComponent(body, parsingContext);
        bodyLayout = new HTMLCellLayout((UIPanelType)body, this, parsingContext);
        bodyLayout.setContainer(bodyName + "-");
        parseEventHandler(entity, parsingContext);
    }

    private OOEEContext parseVariable(UIPage entity)
    {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			if (entity.getODMapping() == null || entity.getODMapping().getDataEntities() == null) {
				variables = Collections.emptyList();
			} else {
				variables = entity.getODMapping().getDataEntities();
			}
			DefaultParsingContext pContext = ODContextHelper.getParsingContext(variables);
			pContext.setVariableClass("tableCondition", TableConditions.class);
			pContext.setVariableClass("page", AjaxContext.class);
			ooeeContext.setDefaultParsingContext(pContext);
			ooeeContext.setParsingContextObject("$", pContext);
			
			DefaultParsingContext gContext = new DefaultParsingContext();
			gContext.setVariableClass("page", AjaxContext.class);
			ooeeContext.setParsingContextObject("@", gContext);
			
			return ooeeContext;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private OOEEContext parseVariable(UIEntity entity)
    {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			if (entity.getMapping() == null || entity.getMapping().getDataEntities() == null) {
				variables = Collections.emptyList();
			} else {
				variables = entity.getMapping().getDataEntities();
			}
			DefaultParsingContext pContext = ODContextHelper.getParsingContext(variables);
			pContext.setVariableClass("tableCondition", TableConditions.class);
			pContext.setVariableClass("page", AjaxContext.class);
			ooeeContext.setDefaultParsingContext(pContext);
			ooeeContext.setParsingContextObject("$", pContext);
			
			DefaultParsingContext gContext = new DefaultParsingContext();
			gContext.setVariableClass("page", AjaxContext.class);
			ooeeContext.setParsingContextObject("@", gContext);
			
			return ooeeContext;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
    }

    private void parseReconfigurable(UIEntity entity)
    {
        List<ReconfigurableType> reconfigurables = entity.getReconfigurableProperties();
        for (ReconfigurableType reconfigurable: reconfigurables)
        {
            if (reconfigurable instanceof ReconfigurablePropertyType)
            {
                ReconfigurablePropertyType reconfig = (ReconfigurablePropertyType)reconfigurable;
                String compID = reconfig.getComponentId();
                List valueList = (List)reconfigurablePropMap.get(compID);
                if (valueList == null)
                {
                    valueList = new ArrayList();
                    reconfigurablePropMap.put(compID, valueList);
                }
                valueList.add(reconfig);
            }
            else if (reconfigurable instanceof ReconfigurableVariableType)
            {
                reconfigurableVarSet.add(((ReconfigurableVariableType)reconfigurable)
                        .getVarName());
            }
        }
    }

    private void parseReconfiguration(UIReferenceEntityType entity, Map propMap, Map i18nMap,
            Map expMap, OOEEContext parsingContext)
    {
        Map entityReconfiguration = new HashMap();
        List<ReconfigurationType> reconfigurations = entity.getReconfigurations();
        Map eventReconfiguration = new HashMap();
        Set propertyReconfiguration = new HashSet();
        Map variableReconfiguration = new HashMap();
        for (ReconfigurationType reconfiguration: reconfigurations)
        {
            if (reconfiguration instanceof FunctionReconfigurationType)
            {
                FunctionReconfigurationType fr = (FunctionReconfigurationType)reconfiguration;
                eventReconfiguration.put(fr.getOriginFunctionName(), fr.getOverrideFunctionName());
            }
            else if (reconfiguration instanceof PropertyReconfigurationType)
            {
                PropertyReconfigurationType pr = (PropertyReconfigurationType)reconfiguration;
                getAttribute(pr.getPropertyName(), pr.getValue(), propMap, i18nMap, expMap,
                        parsingContext);
                propertyReconfiguration.add(pr.getPropertyName());
            }
            else if (reconfiguration instanceof VariableReconfigurationType)
            {
                VariableReconfigurationType vr = (VariableReconfigurationType)reconfiguration;
                ExpressionType expression = vr.getExpression();
                try
                {
                    expression.parse(parsingContext);
                }
                catch (ParsingException e)
                {
                    logger.error(
                            "Exception occured when pass the value for the reconfig variable: "
                                    + vr.getOriginVarName() + " in form: " + name, e);
                }
                variableReconfiguration.put(vr.getOriginVarName(), expression);
            }
        }
        if (!eventReconfiguration.isEmpty())
        {
            entityReconfiguration.put(HTMLReferenceEntityType.EVENT, eventReconfiguration);
        }
        if (!propertyReconfiguration.isEmpty())
        {
            entityReconfiguration.put(HTMLReferenceEntityType.PROPERTY, propertyReconfiguration);
        }
        if (!variableReconfiguration.isEmpty())
        {
            entityReconfiguration.put(HTMLReferenceEntityType.VARIABLE, variableReconfiguration);
        }

        if (!entityReconfiguration.isEmpty())
        {
            reconfigurationMap.put(entity.getUIID(), entityReconfiguration);
        }
    }

    private void parseEventHandler(UIEntity entity, OOEEContext parsingContext)
    {
        List<FunctionType> eventHandler = entity.getEventHandlers();
        if (eventHandler != null && eventHandler.size() > 0)
        {
            OpExecuteContext opContext = new OpExecuteContext();
            DefaultParsingContext globalPContext = (DefaultParsingContext)parsingContext
                    .getParsingContextObject("@");
            if (globalPContext == null) {
            	globalPContext = new DefaultParsingContext();
            }
            globalPContext.setVariableClass("request", HttpServletRequest.class);
            globalPContext.setVariableClass("page", AjaxContext.class);
            globalPContext.setVariableClass("eventsource", Widget.class);
            opContext.setParsingContextObject("@", globalPContext);

			for (FunctionType func : eventHandler) {
				if (func != null && func.getOps() != null) {
					try {
						List<OpType> ops = func.getOps();
						this.parseEventHandlerInternal(opContext, ops);
					} catch (ParsingException e) {
						logger.error(
								"Exception occured when parse the server operation of event handler: "
										+ func.getFunctionName()
										+ " in form: " + name, e);
					}
				}
			}
        }
    }

    private void parseEventHandlerInternal(OpExecuteContext context, List<OpType> ops)
            throws ParsingException
    {
    	String callAjaxName = null;
    	List<OpType> opsList = new ArrayList<OpType>();
        for (OpType op: ops) {
            if (op instanceof OpCallAjaxType) {
                OpCallAjaxType callAjaxOp = (OpCallAjaxType)op;
                callAjaxName = callAjaxOp.getName();
            	callAjaxOp.parse(context);
            	opsList.add(callAjaxOp);
            } else if (op instanceof OpInvokeWorkflowType) {
            	OpInvokeWorkflowType workflowOp = (OpInvokeWorkflowType)op;
            	callAjaxName = workflowOp.getOperationId();
            	workflowOp.parse(context);
            	opsList.add(workflowOp);
            }
        }
        
        if (opsList.size() == 0 && callAjaxName == null) {
        	return;
        }
        if (opsList.size() > 0 && callAjaxName == null) {
        	throw new IllegalStateException("the name must be configured on the OpCallAjaxType operation node.");
        }
	    if (callServerSideOpMap.containsKey(callAjaxName)) {
	    	throw new IllegalArgumentException("the operation name ("
	    			+ callAjaxName + ") is duplicated in functions.");
	    }
        callServerSideOpMap.put(callAjaxName, opsList);
    }

    private void parseComponent(UIComponentType component,
            OOEEContext parsingContext)
    {
		if (logger.isDebugEnabled()) {
			logger.debug("parse ui widget: " + component.getUIID());
		}

        Map propMap = new HashMap();
        Map i18nMap = new HashMap();
        Map<String, ExpressionType> expMap = new HashMap<String, ExpressionType>();
        Map eventMap = new HashMap();

        propMap.put("type", getComponentType(component.getClass().getName()));

        if (component instanceof UIContainerType)
        {
        	Boolean v = ((UIContainerType)component).isDynamicUI();
        	addAttribute(propMap, "dynamicUI", v != null?String.valueOf(v.booleanValue()):"false", "false");
        	getAttribute("dynamicUIFilter", ((UIContainerType)component).getDynamicUIFilter(), propMap, i18nMap, expMap, null,
                    parsingContext);
            getAttributes(component, propMap, i18nMap, expMap, parsingContext);
            getEventListeners(component.getEventListeners(), eventMap);
            UIContainerType container = (UIContainerType)component;
            addLayoutAttributes(container.getLayout(), propMap);
            List<UIComponentType> components = container.getComponents();
            for (UIComponentType uicomponent: components)
            {
                parseComponent(uicomponent, parsingContext);
            }
        }
        else
        {
            getAttributes(component, propMap, i18nMap, expMap, parsingContext);
            if (component instanceof UIReferenceEntityType)
            {
                String referenceEntity = (String)propMap.get("referenceEntity");
                if (logger.isDebugEnabled())
                {
                    logger.debug("parse reference entity: " + referenceEntity);
                }
                UIFormObject refEntity = HTMLUtil.parseUIForm(referenceEntity);
                includeMap.put(refEntity, Long.valueOf(refEntity.lastModifyTime));
                refereneEntityList.add(referenceEntity);
                if (logger.isDebugEnabled())
                {
                    logger.debug("parse reconfiguration for reference entity: "
                            + referenceEntity);
                }
                parseReconfiguration((UIReferenceEntityType)component, propMap, i18nMap, expMap,
                        parsingContext);
            } 
            else if (component instanceof UIFrameType) 
            {
            	UIFrameType frame = (UIFrameType)component;
            	if (frame.getChunkName() == null || frame.getChunkName().trim().isEmpty()) {
            		throw new IllegalArgumentException("The chunk name is empty, please specify it in " + frame.getUIID());
            	}
            	if (frame.getNodeName() == null || frame.getNodeName().trim().isEmpty()) {
            		throw new IllegalArgumentException("The node name is empty, please specify it in " + frame.getUIID());
            	}
            	propMap.put("_chunkname", frame.getChunkName());
                propMap.put("_nodename", frame.getNodeName());
            }
            else if (component instanceof UIFileType)
            {
            	UIFileType file = (UIFileType)component;
            	if (file.getChangedNotification() != null
            			&& file.getChangedNotification().getExpression() != null) {
            		try {
            			file.getChangedNotification().getExpression().parse(parsingContext);
						propMap.put("refreshExpr", file.getChangedNotification().getExpression());
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tab pane expression: "
                                + component.getUIID() + " in form: " + this.name, e);
					}
            	}
            }
            else if (component instanceof UIImageType)
            {
            	UIImageType image = (UIImageType)component;
            	if (image.getIsGallery() != null && image.getSelectedImageExpr() != null
            			&& image.getSelectedImageExpr().getExpression() != null) {
            		try {
            			DefaultParsingContext localP = (DefaultParsingContext)
                				parsingContext.getParsingContextObject(ODContext.LOCAL_TAG);
            			localP.setVariableClass("selectedImage", String.class);
						image.getSelectedImageExpr().getExpression().parse(parsingContext);
						propMap.put("selectedImageExpr", image.getSelectedImageExpr().getExpression());
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tab pane expression: "
                                + component.getUIID() + " in form: " + this.name, e);
					}
            	}
            	getEventListeners(component.getEventListeners(), eventMap);
            }
            else if (component instanceof UITabPaneType)
            {
                UITabPaneType tabPane = (UITabPaneType)component;
                List<UITabPaneItemType> tabs = tabPane.getTabs();
				for (UITabPaneItemType tab : tabs)
                {
					if (tab.getRefEntity() != null) {
	                    parseComponent(tab.getRefEntity(), parsingContext);
	                    String referenceEntity = tab.getRefEntity().getReferenceEntity()
	                            .getEntityName().trim();
	
	                    UIFormObject refEntity = HTMLUtil.parseUIForm(referenceEntity);
	                    includeMap.put(refEntity, Long.valueOf(refEntity.lastModifyTime));
	                    refereneEntityList.add(referenceEntity);
	
	                    parseReconfiguration(tab.getRefEntity(), propMap,
	                            i18nMap, expMap, parsingContext);
					} else if (tab.getPanel() != null) {
						parseComponent(tab.getPanel(), parsingContext);
					}

                }
				propMap.put("ajaxLoad", tabPane.isAjaxLoad());
                propMap.put("tabPaneItems", tabPane.getTabs());
                if (tabPane.getTabSelectedAction() != null) {
                	try {
						tabPane.getTabSelectedAction().getExpression().parse(parsingContext);
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tab pane expression: "
	                                    + component.getUIID() + " in form: " + this.name, e);
					}
                	propMap.put("selectedAction", tabPane.getTabSelectedAction().getExpression());
                }
            }
            else if (component instanceof UIPreNextPanelType)
            {
            	UIPreNextPanelType preNextPanel = (UIPreNextPanelType)component;
                List<UITabPaneItemType> tabs = preNextPanel.getTabs();
				for (UITabPaneItemType tab : tabs)
                {
					if (tab.getRefEntity() != null) {
	                    parseComponent(tab.getRefEntity(), parsingContext);
	                    String referenceEntity = tab.getRefEntity().getReferenceEntity()
	                            .getEntityName().trim();
	
	                    UIFormObject refEntity = HTMLUtil.parseUIForm(referenceEntity);
	                    includeMap.put(refEntity, Long.valueOf(refEntity.lastModifyTime));
	                    refereneEntityList.add(referenceEntity);
	
	                    parseReconfiguration(tab.getRefEntity(), propMap,
	                            i18nMap, expMap, parsingContext);
					} else if (tab.getPanel() != null) {
						parseComponent(tab.getPanel(), parsingContext);
					}

                }
				propMap.put("ajaxLoad", preNextPanel.isAjaxLoad());
                propMap.put("tabPaneItems", preNextPanel.getTabs());
                if (preNextPanel.getPreviousAction() != null) {
                	try {
						preNextPanel.getPreviousAction().getExpression().parse(parsingContext);
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tab pane expression: "
	                                    + component.getUIID() + " in form: " + this.name, e);
					}
                	propMap.put("previousAction", preNextPanel.getPreviousAction().getExpression());
                }
                if (preNextPanel.getNextAction() != null) {
                	try {
						preNextPanel.getNextAction().getExpression().parse(parsingContext);
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tab pane expression: "
	                                    + component.getUIID() + " in form: " + this.name, e);
					}
                	propMap.put("nextAction", preNextPanel.getNextAction().getExpression());
                }
            }
            else if (component instanceof UIFlowDiagramType)
            {
            	UIFlowDiagramType flow = (UIFlowDiagramType)component;
            	if (flow.getFlowName() != null) {
            		propMap.put("flowName", flow.getFlowName());
            	}
            	if (flow.getLoadFlow() != null) {
            		propMap.put("loadFlowExpr", flow.getLoadFlow().getExpression());
            		propMap.put("loadDateModelExpr", flow.getLoadDateModel().getExpression());
            		try {
						flow.getLoadFlow().getExpression().parse(parsingContext);
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the table expression: "
                                + component.getUIID() + " in form: " + this.name, e);
					}
            		getAttribute("loadFlow", flow.getLoadFlow(), propMap, i18nMap,
                            expMap, "", parsingContext);
            		getAttribute("loadDateModel", flow.getLoadDateModel(), propMap, i18nMap,
                            expMap, "", parsingContext);
            	}
            	UIFlowDefaultActionType defaultActions = flow.getDefaultActions();
				if (defaultActions != null) {
            		LinkedList<UITableActionType> seqList = new LinkedList<UITableActionType>();
            		if (defaultActions.getSaveFlow() != null) {
		    			UITableActionType saveAction = new UITableActionType();
		    			saveAction.setUiid(flow.getUIID() + "_saveFlow");
		        		saveAction.setFunction(defaultActions.getSaveFlow());
		        		saveAction.setIcon("ui-icon-disk");
		        		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
		        		i18nInfo.setBundle("Common");
		        		i18nInfo.setKey("SaveItem");
		        		saveAction.setTitle(i18nInfo);
		        		seqList.add(saveAction);
	    			}
	            	if (defaultActions.getNewNode() != null) {
	            		UITableActionType action = new UITableActionType();
	            		action.setUiid(flow.getUIID() + "_newNode");
	            		action.setFunction(defaultActions.getNewNode());
	            		action.setIcon("ui-icon-document");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("NewItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			if (defaultActions.getOpenNode() != null) {
	    				UITableActionType action = new UITableActionType();
	            		action.setUiid(flow.getUIID() + "_openNode");
	            		action.setFunction(defaultActions.getOpenNode());
	            		action.setIcon("ui-icon-pencil");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("OpenItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			if (defaultActions.getDeleteNode() != null) {
	    				UITableActionType action = new UITableActionType();
	    				action.setUiid(flow.getUIID() + "_deleteNode");
	            		action.setFunction(defaultActions.getDeleteNode());
	            		action.setIcon("ui-icon-trash");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("DeleteItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			if (defaultActions.getRefreshFlow() != null) {
		    			UITableActionType refreshAction = new UITableActionType();
		    			refreshAction.setUiid(flow.getUIID() + "_refreshFlow");
		        		refreshAction.setFunction(defaultActions.getRefreshFlow());
		        		refreshAction.setIcon("ui-icon-refresh");
		        		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
		        		i18nInfo.setBundle("Common");
		        		i18nInfo.setKey("RefreshItem");
		        		refreshAction.setTitle(i18nInfo);
		        		seqList.add(refreshAction);
	    			}
	        		
	        		propMap.put("defaultActionGroup", seqList);
            	}
            	if (flow.getActionGroups() != null && flow.getActionGroups().size() > 0) {
            		propMap.put("actionGroups", flow.getActionGroups());
            	}
            }
            else if (component instanceof UITableType)
            {
            	UITableType table = (UITableType)component;
            	propMap.put("beElememt", table.getBeElement());
            	propMap.put("isAppendRowMode", table.isAppendRowMode());
            	propMap.put("refreshInterval", table.getRefreshInterval());
            	propMap.put("defaultRowSize", table.getDefaultRowSize());
            	propMap.put("isShowBigItem", table.isShowBigItem());
            	propMap.put("isShowActionBar", table.isShowActionBar());
            	propMap.put("isShowFilter", table.isShowFilter());
            	propMap.put("isEditableCell", table.isEditableCell());
            	propMap.put("columns", table.getColumns());
            	propMap.put("queryExpr", table.getQuery().getExpression());
            	if (table.getInitQuery() != null) {
            		propMap.put("initQueryExpr", table.getInitQuery().getExpression());
            	}
            	if (table.getRowFilter() == null) {
            		ExpressionPropertyType p = new ExpressionPropertyType();
            		ExpressionType expr = new ExpressionType();
            		expr.setExpressionString("{return $rowBE.isEnabled();}");
            		p.setExpression(expr);
            		table.setRowFilter(p);
            	}
            	propMap.put("rowFilterExpr", table.getRowFilter().getExpression());
            	if (table.getStats() != null) {
            		propMap.put("statistic", table.getStats());
            	}
            	
				List<UITableColumnType> columns = table.getColumns();
				if (table.isShowBigItem()) {
					UITableColumnType htmlCol = null;
					for (UITableColumnType col : columns) {
						if ("HTMLItem".equalsIgnoreCase(col.getUiType().getType())) {
							htmlCol = col;
							break;
						}
					}
					if (htmlCol == null) {
						throw new IllegalArgumentException("Please specify a HTMLItem column for this table. "
								+ component.getUIID() + " in form: " + this.name);
					}
				}
				for (UITableColumnType col : columns) {
					//TODO: check be field whether exists or not.
					// col.getBeFieldId()
					if(col.getRowExpression() == null) {
						if (col.getBeFieldId() == null) {
							throw new IllegalArgumentException("This column must have a befieldid in " + table.getUIID() + " table.");
						}
						ExpressionType expr = new ExpressionType();
						expr.setExpressionString("$" + ComponentMappingHelper.getIndexedComponentPath(col.getBeFieldId(), false, ""));
						ExpressionPropertyType p = new ExpressionPropertyType();
						p.setExpression(expr);
						col.setRowExpression(p);
					}
					if(col.getUpdateCondition() == null && !"Label".equals(col.getUiType().getType())) {
						if (col.getBeFieldId() == null) {
							throw new IllegalArgumentException("This column must have a befieldid in " + table.getUIID() + " table.");
						}
						ExpressionType expr = new ExpressionType();
						expr.setExpressionString("$" + ComponentMappingHelper.getIndexedComponentPath(col.getBeFieldId(), true, "$value"));
						ExpressionPropertyType p = new ExpressionPropertyType();
						p.setExpression(expr);
						col.setUpdateCondition(p);
					}
				}
            	
            	if (table.getDefaultActions() != null) {
            		LinkedList<UITableActionType> seqList = new LinkedList<UITableActionType>();
	            	if (table.getDefaultActions().getDefaultNewAction() != null) {
	            		UITableActionType action = new UITableActionType();
	            		action.setUiid(table.getUIID() + "_newItem");
	            		action.setFunction(table.getDefaultActions().getDefaultNewAction());
	            		action.setIcon("ui-icon-document");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("NewItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			if (table.getDefaultActions().getDefaultOpenAction() != null) {
	    				UITableActionType action = new UITableActionType();
	            		action.setUiid(table.getUIID() + "_openItem");
	            		action.setFunction(table.getDefaultActions().getDefaultOpenAction());
	            		action.setIcon("ui-icon-pencil");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("OpenItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			/**
	    			if (table.getDefaultActions().getDefaultDisableAction() != null) {
	    				UITableActionType action = new UITableActionType();
	    				action.setUiid(table.getUIID() + "_disableItem");
	            		action.setFunction(table.getDefaultActions().getDefaultDisableAction());
	            		action.setIcon("ui-icon-locked");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("DisableItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			if (table.getDefaultActions().getDefaultEnableAction() != null) {
	    				UITableActionType action = new UITableActionType();
	    				action.setUiid(table.getUIID() + "_enableItem");
	            		action.setFunction(table.getDefaultActions().getDefaultEnableAction());
	            		action.setIcon("ui-icon-unlocked");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("EnableItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}*/
	    			if (table.getDefaultActions().getDefaultDeleteAction() != null) {
	    				UITableActionType action = new UITableActionType();
	    				action.setUiid(table.getUIID() + "_deleteItem");
	            		action.setFunction(table.getDefaultActions().getDefaultDeleteAction());
	            		action.setIcon("ui-icon-trash");
	            		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	            		i18nInfo.setBundle("Common");
	            		i18nInfo.setKey("DeleteItem");
	            		action.setTitle(i18nInfo);
	            		seqList.add(action);
	    			}
	    			UITableActionType refreshAction = new UITableActionType();
	    			refreshAction.setUiid(table.getUIID() + "_refreshItem");
	        		refreshAction.setFunction("refreshTable");
	        		refreshAction.setIcon("ui-icon-refresh");
	        		ResourceBundlePropertyType i18nInfo = new ResourceBundlePropertyType();
	        		i18nInfo.setBundle("Common");
	        		i18nInfo.setKey("RefreshItem");
	        		refreshAction.setTitle(i18nInfo);
	        		seqList.add(refreshAction);
	        		
	        		//TODO: not implemented yet
//					UITableActionType importData = new UITableActionType();
//					importData.setFunction("importData");
//					importData.setIcon("ui-icon-arrowthickstop-1-s");
//					StringPropertyType strProperty = new StringPropertyType();
//					strProperty.setValue("Import Data");
//					importData.setTitle(strProperty);
//					importData.setUiid(table.getUIID() + "_importItem");
//					seqList.add(importData);
					// must be manually configured it.
//					UITableActionType exportData = new UITableActionType();
//					exportData.setFunction("exportData");
//					exportData.setIcon("ui-icon-arrowthickstop-1-n");
//					ResourceBundlePropertyType i18nInfoExport = new ResourceBundlePropertyType();
//	        		i18nInfoExport.setBundle("Common");
//	        		i18nInfoExport.setKey("ExportItem");
//					exportData.setTitle(i18nInfoExport);
//					exportData.setUiid(table.getUIID() + "_exportItem");
//					seqList.add(exportData);
					
	        		propMap.put("defaultActionGroup", seqList);
            	}
            	if (table.getActionGroups() != null && table.getActionGroups().size() > 0) {
            		propMap.put("actionGroups", table.getActionGroups());
            	}
    			
            	propMap.put("selectMode", table.getSelectMode());
            	//parse column expressions
            	try {
            		DefaultParsingContext localP = (DefaultParsingContext)
            				parsingContext.getParsingContextObject(ODContext.LOCAL_TAG);
            		localP.setVariableClass("table", Table.class);
            		localP.setVariableClass("page", AjaxContext.class);
					// for cell value evaluation.
            		Class beClass = null;
            		try {
            			beClass = BEUtil.getBEImplementClass(table.getBeElement().trim());
            		} catch (ClassNotFoundException e) {
            			beClass = Class.forName(table.getBeElement());
            		}
            		localP.setVariableClass("rowBE", beClass);
            		localP.setVariableClass("index", int.class);
            		localP.setVariableClass("formId", String.class);
					localP.setVariableClass("tableCondition",  TableConditions.class);
					
					// for condition update.
					localP.setVariableClass("value",  String.class);
					localP.setVariableClass("filterId",  String.class);
					
					if (table.getInitQuery() != null) {
						table.getInitQuery().getExpression().parse(parsingContext);
					}
					table.getRowFilter().getExpression().parse(parsingContext);
					table.getQuery().getExpression().parse(parsingContext);
					
					for (UITableColumnType col : table.getColumns()) {
						col.getRowExpression().getExpression().parse(parsingContext);
						if (col.getUpdateCondition() != null && col.getUpdateCondition().getExpression() != null) {
							col.getUpdateCondition().getExpression().parse(parsingContext);
						}
						if(col.getComboxExpression() != null) {
							col.getComboxExpression().getExpression().parse(parsingContext);
						}
					}
				} catch (ClassNotFoundException e) {
					logger.error("Exception occured when pass the table expression: "
                                    + component.getUIID() + " in form: " + this.name, e);
				} catch (ParsingException e) {
					logger.error("Exception occured when pass the table expression: "
                                    + component.getUIID() + " in form: " + this.name, e);
				}
            }
            else if (component instanceof UIWebTreeType)
            {
            	UIWebTreeType tree = (UIWebTreeType)component;
            	propMap.put("nodeIcon", tree.getNodeIcon());
            	propMap.put("itemIcon", tree.getItemIcon());
            	propMap.put("opened", tree.isOpened());
            	propMap.put("selectedNode", tree.getSelectNodeEvent());
            	propMap.put("dblselectedNode", tree.getDblselectNodeEvent());
            	propMap.put("addNode", tree.getAddNodeEvent());
            	propMap.put("deleteNode", tree.getDeleteNodeEvent());
            	propMap.put("initExpr", tree.getInitExpression().getExpression());
            	if (tree.getActions() != null) {
            		propMap.put("actions", tree.getActions());
            	}
            	if (tree.getExpandExpression() != null) {
	            	propMap.put("expandExpr", tree.getExpandExpression().getExpression());
	            	
	            	DefaultParsingContext localP = (DefaultParsingContext)
	        				parsingContext.getParsingContextObject(ODContext.LOCAL_TAG);
	        		localP.setVariableClass("selectedNode", String.class);
	        		localP.setVariableClass("treeCondition", TreeConditions.class);
	            	try {
						tree.getExpandExpression().getExpression().parse(parsingContext);
					} catch (ParsingException e) {
						logger.error("Exception occured when pass the tree expanded expression: "
	                            + component.getUIID() + " in form: " + this.name, e);
					}
            	}
            	
            	getEventListeners(component.getEventListeners(), eventMap);
            }
            else if (component instanceof UIChartType) 
            {
            	try {
            		UIChartType chart = (UIChartType) component;
            		DefaultParsingContext localP = (DefaultParsingContext)
            				parsingContext.getParsingContextObject(ODContext.LOCAL_TAG);
            		localP.setVariableClass("page", AjaxContext.class);
            		localP.setVariableClass("condition", Object.class);
					// for cell value evaluation.
            		Class beClass = null;
            		try {
            			beClass = BEUtil.getBEImplementClass(chart.getBeElement());
            		} catch (ClassNotFoundException e) {
            			beClass = Class.forName(chart.getBeElement());
            		}
            		localP.setVariableClass("rowBE", beClass);
					
            		propMap.put("columns", chart.getDatasets());
					if (chart.getQuery() != null) {
						chart.getQuery().getExpression().parse(parsingContext);
						propMap.put("queryExpr", chart.getQuery().getExpression());
					}
					if (chart.getLabels() != null) {
						chart.getLabels().getExpression().parse(parsingContext);
						propMap.put("labelExpr", chart.getLabels().getExpression());
					}
					
					List<UITableColumnType> columns = chart.getDatasets();
					for (UITableColumnType col : columns) {
						if(col.getRowExpression() != null && col.getRowExpression().getExpression() != null) {
							col.getRowExpression().getExpression().parse(parsingContext);
						}
						if(col.getIsVisible() != null && col.getIsVisible().getExpression() != null) {
							col.getIsVisible().getExpression().parse(parsingContext);
						}
					}
				} catch (ClassNotFoundException e) {
					logger.error("Exception occured when pass the chart expression: "
                                    + component.getUIID() + " in form: " + this.name, e);
				} catch (ParsingException e) {
					logger.error("Exception occured when pass the chart expression: "
                                    + component.getUIID() + " in form: " + this.name, e);
				}
            } 
            else if (component instanceof UIMatrixType) 
            {
            	getEventListeners(component.getEventListeners(), eventMap);
            	UIMatrixType matrix = (UIMatrixType) component;
            	try {
	            	if (matrix.getColumns() != null && matrix.getColumns().size() > 0) {
		            	List<UITableColumnType> columns = matrix.getColumns();
						for (UITableColumnType col : columns) {
							if(col.getRowExpression() != null && col.getRowExpression().getExpression() != null) {
								col.getRowExpression().getExpression().parse(parsingContext);
							}
						}
	            	}
	            	if (matrix.getInit() != null) {
		            	matrix.getInit().getExpression().parse(parsingContext);
		            	propMap.put("initExpr", matrix.getInit().getExpression());
	            	}
            	} catch (ParsingException e) {
					logger.error("Exception occured when pass the matrix expression: "
                                    + component.getUIID() + " in form: " + this.name, e);
				}
            }
            else if (component instanceof UICustWidgetType) 
            {
            	propMap.put("custType", ((UICustWidgetType)component).getCustType());
            	getAttribute("init", ((UICustWidgetType)component).getInit(), propMap, i18nMap, expMap,
                        "false", parsingContext);
            }
            else
            {
                getEventListeners(component.getEventListeners(), eventMap);
            }
        }

        UISkinType uiskin = component.getUISkin();
        if (uiskin != null)
        {
        	List<PropertyType> properties = uiskin.getParams();
            for (PropertyType param: properties)
            {
                PropertyValueType paramValue = param.getValue();
                if (paramValue instanceof ExpressionPropertyType)
                {
                    ExpressionType expression = ((ExpressionPropertyType)paramValue)
                            .getExpression();
                    try
                    {
                        expression.parse(parsingContext);
                    }
                    catch (ParsingException e)
                    {
                        logger.error("Exception occured when pass the expression for the uiskin of the component: "
                                        + component.getUIID() + " in form: " + this.name, e);
                    }
                }
            }
            uiskinMap.put(component.getUIID(), uiskin);
        }

        componentMap.put(component.getUIID(), propMap);
        if (!i18nMap.isEmpty())
        {
            bundleMap.put(component.getUIID(), i18nMap);
        }
        if (!expMap.isEmpty())
        {
			if (logger.isTraceEnabled()) {
	            logger.trace("{} ui expression: {}", component.getUIID(), expMap);
			}
            expressionMap.put(component.getUIID(), expMap);
        }
        if (!eventMap.isEmpty())
        {
            funcMap.put(component.getUIID(), eventMap);
        }
        
    }

    private static String getComponentType(String className)
    {
        return className.substring(className.lastIndexOf(".") + 3);
    }

    private static void addLayoutAttributes(UILayoutType layout, Map propMap)
    {
        if (layout instanceof TableLayoutType)
        {
            TableLayoutType tableLayout = (TableLayoutType)layout;
            propMap.put("column", String.valueOf(tableLayout.getColumnWidthWeights().size()));
            propMap.put("row", String.valueOf(tableLayout.getRowHeightWeights().size()));
            propMap.put("spacing", String.valueOf(tableLayout.getSpacing()));
            propMap.put("padding", String.valueOf(tableLayout.getPadding()));
        }
    }

    private void getAttributes(UIComponentType component, Map propMap, Map i18nMap, Map expMap,
            OOEEContext parsingContext)
    {
    	if (component.getSecure() != null) {
    		getAttribute(
                "secure", component.getSecure(), propMap, i18nMap, expMap, "", parsingContext);
    	}
        getAttribute(
                "viewPermission",
                (component.getViewPermission() == null) ? (new ArrayPropertyType()) : (component
                        .getViewPermission()), propMap, i18nMap, expMap, "", parsingContext);
        getAttribute(
                "editPermission",
                (component.getViewPermission() == null) ? (new ArrayPropertyType()) : (component
                        .getViewPermission()), propMap, i18nMap, expMap, "", parsingContext);
        getAttribute("visible", component.getVisible(), propMap, i18nMap, expMap, "true",
                parsingContext);
        getAttribute("editable", component.getEditable(), propMap, i18nMap, expMap, "true",
                parsingContext);
        getAttribute("readOnly", component.getReadOnly(), propMap, i18nMap, expMap, "false",
                parsingContext);
        
        addAttribute(propMap, "preIncludePage", component.getPreIncludePage(), "");
        addAttribute(propMap, "postIncludePage", component.getPostIncludePage(), "");
        addAttribute(propMap, "UIStyle", component.getUIStyle(), "");
        getAttribute("widgetLabel", component.getWidgetLabel(), propMap, i18nMap, expMap, "",
                parsingContext);
        getAttribute("widgetLabelColor", component.getWidgetLabelColor(), propMap, i18nMap, expMap,
                "", parsingContext);
        getAttribute("widgetLabelFont", component.getWidgetLabelFont(), propMap, i18nMap, expMap,
                "", parsingContext);
        getAttribute("initValidation", component.getInitValidation(), propMap, i18nMap, expMap,
                "false", parsingContext);
        getAttribute("validator", component.getValidator(), propMap, i18nMap, expMap, null,
                parsingContext);

        List<PropertyType> properties = component.getProperties();
        for (PropertyType property: properties)
        {
            getAttribute(property.getName(), property.getValue(), propMap, i18nMap,
                    expMap, parsingContext);
        }

        if (component instanceof UITextComponentType)
        {
            getAttribute("text", ((UITextComponentType)component).getText(), propMap, i18nMap,
                    expMap, "", parsingContext);
            getAttribute("allowBlank", ((UITextComponentType)component).getAllowBlank(), propMap,
                    i18nMap, expMap, "true", parsingContext);
            getAttribute("allowBlankText", ((UITextComponentType)component).getAllowBlankText(),
                    propMap, i18nMap, expMap, "", parsingContext);
            getAttribute("lengthText", ((UITextComponentType)component).getLengthText(), propMap,
                    i18nMap, expMap, "", parsingContext);
            getAttribute("minLength", ((UITextComponentType)component).getMinLength(), propMap,
                    i18nMap, expMap, "0", parsingContext);
            getAttribute("regex", ((UITextComponentType)component).getRegex(), propMap, i18nMap,
                    expMap, "", parsingContext);
            getAttribute("regexText", ((UITextComponentType)component).getRegexText(), propMap,
                    i18nMap, expMap, "", parsingContext);
            if (component instanceof UITextFieldType)
            {
                getAttribute("maxLength", ((UITextFieldType)component).getMaxLength(), propMap,
                        i18nMap, expMap, "", parsingContext);
            }
            if (component instanceof UITextAreaType)
            {
                getAttribute("maxLength", ((UITextAreaType)component).getMaxLength(), propMap,
                        i18nMap, expMap, "", parsingContext);
                getAttribute("htmlSupport", ((UITextAreaType)component).getHtmlSupport(), propMap,
                        i18nMap, expMap, "", parsingContext);
            }
            if (component instanceof UILinkType)
            {
            	if (((UILinkType)component).getHref() != null)
            	{
            		addAttribute(propMap, "href", ((UILinkType)component).getHref().getFunctionName());
            	}
            }
        } 
        else if (component instanceof UIChoiceType)
        {
            getAttribute("optionValue", ((UIChoiceType)component).getOptionValue(), propMap,
                    i18nMap, expMap, parsingContext);
            if (component instanceof UISingleChoiceType)
            {
            	if (component instanceof UIRadioButtonGroupType)
                {
            		getAttribute("showProgressBar", ((UIRadioButtonGroupType)component).getShowProgressBar(), 
            				propMap, i18nMap, expMap, parsingContext);
                }
                UISingleChoiceType temp = (UISingleChoiceType)component;
                getAttribute("selectedValueConstraint", temp.getSelectedValueConstraint(), propMap,
                        i18nMap, expMap, parsingContext);
                getAttribute("selectedValueConstraintText", temp.getSelectedValueConstraintText(),
                        propMap, i18nMap, expMap, parsingContext);
                if (component instanceof UIComboBoxType)
                {
                    UIComboBoxType comboBox = (UIComboBoxType)component;
                    getAttribute("allowBlank", comboBox.getAllowBlank(), propMap, i18nMap, expMap,
                            parsingContext);
                    getAttribute("allowBlankText", comboBox.getAllowBlankText(), propMap, i18nMap, expMap,
                            parsingContext);
                }
            }
            else if (component instanceof UIMultiChoiceType)
            {
                UIMultiChoiceType temp = (UIMultiChoiceType)component;
                getAttribute("selectedValuesConstraint", temp.getSelectedValuesConstraint(),
                        propMap, i18nMap, expMap, parsingContext);
                getAttribute("selectedValuesConstraintText",
                        temp.getSelectedValuesConstraintText(), propMap, i18nMap, expMap,
                        parsingContext);
                if (component instanceof UIListType)
                {
                    UIListType list = (UIListType)component;
                    getAttribute("allowBlank", list.getAllowBlank(), propMap, i18nMap, expMap,
                            parsingContext);
                    getAttribute("allowBlankText", list.getAllowBlankText(), propMap, i18nMap, expMap,
                            parsingContext);
                	getAttribute("size", ((UIListType)component).getSize(), propMap, i18nMap, expMap,
                			parsingContext);
                	getAttribute("multiple", ((UIListType)component).getMultiple(), propMap, i18nMap,
                			expMap, parsingContext);
                }
            }
        }
        else if (component instanceof UISelectComponentType)
        {
            getAttribute("label", ((UISelectComponentType)component).getLabel(), propMap, i18nMap,
                    expMap, parsingContext);
            getAttribute("selected", ((UISelectComponentType)component).getSelected(), propMap,
                    i18nMap, expMap, parsingContext);
            if (component instanceof UICheckBoxType)
            {
                UICheckBoxType uicbt = (UICheckBoxType)component;
                getAttribute("mustCheck", uicbt.getCheckedValueConstraint(), propMap, i18nMap,
                        expMap, parsingContext);
                getAttribute("mustCheckText", uicbt.getCheckedValueConstraintText(), propMap,
                        i18nMap, expMap, parsingContext);
            }
        }
        else if (component instanceof UIImageType)
        {
            getAttribute("src", ((UIImageType)component).getSrc(), propMap, i18nMap, expMap,
                    parsingContext);
            getAttribute("isGallery", ((UIImageType)component).getIsGallery(), propMap, i18nMap, expMap,
                    parsingContext);
            getAttribute("showWords", ((UIImageType)component).getShowWords(), propMap, i18nMap, expMap,
                    parsingContext);
        }
        else if (component instanceof UIFileType)
        {
            getAttribute("text", ((UIFileType)component).getText(), propMap, i18nMap, expMap, "",
                    parsingContext);
            getAttribute("isMultiple", ((UIFileType)component).getIsMultiple(), propMap, i18nMap, expMap, "",
                    parsingContext);
            getAttribute("suffix", ((UIFileType)component).getSuffix(), propMap, i18nMap, expMap, "",
                    parsingContext);
            getAttribute("storedPath", ((UIFileType)component).getStoredPath(), propMap, i18nMap, expMap, "",
                    parsingContext);
        }
        else if (component instanceof UIReferenceEntityType)
        {
            if (((UIReferenceEntityType)component).isIsReferenceInterface())
            {
                addAttribute(propMap, "isReferenceInterface", "true");
            }
            addAttribute(propMap, "referenceEntity", ((UIReferenceEntityType)component)
                    .getReferenceEntity().getEntityName().trim());
        }
        else if (component instanceof UITableType)
        {
        	UITableType table = (UITableType)component;
//			getAttribute("query", table.getQuery(), propMap, i18nMap, expMap, "",
//					parsingContext);
//			getAttribute("totalCount", table.getTotalCount(), propMap, i18nMap, expMap, "",
//					parsingContext);
        }
        else if (component instanceof UIWebTreeType)
        {
        	UIWebTreeType tree = (UIWebTreeType)component;
			getAttribute("initValue", tree.getInitExpression(), propMap, i18nMap, expMap, "",
					parsingContext);
        }
        else if (component instanceof UIChartType) 
        {
        	UIChartType chart = (UIChartType) component;
        	chart.getDatasets();
        }
    }

    private void getAttribute(String name, PropertyValueType propertyValue, Map propMap,
            Map i18nMap, Map expMap, OOEEContext parsingContext)
    {
        getAttribute(name, propertyValue, propMap, i18nMap, expMap, null, parsingContext);
    }

    private void getAttribute(String name, PropertyValueType propertyValue, Map propMap,
            Map i18nMap, Map expMap, String defaultValue, OOEEContext parsingContext)
    {
        if (propertyValue == null)
        {
            return;
        }

        if (propertyValue instanceof StringPropertyType)
        {
            addAttribute(propMap, name, ((StringPropertyType)propertyValue).getValue(),
                    defaultValue);
        }
        else if (propertyValue instanceof ResourceBundlePropertyType)
        {
            addAttribute(i18nMap, name, ((ResourceBundlePropertyType)propertyValue).getBundle().trim(),
                    defaultValue);
            addAttribute(propMap, name, ((ResourceBundlePropertyType)propertyValue).getKey().trim(),
                    defaultValue);
        }
        else if (propertyValue instanceof NumericPropertyType)
        {
            addAttribute(propMap, name, ((NumericPropertyType)propertyValue).getValue(),
                    defaultValue);
        }
        else if (propertyValue instanceof BooleanPropertyType)
        {
            addAttribute(propMap, name,
                    String.valueOf(((BooleanPropertyType)propertyValue).isValue()), defaultValue);
        }
        else if (propertyValue instanceof FontPropertyType)
        {
            FontPropertyType fontProperty = (FontPropertyType)propertyValue;
            String fontType = fontProperty.getFontName() + "," + fontProperty.getFontSize() + ","
                    + fontProperty.getFontStyle();
            addAttribute(propMap, name, fontType, defaultValue);
        }
        else if (propertyValue instanceof ColorPropertyType)
        {
            ColorPropertyType colorProperty = (ColorPropertyType)propertyValue;
            String value = colorProperty.getRed() + "," + colorProperty.getGreen() + ","
                    + colorProperty.getBlue();
            addAttribute(propMap, name, value, defaultValue);
        }
        else if (propertyValue instanceof ImagePropertyType)
        {
            addAttribute(propMap, name, ((ImagePropertyType)propertyValue).getLocation(),
                    defaultValue);
        }
        else if (propertyValue instanceof ArrayPropertyType)
        {// a special way to handle this type
        	List<PropertyValueType> properties = ((ArrayPropertyType)propertyValue).getProperties();
            getListAttribute(name, properties, propMap, i18nMap, expMap, parsingContext);
        }
        else if (propertyValue instanceof ExpressionPropertyType)
        {
            ExpressionType expression = ((ExpressionPropertyType)propertyValue).getExpression();
            try
            {
                expression.parse(parsingContext);
            }
            catch (ParsingException e)
            {
                logger.error("Exception occured when pass the expression for the attribute: "
                                + name + " in form: " + this.name, e);
            }
            expMap.put(name, expression);
        }
        else if (propertyValue instanceof NullPropertyType)
        {
            addAttribute(propMap, name, "", defaultValue);
        }
        else if (propertyValue instanceof ValidatorPropertyType)
        {
            propMap.put(name, propertyValue);
            ValidatorPropertyType property = (ValidatorPropertyType)propertyValue;
            if (property.getI18NMsg() != null)
            {
                getAttribute("validator-msg--", property.getI18NMsg(), propMap, i18nMap, expMap,
                        "", parsingContext);
            }
        }
        else if (propertyValue instanceof ValidatorsPropertyType)
        {
            propMap.put("validators", propertyValue);
            ValidatorsPropertyType validatorProperty = (ValidatorsPropertyType)propertyValue;
            List<ValidatorPropertyType> validators = validatorProperty.getValidators();
            for (ValidatorPropertyType validator: validators)
            {
                if (validator.getI18NMsg() != null)
                {
                    getAttribute("validator-msg--", validator.getI18NMsg(), propMap,
                            i18nMap, expMap, "", parsingContext);
                }
            }
        }
        else
        {
            logger.error("unsupport property type.");
        }
    }

    private void getListAttribute(String name, List<PropertyValueType> properties, Map propMap,
            Map i18nMap, Map expMap, OOEEContext parsingContext)
    {
        int length = properties.size();
        List valueList = new ArrayList();
        List bundleList = new ArrayList();
        List expList = new ArrayList();
        ExpressionType[] exps = new ExpressionType[length];
        boolean hasI18N = false;
        boolean hasExp = false;
        for (PropertyValueType property: properties)
        {
            if (property instanceof StringPropertyType)
            {
            	valueList.add(((StringPropertyType)property).getValue());
            }
            else if (property instanceof ResourceBundlePropertyType)
            {
                hasI18N = true;
                valueList.add(((ResourceBundlePropertyType)property).getKey());
                bundleList.add(((ResourceBundlePropertyType)property).getBundle());
            }
            else if (property instanceof ExpressionPropertyType)
            {
                hasExp = true;
                try
                {
                	ExpressionPropertyType p = ((ExpressionPropertyType)property);
                	expList.add(p);
                	
                	p.getExpression().parse(parsingContext);
                }
                catch (ParsingException e)
                {
                    logger.error("Exception occured when pass the expression for the list attribute: "
                                    + name + " in form: " + this.name, e);
                }
            }
            else
            {
                logger.error("list's value type should not be " + property.getClass().getName());
            }
        }
        
        propMap.put(name, valueList);
        if (hasI18N)
        {
            i18nMap.put(name, bundleList);
        }
        if (hasExp)
        {
            expMap.put(name, expList);
        }
    }

    private void addAttribute(Map attrMap, String name, String value, String defaultValue)
    {
        if (value == null || value.equals(defaultValue))
        {
            return;
        }
        attrMap.put(name, value);
    }

    private void addAttribute(Map attrMap, String name, String value)
    {
        if (value == null)
        {
            return;
        }

        attrMap.put(name, value);
    }

    private void getEventListeners(List<EventListenerType> eventListeners, Map eventMap)
    {
        for (EventListenerType eventListener: eventListeners)
        {
            String event;
            if (eventListener instanceof CustomListenerType)
            {
                event = ((CustomListenerType)eventListener).getEventName();
            }
            else
            {
                String className = eventListener.getClass().getName();
                event = className.substring(className.lastIndexOf(".") + 1);
            }
            String handler = eventListener.getHandler().getFunctionName();
            addAttribute(eventMap, getEventName(event), handler);
        }
    }

    private static String getEventName(String event)
    {
        String eventName;
        if ("ClickListenerType".equals(event))
        {
            eventName = "onclick";
        }
        else if ("DblClickListenerType".equals(event))
        {
            eventName = "ondblclick";
        }
        else if ("MouseDownListenerType".equals(event))
        {
            eventName = "onmousedown";
        }
        else if ("MouseUpListenerType".equals(event))
        {
            eventName = "onmouseup";
        }
        else if ("MouseOverListenerType".equals(event))
        {
            eventName = "onmouseover";
        }
        else if ("MouseOutListenerType".equals(event))
        {
            eventName = "onmouseout";
        }
        else if ("KeyPressListenerType".equals(event))
        {
            eventName = "onkeypress";
        }
        else if ("KeyDownListenerType".equals(event))
        {
            eventName = "onkeydown";
        }
        else if ("KeyUpListenerType".equals(event))
        {
            eventName = "onkeyup";
        }
        else if ("ItemChangeListenerType".equals(event))
        {
            eventName = "onchange";
        }
        else if ("BlurListenerType".equals(event))
        {
            eventName = "onblur";
        }
        else
        {
            eventName = event;
        }
        return eventName;
    }

    private void clear()
    {
        componentMap = new HashMap();
        bundleMap = new HashMap();
        expressionMap = new HashMap();
        funcMap = new HashMap();
        callServerSideOpMap = new HashMap();
        variables = null;
        reconfigurableVarSet.clear();
        reconfigurablePropMap.clear();
        reconfigurationMap.clear();
        uiskinMap.clear();
        includeMap.clear();
        refereneEntityList.clear();
        jsIncludeMap.clear();
        jsIncludeList.clear();
        jsMobIncludeMap.clear();
        jsMobIncludeList.clear();
        jsMobAppIncludeMap.clear();
        jsMobAppIncludeList.clear();
    }

    public IUISkin getUISkinObj(String UIID, VariableEvaluator ee, HTMLWidgetType htmlComponent)
    {
        if (uiskinMap.containsKey(UIID))
        {
            return HTMLUtil.getUISkinObj((UISkinType)uiskinMap.get(UIID), ee);
        }
        else
        {
            return HTMLUtil.getSystemUISkinObj(htmlComponent);
        }
    }

    public Map getReconfigurationMap(String referenceEntityID, VariableEvaluator ee)
    {
        Map entityReconfiguration = null;
        if (reconfigurationMap.containsKey(referenceEntityID))
        {
            entityReconfiguration = new HashMap();
            entityReconfiguration.putAll((Map)reconfigurationMap.get(referenceEntityID));

            Map variableReconfiguration = (Map)entityReconfiguration
                    .get(HTMLReferenceEntityType.VARIABLE);
            if (variableReconfiguration != null)
            {
                Map tempVariableReconfiguration = new HashMap();
                entityReconfiguration.put(HTMLReferenceEntityType.VARIABLE,
                        tempVariableReconfiguration);

                Iterator it = variableReconfiguration.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry)it.next();
                    String key = (String)entry.getKey();
                    ExpressionType expression = (ExpressionType)entry.getValue();
                    try
                    {
                        Object value = expression.evaluate(ee.getExpressionContext());
                        tempVariableReconfiguration.put(key, value);
                    }
                    catch (EvaluationException e)
                    {
                        logger.error("Exception occured when evaluate the value of the reconfig variable: "
                                        + key + " in form: " + name, e);
                    }
                }
            }
        }
        return entityReconfiguration;
    }

    public void parseReferenceEntity(Map entityMap)
    {
        Iterator iterator = refereneEntityList.iterator();
        while (iterator.hasNext())
        {
            String entityName = (String)iterator.next();
            if (!entityMap.containsKey(entityName))
            {
                UIFormObject entityObj = HTMLUtil.parseUIForm(entityName);
                entityObj.parseReferenceEntity(entityMap);
                entityMap.put(entityName, entityObj);
            }
        }
    }

    public List<OpType> getEventHandler(String opName)
    {
        List<OpType> ops = callServerSideOpMap.get(opName);
        if (ops != null) {
        	return ops;
        }
        return null;
    }

    public DefaultParsingContext getVariablePContext()
    {
        DefaultParsingContext pContext = new DefaultParsingContext();
        if (variables != null)
        {
            for (VariableType var: variables)
            {
                Class clazz = VariableUtil.getVariableClass(var);
                pContext.setVariableClass(var.getName(), clazz);
            }
        }
        return pContext;
    }

    public String getName()
    {
        return this.name;
    }
    
    public String getDescription() 
    {
    	if (desc != null && desc.trim().length() > 0) {
    		return desc;
    	}
    	return this.name;
    }
    
    public ExpressionType getDescriptionExpr() {
    	return descExpr;
    }
    
    public Map getComponentProperty(String componentID)
    {
        return componentMap.get(componentID);
    }
    
    public Iterator<String> getAllComponentID()
    {
        return componentMap.keySet().iterator();
    }

    public Map getComponentEvent(String componentID)
    {
        return (Map)funcMap.get(componentID);
    }

    public Map getComponentI18N(String componentID)
    {
        return (Map)bundleMap.get(componentID);
    }

    public Map<String, ExpressionType> getComponentExpression(String componentID)
    {
        return expressionMap.get(componentID);
    }
    
	public void addWorkflowAction(String eventConsumer, MissionNodeType node, String nodeInfo) throws ParsingException {
		if (workflowActions == null) {
			workflowActions = new ArrayList();
		} 
		for (MissionActionType action : node.getUiActions()) {
			workflowActions.add(action.getActionName());
		}

		// internal refresh.
		UIEntity entity = null;
		if (PageCacheManager.isUIPage(this.name)) {
			entity = (UIEntity)IServerServiceManager.INSTANCE.getEntityManager()
    			.getEntity(this.name, UIPage.class).getUIEntity();
		} else {
			entity = IServerServiceManager.INSTANCE.getEntityManager()
	    			.getEntity(this.name, UIEntity.class);
		}
		
		boolean hasActionPanel = false;
		List<UIComponentType> panelList = entity.getBody().getComponents();
		for (UIComponentType panel : panelList) {
			if ("actionPanel".equals(panel.getUIID())) {
				boolean existWfActionPanel = false;
				UIPanelType actionPanel = (UIPanelType) panel;
				UIPanelType wfactionPanel = new UIPanelType();
				wfactionPanel.setUIID("wfactions");
				for (UIComponentType component : actionPanel.getComponents()) {
					if (component.getUIID().equals("wfactions")) {
						wfactionPanel = (UIPanelType)component;
						existWfActionPanel = true;
						break;
					}
				}
				
				for (MissionActionType action : node.getUiActions()) {
					UIButtonType button = new UIButtonType();
					button.setUIID(action.getActionName());
					ExpressionPropertyType property = new ExpressionPropertyType();
					ExpressionType expr = new ExpressionType();
					expr.setExpressionString("import org.shaolin.bmdp.runtime.security.UserContext; "
							+ "\n{ return UserContext.hasRole(\"" + node.getParticipant().getPartyType() + "\");}");
					if (action.getFilter() != null && action.getFilter().getExpressionString() != null) {
						expr = action.getFilter();
					}
					property.setExpression(expr);
					button.setVisible(property);
					ExpressionPropertyType property1 = new ExpressionPropertyType();
					ExpressionType expr1 = new ExpressionType();
					expr1.setExpressionString("import org.shaolin.uimaster.page.security.UserContext; \n"
							+ "import org.shaolin.bmdp.runtime.AppContext; \n"
							+ "import org.shaolin.bmdp.workflow.coordinator.ICoordinatorService; \n"
							+ "\n{ "
							+ "\n ICoordinatorService service = (ICoordinatorService)AppContext.get().getService(ICoordinatorService.class); "
							+ "\n return service.isTaskExecutedOnNode($beObject.getTaskId(), \"" + nodeInfo + "\");"
							+ "\n}");
					property1.setExpression(expr1);
					if (!node.isMultipleInvoke()) {
						button.setReadOnly(property1);
					}
					StringPropertyType originalStr = (StringPropertyType)button.getText();
					if (originalStr == null) {
						originalStr = new StringPropertyType();
						originalStr.setValue(action.getActionText());
						button.setText(originalStr);
					}
					button.setUIStyle("uimaster_workflow_action");
					ClickListenerType clickListener = new ClickListenerType();
					FunctionCallType func = new FunctionCallType();
					func.setFunctionName("invokeDynamicFunction(this, '" + action.getActionName() + "')");
					clickListener.setHandler(func);
					button.getEventListeners().add(clickListener);
					
					if (wfactionPanel.getLayout() == null) {
						TableLayoutType tableLayout = new TableLayoutType();
						tableLayout.getColumnWidthWeights().add(0.0);
						tableLayout.getRowHeightWeights().add(0.0);
						wfactionPanel.setLayout(tableLayout);
					} else {
						TableLayoutType tableLayout = (TableLayoutType)wfactionPanel.getLayout();
						tableLayout.getColumnWidthWeights().add(0.0);
					}
					ComponentConstraintType constraint = new ComponentConstraintType();
					constraint.setComponentId(action.getActionName());
					TableLayoutConstraintType position = new TableLayoutConstraintType();
					position.setX(wfactionPanel.getLayoutConstraints().size());
					position.setY(0);
					constraint.setConstraint(position);
					//check duplication.
					int index = -1;
					List<UIComponentType> uiList = wfactionPanel.getComponents();
					for (int i=0; i <uiList.size(); i++) {
						if (uiList.get(i).getUIID().endsWith(action.getActionName())) {
							index = i;
							break;
						}
					}
					if (index == -1) {
						wfactionPanel.getComponents().add(button);
						wfactionPanel.getLayoutConstraints().add(constraint);
					} else {
						wfactionPanel.getComponents().set(index, button);
						wfactionPanel.getLayoutConstraints().set(index, constraint);
					}
					
					FunctionType function = new FunctionType();
					function.setNeedAlert(Boolean.TRUE);
					function.setNeedConstraint(Boolean.TRUE);
					function.setFunctionName(action.getActionName());
					
					OpInvokeWorkflowType op = new OpInvokeWorkflowType();
					op.setEventConsumer(eventConsumer);
					op.setExpression(action.getExpression());
					op.setPartyType(node.getParticipant().getPartyType());
					op.setOperationId(action.getActionName());
					op.setAdhocNodeName(node.getName());
					function.getOps().add(op);
					
					//check duplication.
					index = -1;
					List<FunctionType> funcList = entity.getEventHandlers();
					for (int i=0; i <funcList.size(); i++) {
						if (funcList.get(i).getFunctionName().endsWith(action.getActionName())) {
							index = i;
							break;
						}
					}
					if (index == -1) {
						entity.getEventHandlers().add(function);
					} else {
						entity.getEventHandlers().set(index, function);
					}
				}
				if (!existWfActionPanel) {
					TableLayoutType tableLayout = (TableLayoutType)actionPanel.getLayout();
					tableLayout.getColumnWidthWeights().add(0.0);
					ComponentConstraintType wfconstraint = new ComponentConstraintType();
					wfconstraint.setComponentId(wfactionPanel.getUIID());
					TableLayoutConstraintType wfposition = new TableLayoutConstraintType();
					wfposition.setX(0);
					wfposition.setY(0);
					wfconstraint.setConstraint(wfposition);
					actionPanel.getLayoutConstraints().add(0, wfconstraint);
					int count = 0; //reposition.
					actionPanel.getComponents().add(0, wfactionPanel);
					for (ComponentConstraintType c : actionPanel.getLayoutConstraints()) {
						((TableLayoutConstraintType)c.getConstraint()).setX(count++);
					}
				}
				// find ok button if has.
				for (UIComponentType b : actionPanel.getComponents()) {
					if ("okbtn".equals(b.getUIID())) {
						ExpressionPropertyType property2 = new ExpressionPropertyType();
						ExpressionType expr2 = new ExpressionType();
						expr2.setExpressionString("import org.shaolin.uimaster.page.security.UserContext; \n"
								+ "import org.shaolin.bmdp.runtime.AppContext; \n"
								+ "import org.shaolin.bmdp.workflow.coordinator.ICoordinatorService; \n"
								+ "\n{ "
								+ "\n ICoordinatorService service = (ICoordinatorService)AppContext.get().getService(ICoordinatorService.class); "
								+ "\n return service.isPendingTask($beObject.getTaskId()); "
								+ "\n}");
						property2.setExpression(expr2);
						b.setReadOnly(property2);
					}
				}
				
				hasActionPanel = true;
				break;
			}
		}
		if (!hasActionPanel) {
			logger.info("Workflow action should to be added on Action Panel in general, a dynamic event added!");
			for (MissionActionType action : node.getUiActions()) {
				FunctionType function = new FunctionType();
				function.setNeedAlert(Boolean.TRUE);
				function.setFunctionName(action.getActionName());
				OpInvokeWorkflowType op = new OpInvokeWorkflowType();
				op.setEventConsumer(eventConsumer);
				op.setExpression(action.getExpression());
				op.setPartyType(node.getParticipant().getPartyType());
				op.setOperationId(action.getActionName());
				op.setAdhocNodeName(node.getName());
				function.getOps().add(op);
				
				int index = -1;
				List<FunctionType> funcList = entity.getEventHandlers();
				for (int i=0; i <funcList.size(); i++) {
					if (funcList.get(i).getFunctionName().endsWith(action.getActionName())) {
						index = i;
						break;
					}
				}
				if (index == -1) {
					entity.getEventHandlers().add(function);
				} else {
					entity.getEventHandlers().set(index, function);
				}
			}
		}
		
		logger.info("reload form {} due to workflow customization", this.name);
		if (PageCacheManager.isUIPage(this.name)) {
			loadForPage();
		} else {
			load();
		}
	}
    
    public void clearWorkflowActions() {
		if (this.workflowActions != null && this.workflowActions.size() > 0) {
			UIEntity entity = IServerServiceManager.INSTANCE.getEntityManager()
					.getEntity(this.name, UIEntity.class);
			List<UIComponentType> panelList = entity.getBody().getComponents();
			for (UIComponentType panel : panelList) {
				if ("actionPanel".equals(panel.getUIID())) {
					logger.info("remove workflow action {} from {}", panel.getUIID(), this.name);
					UIPanelType actionPanel = (UIPanelType) panel;
					for (UIComponentType comp : actionPanel.getComponents()) {
						if ("wfactions".equals(comp.getUIID())) {
							actionPanel.getComponents().remove(comp);
							break;
						}
					}
					((TableLayoutType)actionPanel.getLayout()).getColumnWidthWeights().remove(0);
					for (ComponentConstraintType constraint : actionPanel.getLayoutConstraints()) {
						if ("wfactions".equals(constraint.getComponentId())) {
							actionPanel.getLayoutConstraints().remove(constraint);
							break;
						}
					}
					int count = 0;
					for (ComponentConstraintType ct: actionPanel.getLayoutConstraints()) {
						((TableLayoutConstraintType)ct.getConstraint()).setX(count ++);
					}
				}
			}
			this.workflowActions.clear();
		}
		
	}
    
    public void addDynamicHints(String uiPanel, String uiwidget, String desc, String url) {
    	Map prop = getComponentProperty(uiwidget);
    	if (prop == null) {
    		logger.warn("UI widget does not exist! {}", uiwidget);
    		return;
    	}
    	if (logger.isDebugEnabled()) {
    		logger.debug("Add dynamic hints into UIEntity: {}, uiwidget: {}", new Object[]{this.name, uiwidget});
    	}
    	prop.put("hints", url);
    	prop.put("hintsDesc", desc);
    }
    
    public void addDynamicLink(String uiPanel, String uiwidget, String linkInfo, String targetInfo) {
    	Map prop = getComponentProperty(uiwidget);
    	if (prop == null) {
    		logger.warn("UI widget does not exist! {}", uiwidget);
    		return;
    	}
    	if (logger.isDebugEnabled()) {
    		logger.debug("Add dynamic link into UIEntity: {}, uiwidget: {}", new Object[]{this.name, uiwidget});
    	}
    	prop.put("dtargetInfo", targetInfo);
    	prop.put("dlinkInfo", linkInfo);
    }
    
    public void addDynamicPageHints(String linkInfo) {
    	this.pageHintLink = linkInfo;
    }
    
    public String getPageHintLink() {
    	return this.pageHintLink;
    }
    
    public void addDynamicItem(HTMLDynamicUIItem item) throws EntityNotFoundException, 
		ParsingException, ClassNotFoundException {
    	if (this.dynamicItems == null) {
    		this.dynamicItems = new HashMap<String, List<HTMLDynamicUIItem>>();
    	}
    	
		if (this.dynamicItems.containsKey(item.getUipanel())) {
			this.dynamicItems.get(item.getUipanel()).add(item);
		} else {
			ArrayList<HTMLDynamicUIItem> items = new ArrayList<HTMLDynamicUIItem>();
			items.add(item);
			this.dynamicItems.put(item.getUipanel(), items);
		}
	}
	
	public List<HTMLDynamicUIItem> getDynamicItems(String panelId, String filter) {
		if (this.dynamicItems == null) {
			return Collections.emptyList();
		}
		List<HTMLDynamicUIItem> items = this.dynamicItems.get(panelId);
		if (filter == null || filter.isEmpty()) {
			return items;
		}
		List<HTMLDynamicUIItem> temps = new ArrayList<HTMLDynamicUIItem>();
		for (HTMLDynamicUIItem i : items) {
			if (i.getFilter() == null || i.getFilter().length() == 0 || i.getFilter().equals(filter)) {
				temps.add(i);
			}
		}
		return temps;
	}
	
	public void clearDynamicItems() {
		if (this.dynamicItems != null) {
			this.dynamicItems.clear();
		}
	}
	
	public boolean hasDynamicItems() {
		if (this.dynamicItems == null) {
			return false;
		}
		return this.dynamicItems.size() > 0;
	}
	
	public void addStatsAction(UITableStatsType uiStatsType) {
		if (componentMap.containsKey(uiStatsType.getUiid())) {
			Map<String, Object> propMap = componentMap.get(uiStatsType.getUiid());
			propMap.put("statistic", uiStatsType);
			
			boolean added = false;
			List<UITableActionType> actions = ((List<UITableActionType>)propMap.get("defaultActionGroup"));
			for (UITableActionType a : actions) {
				if (a.getUiid().equals(uiStatsType.getUiid() + "_statsItem")) {
					added = true;
					break;
				}
			}
			if (!added) {
				UITableActionType statsAction = new UITableActionType();
				statsAction.setUiid(uiStatsType.getUiid() + "_statsItem");
	    		statsAction.setFunction("statistic");
	    		statsAction.setIcon("ui-icon-image");
	    		ResourceBundlePropertyType statsI18nInfo = new ResourceBundlePropertyType();
	    		statsI18nInfo.setBundle("Common");
	    		statsI18nInfo.setKey("StatisticItem");
	    		statsAction.setTitle(statsI18nInfo);
	    		
	    		actions.add(statsAction);
			}
		}
	}
	
	public void clearStatsAction() {
	}
    
    public String getBodyName() {
		return bodyName;
	}

	public List<ParamType> getVariables() {
		return variables;
	}

	public Set getReconfigurableVarSet() {
		return reconfigurableVarSet;
	}

	public Map getReconfigurablePropMap() {
		return reconfigurablePropMap;
	}

	public Map getReconfigurationMap() {
		return reconfigurationMap;
	}

    public HTMLCellLayout getBodyLayout() {
		return bodyLayout;
	}

	public void importSelfJS(HTMLSnapshotContext context, int depth, boolean syncLoadJs) throws JspException
    {
    	Iterator jsFileNameIterator = null;
    	if (UserContext.isMobileRequest()) {
    		if (UserContext.isAppClient()) {
    			jsFileNameIterator = this.jsMobAppIncludeList.iterator();
    		} else {
    			jsFileNameIterator = this.jsMobIncludeList.iterator();
    		}
    	} else {
    		jsFileNameIterator = this.jsIncludeList.iterator();
    	}
        while (jsFileNameIterator.hasNext())
        {
            String jsFileName = (String)jsFileNameIterator.next();
            if (context.containsJsName(jsFileName))
            {
                continue;
            }
            else
            {
                context.addJsName(jsFileName);
            }
            long timestamp = WebConfig.getJsVersion();
            if (timestamp >= 0)
            {
            	String importJSCode = null;
            	if (UserContext.isMobileRequest()) {
            		if (UserContext.isAppClient()) {
            			importJSCode = (String)this.jsMobAppIncludeMap.get(jsFileName);
            		} else {
            			importJSCode = (String)this.jsMobIncludeMap.get(jsFileName);
            		}
            	} else {
                    importJSCode = (String)this.jsIncludeMap.get(jsFileName);
            	}
            	if (jsFileName.startsWith("http") || jsFileName.startsWith("https")) {
            		context.generateJS("<script type=\"text/javascript\" src=\"" + jsFileName + "\" ");
            		context.generateJS(syncLoadJs ? "" : WebConfig.isSyncLoadingJs(jsFileName));
            		context.generateJS("></script>");
            	} else {
            		context.generateJS(importJSCode + timestamp + "\" ");
            		context.generateJS(syncLoadJs ? "" : WebConfig.isSyncLoadingJs(jsFileName));
            		context.generateJS("></script>");
            	}
                HTMLUtil.generateTab(context, depth);
            }
        }
    }

    public String importSelfJs(HTMLSnapshotContext context, UIFormObject tEntityObj, boolean syncLoadJs)
    {
        StringBuffer sb = new StringBuffer();
        Iterator jsFileNameIterator = null;
        if (UserContext.isMobileRequest()) {
        	if (UserContext.isAppClient()) {
    			jsFileNameIterator = this.jsMobAppIncludeList.iterator();
    		} else {
    			jsFileNameIterator = this.jsMobIncludeList.iterator();
    		}
    	} else {
    		jsFileNameIterator = this.jsIncludeList.iterator();
    	}
        while (jsFileNameIterator.hasNext())
        {
            String jsFileName = (String)jsFileNameIterator.next();
            if (!context.containsJsName(jsFileName))
            {
                context.addJsName(jsFileName);
            }
            if (jsFileName.endsWith(".js"))
            {
				if (jsFileName.startsWith("http") || jsFileName.startsWith("https")) {
					sb.append("<script type=\"text/javascript\" src=\"");
	                sb.append(jsFileName).append("\" ");
	                sb.append(syncLoadJs ? "" : WebConfig.isSyncLoadingJs(jsFileName));
	                sb.append("></script>");
				} else {
	                sb.append("<script type=\"text/javascript\" src=\"").append(HTMLUtil.getWebRoot());
	                sb.append(jsFileName).append("?_timestamp=").append(WebConfig.getTimeStamp())
	                        .append("\" ");
	                sb.append(syncLoadJs ? "" : WebConfig.isSyncLoadingJs(jsFileName));
	                sb.append("></script>");
				}
            }
        }
        return sb.toString();
    }

	public String importSelfJs(HTMLSnapshotContext context, boolean syncLoadJs) {
		StringBuffer sb = new StringBuffer();
		Iterator jsFileNameIterator = this.jsIncludeList.iterator();
		if (UserContext.isMobileRequest()) {
			if (UserContext.isAppClient()) {
    			jsFileNameIterator = this.jsMobAppIncludeList.iterator();
    		} else {
    			jsFileNameIterator = this.jsMobIncludeList.iterator();
    		}
		}
		while (jsFileNameIterator.hasNext()) {
			String jsFileName = (String) jsFileNameIterator.next();
			if (!context.containsJsName(jsFileName)) {
				context.addJsName(jsFileName);
			}
			
			if (jsFileName.endsWith(".js")) {
				jsFileName = jsFileName.replace("\\", "/");
				boolean needTimestamp = true;
				if (jsFileName.startsWith("http") || jsFileName.startsWith("https")) {
					needTimestamp = false;
				} else if (UserContext.isMobileRequest() && UserContext.isAppClient()) {
					jsFileName = WebConfig.replaceAppJsWebContext(jsFileName);
				}
				sb.append("<script type=\"text/javascript\" src=\"").append(jsFileName);
				if (needTimestamp) {
					sb.append("?_timestamp=").append(WebConfig.getTimeStamp());
				}
				sb.append("\"").append(" ");
				sb.append(syncLoadJs ? "" : WebConfig.isSyncLoadingJs(jsFileName));
				sb.append("></script>");
			}
		}

		return sb.toString();
	}
	
	public void getJSPathSet(HTMLSnapshotContext context, Map entityMap) {
		getJSPathSet(context, entityMap, false);
	}
	
	public void getJSPathSet(HTMLSnapshotContext context, Map entityMap, boolean syncLoadJs) {
		if (entityMap == null) {
			entityMap = new HashMap();
		}
		context.generateJS(importSelfJs(context, syncLoadJs));

		Iterator iterator = this.refereneEntityList.iterator();
		while (iterator.hasNext()) {
			String entityName = (String) iterator.next();
			if (!entityMap.containsKey(entityName)) {
				UIFormObject formObject = PageCacheManager
						.getUIFormObject(entityName);
				formObject.getJSPathSet(context, entityMap, syncLoadJs);
			}
		}
	}

}
