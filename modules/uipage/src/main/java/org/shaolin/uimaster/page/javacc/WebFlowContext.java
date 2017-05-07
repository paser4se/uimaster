package org.shaolin.uimaster.page.javacc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.shaolin.bmdp.datamodel.common.ParamType;
import org.shaolin.bmdp.exceptions.BusinessOperationException;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.OpExecuteContext;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.shaolin.uimaster.page.flow.nodes.WebNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  The context for webflow's nodes.
 */
public class WebFlowContext extends OpExecuteContext
{
    //log4j
    private static Logger logger = LoggerFactory.getLogger(WebFlowContext.class);

	protected final WebNode node;

    protected final String envName;
    
    protected transient HttpServletRequest request = null;
    
    protected transient HttpServletResponse response = null;
    
    /**
	 * for parsing only!
	 * 
	 * @param node
	 * @param variables
	 * @param needParsing
	 */
	public WebFlowContext(WebNode node, List<ParamType> variables, boolean needParsing)
	{
	    super();
	    this.node = node;
	    this.envName = node.getChunk().getEntityName();
	    
	    if (needParsing) {
	    	parse(variables);
	    }
	}

	/**
     * Request object can't be cached locally.
     * we do it in two phases
     * @param request
     */
    public WebFlowContext(WebNode node, HttpServletRequest request, HttpServletResponse response) {
    	super();
		this.request = request;
		this.response = response;
        this.node = node;
        this.envName = node.getChunk().getEntityName();
		
		//userTransaction
        if (request.getAttribute(WebflowConstants.USERTRANSACTION_KEY) != null)
        {
            if (logger.isDebugEnabled()){
                logger.debug("the userTransaction in request isnot null! span multi-node transacation!");
            }
            userTransaction = (UserTransaction)
            		request.getAttribute(WebflowConstants.USERTRANSACTION_KEY);
        }
        
        try {
	        HttpRequestEvaluationContext requestContext = 
	        		new HttpRequestEvaluationContext(request, node);
	        requestContext.setVariableValue(WebflowConstants.QUIT_ACTION_FLAG_KEY,
	                                        Boolean.FALSE); //for OpQuitAction
	        HttpSessionEvaluationContext sessionContext =
	            new HttpSessionEvaluationContext(request, response, 
	                                             ((node == null) ? null
	                                              : node.getChunk()));
	        sessionContext.setVariableValue(WebflowConstants.QUIT_ACTION_FLAG_KEY,
	                                        Boolean.FALSE); //for OpQuitAction
	        setEvaluationContextObject(WebflowConstants.REQUEST_PARSING_CONTEXT_PREFIX,
	                requestContext);
	        setEvaluationContextObject(WebflowConstants.SESSION_PARSING_CONTEXT_PREFIX,
	                sessionContext);
        } catch(EvaluationException ex) {
        	logger.error("error when create WebFlowContext", ex);
        }
	}
    
    private void parse(List<ParamType> variables)
	{
	    //get request&session parsing context
	    DefaultParsingContext requestPContext = WebFlowContextHelper.
	        getHttpRequestParsingContext(variables);
	    DefaultParsingContext sessionPContext = WebFlowContextHelper.
	        getHttpSessionParsingContext(node.getChunk().getGlobalVariable());
	    
	    //set parsing context.
	    setParsingContextObject(WebflowConstants.
	                            REQUEST_PARSING_CONTEXT_PREFIX,
	                            requestPContext);
	    setParsingContextObject(WebflowConstants.
	                            SESSION_PARSING_CONTEXT_PREFIX,
	                            sessionPContext);
	    this.setExternalParseContext(this);
	    this.setExternalEvaluationContext(this);
	}

	public Class<?> getVariableClass(String name) throws ParsingException
    {
        name = fixVarName(name);

        return super.getVariableClass(name);
    }

    public Object getVariableValue(String name) throws EvaluationException
    {
        name = fixVarName(name);

        return super.getVariableValue(name);
    }

    public void setVariableValue(String name, Object value) throws
        EvaluationException
    {
        name = fixVarName(name);

        super.setVariableValue(name, value);
    }
    
    public void clearTempVariables() {
    	HttpSessionEvaluationContext sessionContext = (HttpSessionEvaluationContext)getEvaluationContextObject(WebflowConstants.SESSION_PARSING_CONTEXT_PREFIX);
    	if (sessionContext != null) {
    		sessionContext.clearAllTempSessionVars();
    	}
    }

    private String fixVarName(String name)
    {
        if (WebflowConstants.QUIT_ACTION_FLAG_KEY.equals(name))
        {
            return WebflowConstants.SESSION_PARSING_CONTEXT_PREFIX + name;
        }
        else
        {
            return name;
        }

    }

    /**
     *  Retruns the http request
     * @return
     */
    public HttpServletRequest getRequest()
    {
        return request;
    }
    
    public HttpServletResponse getResponse()
    {
        return response;
    }

    /**
     *  Retruns the node
     * @return
     */
    public WebNode getNode()
    {
        EvaluationContext context = getEvaluationContextObject(
            WebflowConstants.REQUEST_PARSING_CONTEXT_PREFIX);
        if (context instanceof HttpRequestEvaluationContext)
        {
            return ((HttpRequestEvaluationContext)context).getNode();
        }
        else
        {
            logger.warn("can not find the node in webflow context");
            return null;
        }
    }

    /**
     *  Retruns the http request
     * @return
     */
    public HttpSession getSession()
    {
        EvaluationContext context = getEvaluationContextObject(
            WebflowConstants.SESSION_PARSING_CONTEXT_PREFIX);

        if (context instanceof HttpSessionEvaluationContext)
        {
            return ((HttpSessionEvaluationContext)context).getSession();
        }
        else
        {
            logger.warn("can not find the session in webflow context");
            return null;
        }
    }

    public String getEnvName()
    {
        return envName;
    }

    /**
     * if no transaction, return null
     * @return
     */
    public UserTransaction getCurrentTransaction()
    {
        return userTransaction;
    }

    public void beginTransaction() throws BusinessOperationException
    {
        super.beginTransaction();
        request.setAttribute(WebflowConstants.USERTRANSACTION_KEY,
                             userTransaction);
    }

    public void commitTransaction() throws BusinessOperationException
    {
        super.commitTransaction();
        request.setAttribute(WebflowConstants.USERTRANSACTION_KEY, null);

    }

    public void rollbackTransaction() throws BusinessOperationException
    {
        request.setAttribute(WebflowConstants.USERTRANSACTION_KEY, null);
        super.rollbackTransaction();
    }

}
