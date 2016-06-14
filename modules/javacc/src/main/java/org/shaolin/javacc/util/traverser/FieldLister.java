//package
package org.shaolin.javacc.util.traverser;

import java.util.ArrayList;
import java.util.List;

import org.shaolin.javacc.symbol.ExpressionNode;
import org.shaolin.javacc.symbol.FieldExpression;
import org.shaolin.javacc.symbol.FieldName;


/**
 * The util class to get all possible exceptions thrown by an expression
 *
 * @author shaolin
 */
public class FieldLister implements Traverser
{
    public FieldLister()
    {
        fieldList = new ArrayList();
    }
    
    public void traverse(ExpressionNode node)
    {
        if(node instanceof FieldExpression)
        {
            FieldExpression fieldExpression = (FieldExpression)node;
            int childNum = fieldExpression.getChildNum();
            ExpressionNode lastChildNode = fieldExpression.getChild(childNum - 1);
            if(lastChildNode instanceof FieldName)
            {
	            FieldName fieldNode = (FieldName)lastChildNode;
	            if(fieldNode.isField() || fieldNode.isCustomField() || fieldNode.isVariableNode())
	            {
	                fieldList.add(fieldExpression.toString());
	            }
	        }
        }
    }
    
    public String[] getAllFieldNames()
    {
        return (String[])fieldList.toArray(new String[]{});
    }
    
    public void reset()
    {
        fieldList.clear();
    }
    
    private List fieldList;

    public static final String ___REVISION___ = "$Revision: 1.2 $";
}
