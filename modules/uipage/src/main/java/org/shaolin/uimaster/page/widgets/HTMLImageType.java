package org.shaolin.uimaster.page.widgets;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Image;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLImageType extends HTMLTextWidgetType
{
	private static final long serialVersionUID = 24011835838546883L;
	
    private static final Logger logger = LoggerFactory.getLogger(HTMLImageType.class);

    public HTMLImageType()
    {
    }

    public HTMLImageType(HTMLSnapshotContext context)
    {
        super(context);
    }

    public HTMLImageType(HTMLSnapshotContext context, String id)
    {
        super(context, id);
    }

    @Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
        	String root = UserContext.isAppClient() ? WebConfig.getAppContextRoot() : WebConfig.getResourceContextRoot();
        	String imageRoot = WebConfig.getAppImageContextRoot(context.getRequest());
        	if (this.getAttribute("captureScreen") != null) {
        		HTMLUtil.generateTab(context, depth);
                context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/html2canvas.js\"></script>");
            }
            generateWidget(context);
            if (this.getAttribute("isGallery") != null) {
	        			
//	        	Because JGallary has a bug on importing these files in multiple time.
//	            Please refer to org_shaolin_vogerp_productmodel runconfig.registry.
    			if (context.getRequest().getAttribute("_hasGallery") == null) {
    				context.getRequest().setAttribute("_hasGallery", Boolean.TRUE);
		            HTMLUtil.generateTab(context, depth);
		            context.generateHTML("<link rel=\"stylesheet\" href=\""+root+"/css/jsgallery/font-awesome.min.css\" type=\"text/css\">");
		            HTMLUtil.generateTab(context, depth);
		            context.generateHTML("<link rel=\"stylesheet\" href=\""+root+"/css/jsgallery/jgallery.min.css?v=1.5.0\" type=\"text/css\">");
		            HTMLUtil.generateTab(context, depth);
		        	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/jsgallery/jgallery.js\"></script>");
		        	HTMLUtil.generateTab(context, depth);
		        	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/jsgallery/touchswipe.js\"></script>");
		        	HTMLUtil.generateTab(context, depth);
		        	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/jsgallery/tinycolor-0.9.16.min.js\"></script>");
		        	HTMLUtil.generateTab(context, depth);
    			}
	        	context.generateHTML("<div id=\"");
	        	context.generateHTML(getName());
	            context.generateHTML("\" style=\"display:none;\">");
	            
	            HTMLUtil.generateTab(context, depth + 1);
	            String path = this.getValue();
	            if (path != null && !path.trim().isEmpty()) {
		            if (path.indexOf(";") != -1) {
		            	String[] images = path.split(";");
		            	for (String i : images) {
		            		String item = imageRoot + "/" +  i;
		            		context.generateHTML("<a href=\"" + item + "\"><img src=\"" + item + "\"/></a>");
		            	}
		            } else {
			            File directory = new File(WebConfig.getResourcePath() + path);
			            int dirCounter = 0;
			            if (directory.exists()) {
			            	String[] images = directory.list();
			            	for (String i : images) {
			            		File f = new File(directory, i);
			            		if (f.isDirectory()) {
			            			dirCounter ++;
			            		}
			            	}
			            }
			            if (directory.exists()) {
			            	String[] images = directory.list();
			            	if (dirCounter > 0) {
			            		context.generateHTML("<div class=\"album\" data-jgallery-album-title=\""+directory.getName()+"\">");
			            	}
			            	for (String i : images) {
			            		File f = new File(directory, i);
			            		if (f.isFile()) {
				            		String item = imageRoot + path + "/" +  i;
				            		context.generateHTML("<a href=\""+ item +"\"><img src=\""+ item +"\" alt=\""+i+"\"/></a>");
			            		}
			            	}
			            	if (dirCounter > 0) {
			            		context.generateHTML("</div>");
			            	}
			            	for (String i : images) {
			            		File f = new File(directory, i);
			            		if (f.isDirectory()) {
			            			genarateAblum(imageRoot + path + "/" + i, context, f);
			            		}
			            	}
			            } else {
			            	context.generateHTML("<a href=\"" + imageRoot + path + "\"><img src=\"" + imageRoot + path + "\"/></a>");
			            }
		            }
	            }
	            HTMLUtil.generateTab(context, depth);
	            context.generateHTML("</div>");
        	} else {
	            context.generateHTML("<input type=hidden name=\"");
	            context.generateHTML(getName());
	            context.generateHTML("\">");
	            context.generateHTML("<img");
	            context.generateHTML(" src=\"");
	            context.generateHTML(getSrc(context));
	            context.generateHTML("\"");
	            generateAttributes(context);
	            generateEventListeners(context);
	            context.generateHTML(" style=\"cursor:pointer;\"/>");
	            if (this.getAttribute("showWords") != null) {
	            	context.generateHTML("<span>" + this.getAttribute("text") + "</span>");
	            }
        	}
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }
    
    private void genarateAblum(String imageRoot, HTMLSnapshotContext context, File directory) {
    	String[] images = directory.list();
    	context.generateHTML("<div class=\"album\" data-jgallery-album-title=\""+directory.getName()+"\">");
    	for (String i : images) {
    		File f = new File(directory, i);
    		if (f.isFile()) {
        		String item = imageRoot + "/" +  i;
        		context.generateHTML("<a href=\""+ item +"\"><img src=\""+ item +"\" alt=\""+i+"\"/></a>");
    		}
    	}
    	context.generateHTML("</div>");
    }

    private String getSrc(HTMLSnapshotContext context)
    {
        return context.getImageUrl(getUIEntityName(), (String) getAllAttribute("src"));
    }

    public void generateAttribute(HTMLSnapshotContext context, String attributeName, Object attributeValue) throws IOException
    {
        if ("src".equals(attributeName))
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    /**
     * Whether this component can have editPermission.
     */
    @Override
    public boolean isEditPermissionEnabled()
    {
        return false;
    }
    
	public Widget createAjaxWidget(VariableEvaluator ee) {
		Image image = new Image(getName(), Layout.NULL);

		image.setReadOnly(isReadOnly());
		image.setUIEntityName(getUIEntityName());

		if (getValue() != null && !"".equals(getValue())) {
			image.setSrc(getValue());
		} else {
			image.setSrc((String) getAllAttribute("src"));
		}
		image.setIsGallery(this.getAttribute("isGallery") != null);
		image.setListened(true);
		image.setFrameInfo(getFrameInfo());

		Object expr = this.removeAttribute("selectedImageExpr");
		if (expr != null) {
			image.setSelectedImageExpr((ExpressionType)expr);
		}
		
		return image;
	}
	
	public static String generateSimple(HttpServletRequest request, String srcs, int width, int height) {
		if (srcs == null || srcs.trim().length() == 0) {
			return "";
		}
		
		String imageRoot = WebConfig.getAppImageContextRoot(request);
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		String[] list = srcs.split(",");
		for (String image : list) {
			File directory = new File(WebConfig.getResourcePath() + image);
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (File f : files) {
            		if (f.isFile()) {
	            		String realPath = image + "/" +  f.getName();
	            		sb.append("<img src=\"").append(imageRoot).append(realPath);
	    				sb.append("\" style=\"width:").append(width).append("px;height:").append(height).append("px;\">");
            		}
            	}
			} else {
				if (image.startsWith("http") || image.startsWith("https")) {
					sb.append("<img src=\"").append(image);
				} else {
					sb.append("<img src=\"").append(imageRoot).append(image);
				}
				sb.append("\" style=\"width:").append(width).append("px;height:").append(height).append("px;\">");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}

}
