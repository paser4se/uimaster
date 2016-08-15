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
package org.shaolin.uimaster.page.ajax;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.utils.StringUtil;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.IJSHandlerCollections;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.od.ODContext;

public class Image extends TextWidget implements Serializable
{
    private static final long serialVersionUID = 3140747849841049235L;
    
    private String src;
    
    private List<String> links;// the customized links.
    
    private boolean isgallery;

    private String selectedImage;
    
    private ExpressionType selectedImageExpr;

	public Image(String uiid)
    {
        this(AjaxActionHelper.getAjaxContext().getEntityPrefix() + uiid, new CellLayout());
        this.setListened(true);
    }
    
    public Image(String uiid, String src)
    {
        this(AjaxActionHelper.getAjaxContext().getEntityPrefix() + uiid, new CellLayout());
        this.setSrc(src);
        this.setListened(true);
    }
    
    public Image(String id, Layout layout)
    {
        super(id, layout);
    }

    public void setIsGallery(boolean isgallery) {
    	this.isgallery = isgallery;
    }
    
    public void addAttribute(String name, Object value, boolean update)
    {
    	if (!this.isgallery) {
    		return;
    	}
    	
		if ("selectedImage".equals(name)) {
			this.selectedImage = value.toString();
			
			if (this.selectedImageExpr != null) {
				try {
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
					DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
					evaContext.setVariableValue("selectedImage", this.selectedImage);
					evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
					ooeeContext.setDefaultEvaluationContext(evaContext);
					ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
					ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);
					this.selectedImageExpr.evaluate(ooeeContext);
				} catch (Exception e) {
					logger.error("error occurrs after selecting image: " + this.getId(), e);
				}
			}
		} 
    }
    
    public void setSrc(String src)
    {
    	if(src == null)
    	{
    		this.src = "";
    	}
    	else
    	{
    		this.src = src;
    		if (!this.src.startsWith(WebConfig.getWebContextRoot())) {
    			this.src = WebConfig.getWebContextRoot() + this.src;
    		}
    	}
        addAttribute("src",this.src);
    }

    public void updateCustLinks(List<String> links) {
    	this.links = links;
    	
    	StringBuilder html = new StringBuilder();
    	html.append("<div class=\"swiper-wrapper\">");
        
    	for (String item : links) {
    		html.append("<span class=\"swiper-slide\" style=\"background-image:url(" + item + ")\"/></span>");
    	}
        html.append("</div>");
        
        IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.GALLERY_REFRESH);
		dataItem.setData(StringUtil.escapeHtmlToBytes(html.toString()));
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
    }
    
    public String getSrc()
    {
        return src;
    }
    
    public String generateJS()
    {
        StringBuffer js = new StringBuffer(200);
        js.append("defaultname.");
        js.append(getId());
        js.append("=new UIMaster.ui.image({");
        js.append("ui:elementList[\"");
        js.append(getId());
        js.append("\"]});");
        return js.toString();
    }    
    
    public String generateHTML()
    {
    	StringBuilder html = new StringBuilder();

        generateWidget(html);
        
        if (isgallery) {
        	html.append("<div id=\"");
        	html.append(getId());
            html.append("\">");
            html.append("<div class=\"swiper-wrapper\">");
            String path = this.getAttribute("src").toString();
            
            String root = WebConfig.getResourceContextRoot();
            if (UserContext.isAppClient()) {
            	root = WebConfig.getAppImageContextRoot(AjaxActionHelper.getAjaxContext().getRequest());
            }
            File directory = new File(WebConfig.getResourcePath() + path);
            if (directory.exists()) {
            	String[] images = directory.list();
            	for (String i : images) {
            		String item = root + path + "/" +  i;
            		html.append("<span class=\"swiper-slide\" style=\"background-image:url(" + item + ")\"/></span>");
            	}
            }
            html.append("</div>");
    		html.append("</div>");
        } else {
	        html.append("<input type=\"hidden\" name=\"");
	        html.append(getId());
	        html.append("\">");
	        html.append("<img");
	        html.append(" src=\"");
	        html.append(this.isValueMask() ? WebConfig.getHiddenValueMask() : src);
	        html.append("\"");
	        generateAttributes(html);
	        generateEventListeners(html);
	        html.append(" />");
        }
        
        return html.toString();
    }
    
    public void createAlbum(String name) {
    	File root = new File(WebConfig.getRealPath("/images"));
		if (root.exists()) {
			String[] files = root.list();
			for (String f: files) {
				if (f.equals(name)) {
					return;
				}
			}
			File newFolder = new File(root, name.toString().replace('/', '.'));
			newFolder.mkdir();
			
			this.refresh();
		}
    }
    
	public List<String> getAllAlbums() {
		File root = new File(WebConfig.getRealPath("/images"));
		if (root.exists()) {
			ArrayList<String> all = new ArrayList<String>();
			String[] files = root.list();
			for (String f : files) {
				File newFolder = new File(root, f);
				if (newFolder.isDirectory()) {
					all.add(f);
				}
			}
			return all;
		}
		return Collections.emptyList();
	}
    
	public void clearAll() {
		if (!this.isgallery) {
    		return;
    	}
		
		String path = this.src;
		if (path.startsWith(WebConfig.getWebRoot())) {
			path = path.substring(WebConfig.getWebRoot().length());
		}
    	File directory = new File(WebConfig.getResourcePath() + File.separator + path);
        if (directory.exists()) {
        	String[] images = directory.list();
        	for (String i : images) {
        		File f = new File(directory, i);
        		logger.warn("Deleted the file: " + f.getAbsolutePath());
        		f.delete();
        	}
        }
		
		refresh();
	}
	
    public void refresh() {
    	if (!this.isgallery) {
    		return;
    	}
    	
    	if (this.links != null && this.links.size() > 0) {
    		updateCustLinks(this.links);
    		return;
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	String path = this.src;
		if (path.startsWith(WebConfig.getWebRoot())) {
			path = path.substring(WebConfig.getWebRoot().length());
		}
    	File directory = new File(WebConfig.getResourcePath() + File.separator + path);
        if (directory.exists()) {
        	String[] images = directory.list();
        	sb.append("<div class=\"swiper-wrapper\">");
        	for (String i : images) {
        		File f = new File(directory, i);
        		if (f.isFile()) {
            		String item = this.src + "/" +  i;
            		if (UserContext.isAppClient()) {
            			WebConfig.getAppImageContextRoot(AjaxActionHelper.getAjaxContext().getRequest());
            		}
            		sb.append("<span class=\"swiper-slide\" style=\"background-image:url("+ item +")\" alt=\""+i+"\"/></span>");
        		}
        	}
        	sb.append("</div>");
        }
        
        IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.GALLERY_REFRESH);
		dataItem.setData(StringUtil.escapeHtmlToBytes(sb.toString()));
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
    }

    private void genarateAblum(String root, StringBuilder sb, File directory) {
    	String[] images = directory.list();
    	sb.append("<div class=\"album\" data-jgallery-album-title=\""+directory.getName()+"\">");
    	for (String i : images) {
    		File f = new File(directory, i);
    		if (f.isFile()) {
        		String item = root + "/" +  i;
        		if (UserContext.isAppClient()) {
        			WebConfig.getAppImageContextRoot(AjaxActionHelper.getAjaxContext().getRequest());
        		}
        		sb.append("<a href=\""+ item +"\"><img src=\""+ item +"\" alt=\""+i+"\"/></a>");
    		}
    	}
    	sb.append("</div>");
    }
    
    /**
     * Whether this component can have editPermission.
     */
    public boolean isEditPermissionEnabled()
    {
        return false;
    }

    public String getSelectedImage() {
    	return this.selectedImage;
    }
    
	public void setSelectedImageExpr(ExpressionType selectedImageExpr) {
		this.selectedImageExpr = selectedImageExpr;
	}
}
