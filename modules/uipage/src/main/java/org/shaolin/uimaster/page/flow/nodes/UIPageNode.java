package org.shaolin.uimaster.page.flow.nodes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shaolin.bmdp.datamodel.common.NameExpressionType;
import org.shaolin.bmdp.datamodel.common.ParamType;
import org.shaolin.bmdp.datamodel.page.UIPage;
import org.shaolin.bmdp.datamodel.pagediagram.DisplayOutType;
import org.shaolin.bmdp.datamodel.pagediagram.NextType;
import org.shaolin.bmdp.datamodel.pagediagram.OutType;
import org.shaolin.bmdp.datamodel.pagediagram.PageNodeType;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.MobilitySupport;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.shaolin.uimaster.page.exception.ODProcessException;
import org.shaolin.uimaster.page.exception.WebFlowException;
import org.shaolin.uimaster.page.flow.ProcessHelper;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.shaolin.uimaster.page.flow.error.WebflowError;
import org.shaolin.uimaster.page.flow.error.WebflowErrorUtil;
import org.shaolin.uimaster.page.javacc.WebFlowContext;
import org.shaolin.uimaster.page.javacc.WebFlowContextHelper;
import org.shaolin.uimaster.page.od.PageODProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This node is sharing for all requests!
 * 
 * @author wushaol
 *
 */
public class UIPageNode extends WebNode {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(UIPageNode.class);

	private PageNodeType type;
	
	private List<ParamType> inputVars;
	
	private boolean hasMobilePage = false;
	
	public UIPageNode(PageNodeType type) {
		super(type);
		this.type = type;
	}
	
	public String getSourceEntity() {
		if (type.getSourceEntity() != null) {
			return type.getSourceEntity().getEntityName();
		}
		return null;
	}
	
    /**
     * parse current node: input datas, operations, outs
     * @param request
     * @throws ParsingException
     */
    public void parse() throws ParsingException
    {
		if (logger.isDebugEnabled())
			logger.debug("parse: " + toString());

		if (isParsed) {
			return;
		}

		WebFlowContext inContext = WebFlowContextHelper.getWebFlowContext(this, type.getVariables(), true);
		// parse input datas
        ProcessHelper.parseVariables(type.getVariables(), inContext);
        IEntityManager entityManager = IServerServiceManager.INSTANCE.getEntityManager();
        UIPage page = entityManager.getEntity(type.getSourceEntity().getEntityName(), UIPage.class);
        inputVars = page.getODMapping().getDataEntities();
        if (inputVars != null) {
        	ProcessHelper.parseVariables(inputVars, inContext);
        }
        
        List<OutType> outs = type.getOuts();
        for (OutType out: outs) {
        	WebFlowContext outContext = WebFlowContextHelper.getWebFlowContext(this, type.getVariables(), true);
        	NextType next = out.getNext();
			if (next == null) {
				logger.error("processDataMapping(():the next is null, out="
						+ out.getName() + ", node" + toString());
				return;
			}

			for (NameExpressionType ne : next.getOutDataMappingToNodes()) {
				ne.getExpression().parse(outContext);
			}
        }
        
        try {
        	entityManager.getEntity(type.getSourceEntity().getEntityName() + MobilitySupport.MOB_PAGE_SUFFIX, UIPage.class);
        	hasMobilePage = true;
        } catch (EntityNotFoundException e) {
        	hasMobilePage = false;
        }
        
		isParsed = true;
    }
    
    /**
     * execute the node
     * always return null
     *
     *@return
     */
    public WebNode execute(WebFlowContext inContext) 
    		throws WebFlowException
    {
		if (logger.isInfoEnabled())
			logger.info("execute():" + toString());

		try {
			// parse node
			if (!isParsed) {
				parse();
			}
			// prepare input data
			prepareInputData(inContext);

			ProcessHelper.processPreForward(this, inContext.getRequest());
	        String uipageName = type.getSourceEntity().getEntityName();
	        try
	        {
	        	if (UserContext.isMobileRequest() && this.hasMobilePage) {
	            	uipageName += MobilitySupport.MOB_PAGE_SUFFIX;
	            } 
	        	UserRequestContext context = new UserRequestContext(inContext.getRequest(), inContext.getResponse());
	        	context.setCurrentFormInfo(uipageName, "", "");
	        	context.setIsDataToUI(true);
	        	if (inputVars != null && inputVars.size() > 0) {
		        	Map<String, Object> data = new HashMap<String, Object>();
	        		for (ParamType var : inputVars) {
	        			data.put(var.getName(), inContext.getRequest().getAttribute(var.getName()));
		        	}
		        	context.setODMapperData(data);
	        	}
	        	HTMLUtil.forward(context, uipageName);
				if (type.getOperation() != null) {
					// begin transaction if needed
					type.getOperation().evaluate(inContext);
				}
	        }
	        catch ( Exception e )
	        {
	        	rollbackTransaction(inContext);
	            String msg = "Exception occurs when forward to the uipage: " + uipageName;
	            if ( e instanceof ServletException )
	            {
	                e = ProcessHelper.transformServletException((ServletException)e);
	            }
	            logger.error(msg, e);
	            
	            WebflowErrorUtil.addError(inContext.getRequest(), type.getName() + ".uipage.error", 
	                    new WebflowError(e.getMessage(), e));
	            ProcessHelper.processForwardError(this, inContext.getRequest(), inContext.getResponse());
	            return null;
	        }
		} catch (ParsingException ex1) {
			throw new WebFlowException("ParsingException when execute display node {0}", ex1,
					new Object[] { toString() });
		} catch (EvaluationException ex1) {
			throw new WebFlowException("EvaluationException when execute display node {0}", ex1,
					new Object[] { toString() });
		} finally {
			// clear webflow temporary variables in session.
			inContext.clearTempVariables();
		}

		return null;
    }

	private void rollbackTransaction(WebFlowContext wfcontext) {
		if (logger.isInfoEnabled())
			logger.info("rollbackTransaction() if has transaction");
		try {
			if (wfcontext.isInTransaction()) {
				wfcontext.rollbackTransaction();
			}
		} catch (Exception e) {
			logger.error(
					"error when rollback the user transaction, execute node "
							+ toString(), e);
		}
	}
    
    public void prepareInputData(WebFlowContext inContext)
        throws ParsingException, EvaluationException
    {
        if (logger.isDebugEnabled())
            logger.debug("prepareInputData():" + toString());

        //prepare global variables of chunk
        this.getChunk().prepareGlobalVariables(inContext.getRequest(), inContext);

        //get datamappingToNode of previous node
        Map datas = (Map) inContext.getRequest().getAttribute(WebflowConstants.OUTDATA_MAPPING2NODE_KEY);
        inContext.getRequest().removeAttribute(WebflowConstants.OUTDATA_MAPPING2NODE_KEY);
        if(datas == null) {
        	datas = new HashMap();//can be null
        }
        setLocalVariables(inContext, type.getVariables(), datas);
        if (this.inputVars != null) {
        	setLocalVariables(inContext, this.inputVars, datas);
        }
        
        Map<String, Object> cachedInput = new HashMap<String, Object>();
        List<ParamType> vars = type.getVariables();
        for (ParamType var: vars)
        {
            String varName = WebflowConstants.
                          REQUEST_PARSING_CONTEXT_PREFIX + var.getName();
            Object varValue = inContext.getVariableValue(varName);
            if (varValue != null)
            {
                cachedInput.put(var.getName(), varValue);
            }
        }
        if (this.inputVars != null) {
	        for (ParamType var: this.inputVars)
	        {
	            String varName = WebflowConstants.
	                          REQUEST_PARSING_CONTEXT_PREFIX + var.getName();
	            Object varValue = inContext.getVariableValue(varName);
	            if (varValue != null)
	            {
	                cachedInput.put(var.getName(), varValue);
	            }
	        }
        }
        if (!cachedInput.isEmpty())
        {
//            HttpSession session = inContext.getRequest().getSession(true);
//            session.setAttribute(WEBFLOW_CACHED_INPUT, cachedInput);
        }
    }

    /**
     * prepare Output data
     * 1.  init chunk
     * 2. find out
     * 3.  UI2DataConvert
     * 4. set/init  the OutputData for this node
     * 5. evaluate outDataMappingToNode and outDataMappingToChunk, and set for next node
     * @param context
     * @param variables the default value expression should be parsed
     * @throws ODProcessException 
     */
    public void prepareOutputData(WebFlowContext inContext)
        throws EvaluationException, ParsingException
    {
        if (logger.isInfoEnabled())
            logger.info("execute UIPageDisplayNode.prepareOutputData(HttpServletRequest): " + toString());

        if(!isParsed) {
        	parse();
        }
        
        //2. find out
        DisplayOutType out = findOut(inContext.getRequest());
        if(out != null)
        {
        	WebFlowContext outContext = new WebFlowContext(this, inContext.getRequest(), inContext.getResponse());
            //3.  UI2DataConvert
            Map convertResult = null;
			try {
				convertResult = processUI2DataConvert(inContext.getRequest(), inContext.getResponse(), out,
						outContext);
			} catch (Exception e) {
				throw new EvaluationException(e);
			}
            //4. set/init  the OutputData for this node
            setLocalVariables(outContext, this.getType().getVariables(), convertResult);
            
            //5. evaluate outDataMappingToNode and outDataMappingToChunk, and set for next node
            processDataMapping(inContext.getRequest(), inContext.getResponse(), out, outContext);
        }

    }


    /**
     * 1. find out
     * @param request The servlet request we are processing
     * @return the collection of business entities
     */
    protected DisplayOutType findOut(HttpServletRequest request)
    {
        if(logger.isDebugEnabled()) {
            logger.debug("findOut(): from sourcenode {}", toString());
        }
        
        //get the Out in source node which is associated with the destination node
        DisplayOutType out = null;
        String outName = (String)request.getParameter(WebflowConstants.OUT_NAME);
        if (outName == null) {
        	outName = (String)request.getAttribute(WebflowConstants.OUT_NAME);
        }
        if(outName != null)
        {
            OutType tempOut = findOut(outName);
            if(tempOut == null)
            {
                logger.error("findOut():cannot find the out {} of display node: {}",
                			new Object[] {outName, toString()});
                return null;
            }

			if (tempOut instanceof DisplayOutType) {
				out = (DisplayOutType) tempOut;
			} else {
				logger.error("findOut():the out {} of node: {} is not DisplayOutType!!! the out type is {}", 
						new Object[] {outName, toString(), tempOut.getClass().getName()});
				return null;
			}
        } else {
			logger.warn("processConvert():cannot find the out in request attributes");
			return null;
		}
        return out;
    }

    /**
     * convert the uidata to the business data in Display Node
     * validate and convert the display node UI outputdata to business entity data,
     *  the method will
     * use uihtml converter to validate and convert the data.
     * @param srcNode
     * @param out
     * @param request
     * @return
     * @throws AjaxException 
     */
    private Map processUI2DataConvert(HttpServletRequest request, HttpServletResponse response,
            DisplayOutType out, WebFlowContext outContext) throws EvaluationException, ODProcessException, AjaxException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("processUI2DataConvert()");
            logger.debug("the node is " + toString());
            logger.debug("the out name is " + out.getName());
        }
        
        Map datas = ProcessHelper.evaluateNameExpressions(outContext, out.getMappings());
        
        UserRequestContext requestContext = new UserRequestContext(request);
        requestContext.resetRepository();
        
        String uipageName = type.getSourceEntity().getEntityName();
        if (UserContext.isMobileRequest() && this.hasMobilePage) {
        	uipageName += MobilitySupport.MOB_PAGE_SUFFIX;
        } 
        requestContext.setCurrentFormInfo(uipageName, "", "");
        requestContext.setAjaxWidgetMap(AjaxContextHelper.getFrameMap(request));
        requestContext.setODMapperData(datas);
        requestContext.setIsDataToUI(false);
        try
        {
        	UserRequestContext.UserContext.set(requestContext);
        	PageODProcessor pageODProcessor = new PageODProcessor(requestContext, uipageName);
            pageODProcessor.process();
        } catch (ODProcessException e) {
			throw e;
		}
        finally
        {
        }
        return requestContext.getODMapperData();       
    }



    /**
     * prepare Output data
     *  1. evaluate NameExpressions outDataMappingToNode, and set result in request
     *  2. evaluate NameExpressions outDataMappingToChunk, and set result in session
     *  3. remove outputdata of the node from request;
     *  4. (Postpone) remove global variables from session only when destchunk is different from current chunk
     * @param context
     * @param variables the default value expression should be parsed
     */
    protected void processDataMapping(HttpServletRequest request, HttpServletResponse response,
                                      DisplayOutType out, WebFlowContext outContext)
         throws ParsingException, EvaluationException
    {
        if (logger.isDebugEnabled())
            logger.debug("processDataMapping(():" + toString());
        
        NextType next = out.getNext();
        if(next == null) {
            logger.error("processDataMapping(():the next is null, out=" + out.getName()
                         + ", node" + toString());
            return;
        }

        //1. evaluate NameExpressions outDataMappingToNode, and set result in request
        Map datas = ProcessHelper.evaluateNameExpressions(outContext,
            next.getOutDataMappingToNodes());
		if (datas == null || datas.size() == 0) {
			request.setAttribute(WebflowConstants.OUTDATA_MAPPING2NODE_KEY, null);
		} else {
			request.setAttribute(WebflowConstants.OUTDATA_MAPPING2NODE_KEY, datas);
		}

        //2.evaluate NameExpressions outDataMappingToChunk, and set result in session
        datas = ProcessHelper.evaluateNameExpressions(outContext,
            next.getOutDataMappingToNodes());
		if (datas == null || datas.size() == 0) {
			request.setAttribute(WebflowConstants.OUTDATA_MAPPING2CHUNK_KEY, null);
		} else {
			request.setAttribute(WebflowConstants.OUTDATA_MAPPING2CHUNK_KEY, datas);
		}
    }

    //private String str = null;cache
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" [");
        if(this.getChunk() != null)
        {
            sb.append("chunkname=");
            sb.append(this.getChunk().getEntityName());
            sb.append(", ");
        }
        sb.append("nodename=");
        sb.append(type.getName());
        sb.append(", type=UIPageDisplayNodeType]");
        return sb.toString();

    }

}
