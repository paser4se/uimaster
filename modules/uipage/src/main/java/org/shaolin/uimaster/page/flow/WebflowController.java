package org.shaolin.uimaster.page.flow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.NDC;
import org.shaolin.bmdp.datamodel.pagediagram.NextType;
import org.shaolin.bmdp.datamodel.pagediagram.OutType;
import org.shaolin.bmdp.exceptions.BusinessOperationException;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.security.IPermissionService;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.MobilitySupport;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.UIFlowCacheManager;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.shaolin.uimaster.page.exception.NoWebflowAPException;
import org.shaolin.uimaster.page.exception.NoWebflowNodeAPException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.exception.WebFlowException;
import org.shaolin.uimaster.page.flow.error.WebflowError;
import org.shaolin.uimaster.page.flow.error.WebflowErrorUtil;
import org.shaolin.uimaster.page.flow.nodes.WebNode;
import org.shaolin.uimaster.page.javacc.HttpRequestEvaluationContext;
import org.shaolin.uimaster.page.javacc.WebFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebflowController {
    private static Logger logger = LoggerFactory.getLogger(WebflowController.class);
    
    protected String charset = "UTF-8";

    protected boolean nocache = false;
    
    public static class AttributesAccessor {
    	
    	private final transient HttpServletRequest request;
    	
    	public String chunkName;
    	public String nodeName;
    	public String outName;
    	public String entityName;
    	public String destnodename;
    	public String destchunkname;
    	
    	String orgId;
    	String orgCode;
    	
    	public AttributesAccessor(HttpServletRequest request) {
    		this.request = request;
    		
    		this.chunkName = (String)request.getAttribute(WebflowConstants.SOURCE_CHUNK_NAME);
    		if (this.chunkName == null) {
    			this.chunkName = request.getParameter(WebflowConstants.SOURCE_CHUNK_NAME);
    		}
    		this.nodeName = (String)request.getAttribute(WebflowConstants.SOURCE_NODE_NAME);
    		if (this.nodeName == null) {
    			this.nodeName = request.getParameter(WebflowConstants.SOURCE_NODE_NAME);
    		}
    		this.outName = (String)request.getAttribute(WebflowConstants.OUT_NAME);
    		if (this.outName == null) {
    			this.outName = request.getParameter(WebflowConstants.OUT_NAME);
    		}
    		this.entityName = (String)request.getAttribute(WebflowConstants.SOURCE_ENTITY_NAME);
    		if (this.entityName == null) {
    			this.entityName = request.getParameter(WebflowConstants.SOURCE_ENTITY_NAME);
    		}
    		this.destnodename = (String)request.getAttribute(WebflowConstants.DEST_NODE_NAME);
    		if (this.destnodename == null) {
    			this.destnodename = request.getParameter(WebflowConstants.DEST_NODE_NAME);
    		}
    		this.destchunkname = (String)request.getAttribute(WebflowConstants.DEST_CHUNK_NAME);
    		if (this.destchunkname == null) {
    			this.destchunkname = request.getParameter(WebflowConstants.DEST_CHUNK_NAME);
    		}
    		this.orgId = (String)request.getAttribute(WebflowConstants.USER_ORGID);
    		if (this.orgId == null) {
    			this.orgId = request.getParameter(WebflowConstants.USER_ORGID);
    		}
    		this.orgCode = (String)request.getAttribute(WebflowConstants.USER_ORGNAME);
    		if (this.orgCode == null) {
    			this.orgCode = request.getParameter(WebflowConstants.USER_ORGNAME);
    		}
    	}
    	
    	public void setAttribute(String constant, Object obj) {
    		request.setAttribute(constant, obj);
    	}
    	
    	public void setFlag(Boolean flag) {
    		request.setAttribute(WebflowConstants.ATTRIBUTE_FLAG, flag);
    	}
    	
    	public boolean getFlag() {
    		Boolean attributeFlag = (Boolean)
    				request.getAttribute(WebflowConstants.ATTRIBUTE_FLAG);
    		if(attributeFlag == null) {
            	attributeFlag = Boolean.TRUE;
            	setFlag(Boolean.TRUE);
    		}
    		return attributeFlag.booleanValue();
    	}
    	
    	public void setOut(String outName){
    		request.setAttribute(WebflowConstants.OUT_NAME, outName);
    	}
    	
    	public String getOut(){
    		return (String)request.getAttribute(WebflowConstants.OUT_NAME);
    	}
    }

    /**
     * a normal link action.
     * 
     * @param request
     * @param response
     * @param _chunkname
     * @param _nodename
     * @throws IOException
     */
    @RequestMapping(name="/webflow.do", method=RequestMethod.GET)
    public void doWebflowGet(HttpServletRequest request, HttpServletResponse response, 
    		@RequestParam(value="_chunkname", required=true) String _chunkname, 
    		@RequestParam(value="_nodename", required=true) String _nodename)
            throws IOException {
    		if (WebConfig.isJAAS()) {
			if (request.getParameter("_login") == null) {
				HttpSession session = request.getSession(false);
				if ((session == null) || (session.getAttribute("indexPageVisited") == null)) {
					ProcessHelper.processDirectForward(WebConfig.replaceWebContext(WebConfig.getIndexPage()), request,response);
					return;
				}
			}
		}
    		HttpSession session = request.getSession();
		UserContext currentUserContext = (UserContext)session.getAttribute(WebflowConstants.USER_SESSION_KEY);
		if (currentUserContext == null) {
			// create a fake user context for guest users when access our website at the first time.
			currentUserContext = new UserContext();
			currentUserContext.setOrgCode(null);
			currentUserContext.setOrgId(-1);
			currentUserContext.setUserRequestIP(request.getRemoteAddr());
			session.setAttribute(WebflowConstants.USER_SESSION_KEY, currentUserContext);
		}
		String userLocale = WebConfig.getUserLocale(request);
		List userRoles = (List)session.getAttribute(WebflowConstants.USER_ROLE_KEY);
		String userAgent = request.getHeader("user-agent");
		boolean isMobile = MobilitySupport.isMobileRequest(userAgent);
		//add user-context thread bind
        UserContext.register(session, currentUserContext, userLocale, userRoles, isMobile);
        UserContext.setAppClient(request);
        //add request thread bind
        HttpRequestEvaluationContext.registerCurrentRequest(request);
        AppContext.register(IServerServiceManager.INSTANCE);
        String locale = UserContext.getUserLocale();
		if (logger.isDebugEnabled()) {
			logger.debug("Detected user locale:" + locale);
		}
        LocaleContext.createLocaleContext(locale);
        
 	    WebNode destNode = UIFlowCacheManager.getInstance().findWebNode(_chunkname, _nodename);
		if (destNode == null) {
			ProcessHelper.processResponseSendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"can't find destination node");
			return;
		}
		if (!checkAccessPermission(_chunkname, _nodename, request)) {
			ProcessHelper.processDirectForward(WebConfig.getNoPermissionPage(), request, response);
			return;
		}
		ProcessHelper.convertParameter2Attribute(request, destNode.getType());
		if (logger.isInfoEnabled()) {
			logger.info("Process destination node " + destNode.toString());
		}
		WebFlowContext flowContext = null;
		try {
			while (destNode != null) {
				flowContext = new WebFlowContext(destNode, request, response);
				WebNode nextNode = destNode.execute(flowContext);
				destNode = nextNode;
			}
			HibernateUtil.releaseSession(true);
		} catch (Throwable ex) {
			HibernateUtil.releaseSession(false);
			handleFlowException(request, response, destNode, flowContext, ex);
		} finally {
        		UserContext.unregister();
            LocaleContext.clearLocaleContext();
		}
    }
    
    /**
     * the page submit action.
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(name="/webflow.post", method=RequestMethod.POST)
    public void doWebflowPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    		AttributesAccessor attrAccessor = new AttributesAccessor(request);
    		if(attrAccessor.outName == null || attrAccessor.outName.trim().length() == 0) {
    			ProcessHelper.processResponseSendError(response, HttpServletResponse.SC_BAD_REQUEST,
    					"page out does not specify!");
    			return;
    		}
    		if (checkSessionTimeout(request)) {
			ProcessHelper.processDirectForward(WebConfig.getTimeoutPage(), request, response);
			return;
		}
    		HttpSession session = request.getSession();
		UserContext currentUserContext = (UserContext)session.getAttribute(WebflowConstants.USER_SESSION_KEY);
		if (currentUserContext == null) {
			// user context must be existing before submits a page.
			ProcessHelper.processDirectForward(WebConfig.getTimeoutPage(), request, response);
			return;
		}
        try
        {
			String userLocale = WebConfig.getUserLocale(request);
			List userRoles = (List)session.getAttribute(WebflowConstants.USER_ROLE_KEY);
			String userAgent = request.getHeader("user-agent");
			boolean isMobile = MobilitySupport.isMobileRequest(userAgent);
			//add user-context thread bind
            UserContext.register(session, currentUserContext, userLocale, userRoles, isMobile);
            UserContext.setAppClient(request);
            //add request thread bind
            HttpRequestEvaluationContext.registerCurrentRequest(request);
            AppContext.register(IServerServiceManager.INSTANCE);
            String locale = UserContext.getUserLocale();
			if (logger.isDebugEnabled()) {
				logger.debug("Detected user locale:" + locale);
			}
            LocaleContext.createLocaleContext(locale);
            
            if (!checkAccessPermission(attrAccessor.chunkName, attrAccessor.nodeName, request)) {
				ProcessHelper.processDirectForward(WebConfig.getNoPermissionPage(), request, response);
				return;
			}
            _processPageSubmit(request, response, attrAccessor);
        }
        catch(RuntimeException e)
        {   
            logger.warn("Error while processing webflow:" + e.getMessage(), e);
            throw e;
        }
        catch(Error e)
        {
            logger.warn("Error while processing webflow:" + e.getMessage(), e);
            throw e;
        } 
        finally {
        		UserContext.unregister();
            LocaleContext.clearLocaleContext();
        }
    }
    
    private boolean checkAccessPermission(final String chunkName, final String nodeName, final HttpServletRequest request) 
    {
    		String orgCode = (String)UserContext.getUserData(UserContext.CURRENT_USER_ORGNAME);
        if (orgCode == null) {
        		orgCode = IServerServiceManager.INSTANCE.getMasterNodeName();
        }
	    	IPermissionService permiService = AppContext.get().getService(IPermissionService.class);
	    	List<IConstantEntity> roleIds = (List<IConstantEntity>)request.getSession().getAttribute(WebflowConstants.USER_ROLE_KEY);
	    	int decision = permiService.checkModule(orgCode, chunkName, nodeName, roleIds);
	    	return IPermissionService.ACCEPTABLE == decision || IPermissionService.NOT_SPECIFIED == decision;
    }
    
    private void _processPageSubmit(HttpServletRequest request, HttpServletResponse response, AttributesAccessor attrAccessor)
    {
        //the flag whether current method should call NDC.pop before returning
        boolean needNDCPop = false;
        if(NDC.getDepth() == 0)
        {
            NDC.push(request.getRemoteAddr());
            needNDCPop = true;
        }
        try
        {
			try {
				request.setCharacterEncoding(charset);
			} catch (UnsupportedEncodingException e1) {
			}
            // Identify the path component
            String path = ProcessHelper.processPath(request);
            if(logger.isDebugEnabled())
                logger.debug("Processing a " + request.getMethod() + " for " + path);

            if (logger.isDebugEnabled())
            {
                for (Enumeration<String> enup = request.getParameterNames(); 
                		enup.hasMoreElements();) {
                    String paramName = enup.nextElement();
                    String paramValue = request.getParameter(paramName);
                    logger.debug("Parameter:{}={}", new Object[] {paramName, paramValue});
                }
            }
            try {
				AjaxContextHelper.getAjaxWidgetMap(request.getSession());
			} catch (NullPointerException e) {
				logger.info("Session time out or submit duplication. forward to login page");
				WebflowErrorUtil.addError(request, "submit.error",
						new WebflowError(e.getMessage(), e));
				WebNode srcNode = processSourceWebNode(request, attrAccessor);
				ProcessHelper.processForwardError(srcNode, request, response);
				return;
			}
            // sync value
			try {
				ProcessHelper.processSyncValues(request);
			} catch (AjaxException e) {
				logger.error("Error occurs when synchronize the widget values: "
								+ e.getMessage(), e);
				WebflowErrorUtil.addError(request, "ajax.sync.error",
						new WebflowError(e.getMessage(), e));
				WebNode srcNode = processSourceWebNode(request, attrAccessor);
				ProcessHelper.processForwardError(srcNode, request, response);
				return;
			} 

            // Set the content type and no-caching headers if requested
            processNoCache(response);

            	// find the source WebNode first.
            	WebNode srcNode = processSourceWebNode(request, attrAccessor);
            	if (logger.isInfoEnabled()) {
				logger.info("source node " + srcNode.toString());
			}
            try
            {
                	WebFlowContext flowContext = new WebFlowContext(srcNode, request, response);
                	//validate and convert the output data of DisplayNode srcNode
                srcNode.prepareOutputData(flowContext);
                
                HibernateUtil.releaseSession(true);
            }
            catch (Throwable ex)
            {
            		HibernateUtil.releaseSession(false);
                if (ex instanceof ParsingException)
                {
                    logger.error("ParsingException when  prepare OutputData for node "
                                 + srcNode.toString(), ex);
                    WebflowErrorUtil.addError(request, srcNode.getName() + ".parsing.error",
                                           new WebflowError(ex.getMessage(), ex));
                    ProcessHelper.processForwardError(srcNode, request, response);
                }
                else if (ex instanceof EvaluationException)
                {
                    logger.error("EvaluationException when  prepare OutputData for node "
                                 + srcNode.toString(), ex);
                    WebflowErrorUtil.addError(request, srcNode.getName() + ".evaluation.error",
                                           new WebflowError(ex.getMessage(), ex));
                    ProcessHelper.processForwardError(srcNode, request, response);
                }
                else if (ex instanceof UIPageException)
                {
                    logger.error("UIPageException when  prepare OutputData for node "
                                 + srcNode.toString(), ex);
                    WebflowErrorUtil.addError(request, srcNode.getName() + ".uipage.error",
                                           new WebflowError(ex.getMessage(), ex));
                    ProcessHelper.processForwardError(srcNode, request, response);
                }
                else
                {
                    logger.error("Exception when  prepare OutputData for node "
                                 + srcNode.toString(), ex);
                    WebflowErrorUtil.addError(request, srcNode.getName() + ".prepareOutputData.error",
                                           new WebflowError(ex.getMessage(), ex));
                    ProcessHelper.processForwardError(srcNode, request, response);
                }
                return;
            } 
            // find the page out's dest node
            WebNode destNode = processDestWebNode(srcNode, request, attrAccessor);
            if (destNode == null)
            {
                ProcessHelper.processResponseSendError(response,
                                         HttpServletResponse.SC_BAD_REQUEST,
                                         "can't find destination node");
                return;
            }
            ProcessHelper.convertParameter2Attribute(request, destNode.getType());
            if(logger.isInfoEnabled()) {
                logger.info("Process destination node " + destNode.toString());
            }
            
            WebFlowContext flowContext = null;
            try
            {
                while(destNode != null)
                {
                		flowContext = new WebFlowContext(destNode, request, response);
                    WebNode nextNode = destNode.execute(flowContext);
                    destNode = nextNode;
                }
                HibernateUtil.releaseSession(true);
            }
            catch (Throwable ex)
            {
            		HibernateUtil.releaseSession(false);
                handleFlowException(request, response, destNode, flowContext, ex);
            } 
        } 
        finally
        {
            if (needNDCPop)
            {
                NDC.pop();
            }
        }
    }

	private void handleFlowException(HttpServletRequest request, HttpServletResponse response, WebNode destNode,
			WebFlowContext flowContext, Throwable ex) {
		if(ex instanceof NoWebflowAPException)
		{
		    String key = destNode.getChunk().getEntityName() + ".access.error";
		    String message = "----PermissionError: access the webflow chunk" + 
		                destNode.getChunk().getEntityName() + " error ";
		    logger.error("*******" + destNode.getChunk().getEntityName() + ".webflow access denied!", ex);
		    
		    WebflowErrorUtil.addError(request, key, new WebflowError(message, ex));
		    ProcessHelper.processForwardError(destNode, request, response);
		    rollbackTransaction(request, flowContext);
		}
		else if(ex instanceof NoWebflowNodeAPException)
		{
		    String key = destNode.getName() + ".access.error";
		    String message = "----PermissionError: access the webflow node" + 
		                destNode.getName() + " error ";
		    logger.error("*******" + destNode.getName() + ".webflowNode access denied!", ex);
		    
		    WebflowErrorUtil.addError(request, key, new WebflowError(message, ex));
		    ProcessHelper.processForwardError(destNode, request, response);
		    rollbackTransaction(request, flowContext);
		}
		else if (ex instanceof WebFlowException)
		{
		    String nodename = destNode.getName();
		    String message = "execute the node " + destNode.toString() + " error ";
		    String key = nodename + ".execute.error";
		    Throwable t = ((WebFlowException)ex).getNestedThrowable();
		    String nestedMessage = "";
			if (t != null && t.getMessage() != null) {
				nestedMessage = t.getMessage();
			}
			if (t instanceof ParsingException) {
				key = nodename + ".parsing.error";
				message += "----ParsingError:" + nestedMessage;
			} else if (t instanceof BusinessOperationException) {
				key = nodename + ".bo.error";
				message += "----BusinessOperationError:"
						+ nestedMessage;
			} else if (t instanceof UIPageException) {
				key = nodename + ".uipage.error";
				message += "----UIPageError:" + nestedMessage;
			}
      
			else if (t != null) {
				message += "----" + nestedMessage;
			}
      
		    logger.error("*******execute the node " + destNode.toString() + " error ", ex);
      
		    WebflowErrorUtil.addError(request, key, new WebflowError(message, t));
		    ProcessHelper.processForwardError(destNode, request, response);
      
		    rollbackTransaction(request, flowContext);
		}
		else
		{
		    String message = "execute the node " + destNode.toString() + " error ";
		    logger.error("*******" + message, ex);
		    message = message + "------" + ex.getMessage();
		    String key = destNode.getName() + ".execute.error";
      
		    WebflowErrorUtil.addError(request, key, new WebflowError(message, ex));
		    ProcessHelper.processForwardError(destNode, request, response);
      
		    rollbackTransaction(request, flowContext);
		}
	}

    /**
     * Render the HTTP headers to defeat browser caching if requested.
     *
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    private void processNoCache(HttpServletResponse response)
            //throws IOException, ServletException
    {
		if (!nocache) {
			return;
		}
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 1);

    }
    
    /**
     * find:
     *  <li>  find sourcenode from request attributes
     *  <li>  find sourcenode from request parameters
     *  <li>  find pagename from request parameters
     *  <li> find
     *
     * Identify and return an appropriate source WebNode of this reqeust.
     * the information about source WebNode is stored in request.
     * the information is stored as attributes or parameters in request.
     *   If no such WebNode can be identified, return <code>null</code>.
     * The <code>request</code> parameter is available if you need to make
     * decisions on available mappings (such as checking permissions) based
     * on request parameters or other properties, but it is not used in the
     * default implementation.
     *
     * @param path Path component used to select a mapping
     * @param request The request we are processing
     */
    private WebNode processSourceWebNode(HttpServletRequest request, AttributesAccessor attrAccessor)
    {
        if (logger.isDebugEnabled())
            logger.debug("processSourceWebNode()");

        UIFlowCacheManager manager = UIFlowCacheManager.getInstance();
        
        //set attribute flag
        attrAccessor.setFlag(Boolean.TRUE);
        if(attrAccessor.nodeName == null)
        {
            //....do?_destchunkname=xxx&_destnodename=
            if (logger.isInfoEnabled())
                logger.info("processSourceWebNode():the nodename is null in request attribute, get nodename from parameter");
            //set attribute flag
            attrAccessor.setFlag(Boolean.FALSE);

            if (logger.isDebugEnabled())
                logger.debug("processSourceWebNode():the nodename is null in request parameter, get pagename");
            if(attrAccessor.entityName != null)
            {
                if (logger.isDebugEnabled())
                    logger.debug("processSourceWebNode(): source node sourceentity name: {}",
                    		new Object[]{attrAccessor.entityName});
                return manager.findWebNodeBySourceEntity(attrAccessor.entityName);
            }
        }

        String chunkName = attrAccessor.chunkName;
        String nodeName = attrAccessor.nodeName;
        if(chunkName == null || nodeName == null)
        {
            if (logger.isInfoEnabled())
                logger.info("processSourceWebNode(): chunkName is {}, nodeName is {}",
                		new Object[]{chunkName, nodeName});
            return null;
        }
        else
        {
            return manager.findWebNode(chunkName, nodeName);
        }
    }

    /**
     * find dest node:
     * <li> destnode in attributes
     * <li> srcnode != null: find outName in attributes or parameters
     * <li> srcnode == null: find destnode in parameters
     * Identify and return an appropriate WebNode with the source node and its
     *  out.  If no such WebNode can be identified, find the webnode
     *  by request uri path.
     *
     * @param srcNode the source node
     * @param path the request uri path
     * @param request The request we are processing
     */
    private WebNode processDestWebNode(WebNode srcNode, HttpServletRequest request, AttributesAccessor attrAccessor)
    {
        if (logger.isDebugEnabled())
            logger.debug("processDestWebNode()");

        if (logger.isDebugEnabled()) {
            logger.debug("processDestWebNode(): get destnode from request attribute");
        }
        
        UIFlowCacheManager manager = UIFlowCacheManager.getInstance();
        
        String destnodename = attrAccessor.destnodename;
        if(destnodename != null && !destnodename.isEmpty())
        {
            if (logger.isDebugEnabled())
                logger.debug("processDestWebNode(): the destnode in request attribute is " + destnodename);
            String destchunkname = attrAccessor.destchunkname;
            if((srcNode != null) &&
               (destchunkname == null || destchunkname.equals("")))
                destchunkname = srcNode.getChunk().getEntityName();
            attrAccessor.setAttribute(WebflowConstants.OUT_NAME, null);//don't do the convert
            attrAccessor.setAttribute(WebflowConstants.DEST_NODE_NAME, null);
            attrAccessor.setAttribute(WebflowConstants.DEST_CHUNK_NAME, null);

            if(destchunkname == null)
            {
                logger.error("processDestWebNode(): the destchunkname is null in request attribute, the destnodename is "
                             + destnodename);
                return null;
            }

            return manager.findWebNode(destchunkname, destnodename);
        }

        //attribute: outname
        //parameter: outname
        if(srcNode != null)
        {
            String outName = null;
            if(attrAccessor.getFlag())
            {
                if (logger.isDebugEnabled())
                    logger.debug("processDestWebNode(): find outname in request attribute");
                outName = attrAccessor.getOut();
            }
            if (outName == null)
            {
                if (logger.isDebugEnabled())
                    logger.debug("processDestWebNode(): find outname in request parameter");
                outName = request.getParameter(WebflowConstants.OUT_NAME);
            }

            if(outName != null)
            {
                if (logger.isDebugEnabled())
                    logger.debug("processDestWebNode():the outname is " + outName);
                attrAccessor.setOut(outName);
                //find out
                OutType out = srcNode.findOut(outName);
                if(out != null)
                {
                    NextType next  = out.getNext();
                    if(next != null)
                    {
                        return manager.findNextWebNode(srcNode, next);
                    }
                    else
                    {
                        logger.error("the next is null, out=" + outName + srcNode.toString());
                        return null;
                    }
                }
            }
        }
        //no source node or no out name
        //parameter: destnode
        if (logger.isDebugEnabled())
            logger.debug("processDestWebNode():finding destnode in request parameter");

        destnodename = attrAccessor.destnodename;
        String destchunkname = attrAccessor.destchunkname;

        if((srcNode != null) && (destchunkname == null || destchunkname.equals("")))
            destchunkname = srcNode.getChunk().getEntityName();

        attrAccessor.setAttribute(WebflowConstants.OUT_NAME, null);//don't do the convert
        attrAccessor.setAttribute(WebflowConstants.DEST_NODE_NAME, null);
        attrAccessor.setAttribute(WebflowConstants.DEST_CHUNK_NAME, null);
        if(destchunkname == null || destnodename == null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("cant find dynamicout destnode:destchunkname=" +
                    destchunkname + ",destnodename=" + destnodename);
            }
            return null;
        }
        return manager.findWebNode(destchunkname, destnodename);
    }
    
    private void rollbackTransaction(HttpServletRequest request, WebFlowContext context)
    {
        try
        {
            if(context.isInTransaction())
            {
                if (logger.isInfoEnabled())
                    logger.info("rollback the userTransaction");
                context.rollbackTransaction();
            }
        }
        catch (Exception e)
        {
            logger.error("error when rollback the user transaction, execute node "
                + toString(), e);
        }
        request.setAttribute(WebflowConstants.USERTRANSACTION_KEY, null);
    }

    protected boolean checkSessionTimeout(HttpServletRequest request)
    {
        //add request parameter check
        String needCheckInRequest = request.getParameter("_needCheckSessionTimeOut");
        if(!"true".equals(needCheckInRequest))
        {
            return false;
        }
        HttpSession session = request.getSession(false);
        if (session == null)
        {
            return true;
        }
        return (session.getAttribute(WebflowConstants.USER_SESSION_KEY) == null);
    }

}