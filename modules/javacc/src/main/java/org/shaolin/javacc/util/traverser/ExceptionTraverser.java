//package
package org.shaolin.javacc.util.traverser;

import java.util.ArrayList;
import java.util.List;

import org.shaolin.javacc.symbol.ExpressionNode;


/**
 * The util class to get all possible exceptions thrown by an expression
 *
 * @author Xiao Yi
 */
public class ExceptionTraverser implements Traverser
{
    public ExceptionTraverser()
    {
        exceptionList = new ArrayList();
    }
    
    public void traverse(ExpressionNode node)
    {
        Class[] exceptions = node.getThrownExceptions();

        if(exceptions != null)
        {
            for(int i = 0, n = exceptions.length; i < n; i++)
            {
                Class exception = exceptions[i];
                if(!(RuntimeException.class.isAssignableFrom(exception)) && !exceptionList.contains(exception))
                {
                    exceptionList.add(exception);
                }
            }
        }
    }
    
    public Class[] getExceptions()
    {
        return (Class[])exceptionList.toArray(new Class[]{});
    }
    
    public void reset()
    {
        exceptionList.clear();
    }
    
    private List exceptionList;    

    public static final String ___REVISION___ = "$Revision: 1.2 $";
}
