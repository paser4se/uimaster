//package
package org.shaolin.javacc.symbol;

//imports
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.i18n.ExceptionConstants;
import org.shaolin.javacc.context.OOEEEvaluationContext;
import org.shaolin.javacc.context.OOEEParsingContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.javacc.util.ExpressionStringBuffer;
import org.shaolin.javacc.util.ExpressionUtil;


/**
 * The class for new allocation new expression node
 */

public class AllocationExpression extends ExpressionNode
{
    public AllocationExpression()
    {
        super("AllocationExpression");
    }
    
	public Class checkType(OOEEParsingContext context) throws ParsingException
	{
	    super.isVariable = true;
	    
	    ExpressionNode typeNode = getChild(0);
			
		setValueClass(typeNode.checkType(context));
        
        if(typeNode instanceof FieldExpression)
        {		    
		    FieldExpression field = (FieldExpression)typeNode;
		    if(!field.isClass())
		    {
                String className = field.toString();
               // throw new ParsingException("Can't find the class " + className + " for new expression");
                throw new ParsingException(ExceptionConstants.EBOS_OOEE_034,new Object[]{className});
		    }
        }
        
		ExpressionNode init = getChild(1);
		
		if(init instanceof Argument)
		{
		    try
		    {
        	    init.checkType(context);
        	    Argument arg = (Argument)init;
        	    List argClasses = arg.getArgClasses();
        	    constructor = ExpressionUtil.findConstructor(valueClass, argClasses);
        	    super.setThrownExceptions(constructor.getExceptionTypes());
           	}
        	catch(ParsingException e)
        	{
        	    throw e;
        	}
		}
		else
		{
		    setValueClass(init.checkType(context));
		}
    	
    	return valueClass;
	}
	
	protected void evaluateNode(OOEEEvaluationContext context) throws EvaluationException
	{
        try
        {
            getValueClass();
        }
        catch(ParsingException e)
        {
        	throw new EvaluationException(ExceptionConstants.EBOS_000,e);
          //  throw new EvaluationException(e);
        }
        
        ExpressionNode init = getChild(1);

		if(init instanceof Argument)
		{
		    try
		    {
        	    Argument arg = (Argument)init;

        	    init.evaluate(context);
        	    
        	    List argClasses = arg.getArgClasses();
        	    
        	    if (constructor == null)
        	        constructor = ExpressionUtil.findConstructor(valueClass, argClasses);
        	    
        	    List argObjects = new ArrayList();
        	    
        	    for(int i = 0, n = argClasses.size(); i < n; i++)
        	    {
        	    	argObjects.add(0, context.stackPop());
        	    }
        	    
        	    context.stackPush(constructor.newInstance((Object[])(argObjects.toArray(new Object[]{}))));
        	}
        	catch(Exception e)
        	{
        		throw new EvaluationException(ExceptionConstants.EBOS_000,e);
        	}
		}
		else
		{
		    init.evaluate(context);
		}
	}
	
	public void appendToBuffer(ExpressionStringBuffer buffer)
	{
		buffer.appendSeperator(this, "new ");
		for(int i = 0, childNum = getChildNum(); i < childNum; i++)
		{
		    buffer.appendExpressionNode(getChild(i));
		}
	}
	
	/*
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("new ");
		
		for(int i = 0, childNum = getChildNum(); i < childNum; i++)
		{
		    buffer.append(getChild(i).toString());
		}
		
		return buffer.toString();
	}
	*/
	
	/* constructor object cache */
	private transient volatile Constructor constructor;
	
	static final long serialVersionUID = 0x2EC43588D56D770EL;

    public static final String ___REVISION___ = "$Revision: 1.8 $";
}
