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

import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Matrix;
import org.shaolin.uimaster.page.ajax.Widget;
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

    public HTMLMatrixType()
    {
    }

    public HTMLMatrixType(HTMLSnapshotContext context)
    {
        super(context);
    }

    public HTMLMatrixType(HTMLSnapshotContext context, String id)
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
        	String iconClick = (String)this.getEventListener("onclick");
        	if (iconClick != null) {
        		iconClick = this.getReconfigurateFunction(iconClick, false);
        	}
            generateWidget(context);
            context.generateHTML("<div class=\"uimaster_matrix\" type=\"\" id=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            context.generateHTML(">");
            
            List<List> blocks = (List<List>)this.removeAttribute("init");
			for (int i = 0; i < blocks.size(); i++) {
				context.generateHTML("<div class=\"uimaster_matrix_row\" i='"+i+"'>");
				List<Object> row = blocks.get(i);
				for (int j=0; j < row.size(); j++) {
					context.generateHTML("<div j='"+j+"' "+(j<row.size()-1?"style=\"float:left;\"":"")+"><span class=\"uimaster_matrix_col\">");
					Object v = row.get(j);
					if (v instanceof DataMode) {
						DataMode mode = (DataMode)v;
						context.generateHTML("<div onclick=\"" + iconClick + "('"+escapeTNR(mode.link)+"','"+mode.name+"');\" class=\""+mode.css+"\">");
						context.generateHTML(mode.name);
						context.generateHTML("</div>");
					} else {
						context.generateHTML(v.toString());
					}
					context.generateHTML("</span></div>");
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
    
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
    	try {
			ExpressionType initQueryExpr = (ExpressionType)this.removeAttribute("initExpr");
			Object initResult = ee.evaluateExpression(initQueryExpr);
			
			this.addAttribute("init", initResult);
    	} catch (EvaluationException e) {
			throw new IllegalStateException(e);
		}
    	
    	Matrix matrix = new Matrix(getName(), Layout.NULL);
    	matrix.setUIEntityName(getUIEntityName());
    	matrix.setListened(true);
    	matrix.setFrameInfo(getFrameInfo());
		return matrix;
    }

    public static class DataMode {
    	public String name;
    	public String image;
    	public String css;
    	public String link;
    }
}
