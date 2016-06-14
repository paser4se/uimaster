//package
package org.shaolin.javacc.util.traverser;

import java.util.ArrayList;
import java.util.List;

import org.shaolin.javacc.symbol.ExpressionNode;
import org.shaolin.javacc.symbol.FieldName;


/**
 * The util class to get all possible exceptions thrown by an expression
 *
 * @author shaolin
 */
public class VariableRenamer implements Traverser
{
    public VariableRenamer(String oldName, String newName)
    {
        this.oldName = oldName;
        this.newName = newName;
    }
    
    public void traverse(ExpressionNode node)
    {
        if(node instanceof FieldName)
        {
            FieldName fieldNode = (FieldName)node;
            if(fieldNode.isVariableNode())
            {
                String varName = fieldNode.getFieldName();
                if(varName.equals(oldName))
                {
                    fieldNode.setFieldName(newName);
                }
            }
        }
    }
    
    public String getOldName()
    {
        return oldName;
    }
    
    public String getNewName()
    {
        return newName;
    }    
    
    private String oldName;
    private String newName;   

    public static final String ___REVISION___ = "$Revision: 1.2 $";
}
