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
package org.shaolin.uimaster.page.widgets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <div class="seatlist" style="margin:0 auto;width:352px">
 * 	<dl class="clear">
 * 		<dd>
 * 			<div class="TSUfEr"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="TSUfEr"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="BVKLdL"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="TSUfEr"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="TSUfEr"></div>
 * 		</dd>
 * 		<dd>
 * 			<div class="TSUfEr"></div>
 * 		</dd>
 * 	</dl>
 * </div>
 * 
 * @author wushaol
 *
 */
public class HTMLMatrixType extends HTMLTextWidgetType 
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HTMLMatrixType.class);

    public HTMLMatrixType(String id)
    {
        super(id);
    }

    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
        	String iconClick = (String)this.getEventListener("onclick");
        	if (iconClick != null) {
        		iconClick = this.getReconfigurateFunction(iconClick, false);
        	}
        	List<List> blocks = (List<List>)this.removeAttribute("init");
        	if (this.getAttribute("ceType") != null && blocks == null) {
        		blocks = HTMLMatrixType.getModulesInMatrix(this.getAttribute("ceType").toString());
        	}
        	if (blocks == null) {
        		throw new IllegalStateException("No initial data defined in UI widget: " + this.getId() + "!");
        	}
            generateWidget(context);
            context.generateHTML("<div class=\"uimaster_matrix\" type=\"\" id=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            context.generateHTML(">");
            
			for (int i = 0; i < blocks.size(); i++) {
				context.generateHTML("<div class=\"uimaster_matrix_row\" i='"+i+"'>");
				List<Object> row = blocks.get(i);
				for (int j=0; j < row.size(); j++) {
					context.generateHTML("<div j='"+j+"' class=\"uimaster_matrix_col\">");
					Object v = row.get(j);
					if (v instanceof DataMode) {
						DataMode mode = (DataMode)v;
						context.generateHTML("<div onclick=\"" + iconClick + "('"+escapeTNR(mode.link)+"','"+mode.name+"','"+mode.id+"');\" class=\""+mode.css+"\" alt='"+mode.name+"' nodeid='"+mode.id+"'>");
						context.generateHTML(mode.name);
						context.generateHTML("<div class=\"uimaster_matrix_desc\">");
						context.generateHTML(mode.description);
						context.generateHTML("</div>");
						context.generateHTML("</div>");
					} else {
						context.generateHTML("<span>");
						context.generateHTML(v.toString());
						context.generateHTML("</span>");
					}
					context.generateHTML("</div>");
				}
				context.generateHTML("</div>");
			}
            
            context.generateHTML("</div>");
            
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    private static String escapeTNR(String line)
    {
        if (line == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = line.length(); i < n; i++)
        {
            char c = line.charAt(i);
            switch (c)
            {
                case '\t':
                    sb.append("");
                    break;
                case '\n':
                    sb.append("");
                    break;
                case '\r':
                    sb.append("");
                    break;
                default:
                    sb.append(c);
            }
        }
        return new String(sb);
    }
    
    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
	    	if (this.containsAttribute("initExpr")) {
		    	try {
				ExpressionType initQueryExpr = (ExpressionType)this.removeAttribute("initExpr");
				Object initResult = ee.evaluateExpression(initQueryExpr);
				
				this.addAttribute("init", initResult);
		    	} catch (EvaluationException e) {
				throw new IllegalStateException(e);
			}
	    	}
//		JSONObject json = super.createJsonModel(ee);
//		json.getJSONObject("attrMap").remove("init");
//		return json;
	    	return null;
    }

    public static class DataMode implements Serializable {
    	public String id;
    	public String name;
    	public String description;
    	public String image;
    	public String css;
    	public String link;
    }
    
    public static List<List> getModulesInMatrix(String ceType) {
		List<IConstantEntity> items = CEUtil.getConstantEntities(ceType, false, null);
		ArrayList<DataMode> row = new ArrayList<DataMode>(items.size());
		for (IConstantEntity i : items) {
			DataMode item = new DataMode();
			item.id = i.getIntValue() + "";
			item.name = i.getDisplayName();
			item.description = i.getDisplayName().equals(i.getDescription()) ? "":i.getDescription();
			item.css = "";
			item.image = i.getConstantDecorator() != null ? i.getConstantDecorator().getIcon(): "";
			item.link = "";
			row.add(item);
		}
		List<List> result = new ArrayList<List>();
		result.add(row);
		return result;
	}
}
