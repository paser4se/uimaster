//package
package org.shaolin.javacc.symbol;

//imports
import org.shaolin.javacc.context.OOEEEvaluationContext;
import org.shaolin.javacc.context.OOEEParsingContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.javacc.util.ExpressionStringBuffer;




/**
 * The class for argumentlist expression node
 */

public class ArgumentList extends ExpressionNode
{
    public ArgumentList()
    {
        super("ArgumentList");
    }
    
	//do nothing, let parent argument node check children's types
	public Class checkType(OOEEParsingContext context) throws ParsingException
	{
		return null;
	}
	
	//do nothing, let parent argument node evaluate children's values
	protected void evaluateNode(OOEEEvaluationContext context) throws EvaluationException
	{
	}
	
	public void appendToBuffer(ExpressionStringBuffer buffer)
	{
        int childNum = getChildNum();
        for(int i = 0; i < childNum; i++)
        {
            ExpressionNode child = getChild(i);
            buffer.appendExpressionNode(child);
            if(i != childNum - 1)
            {
                buffer.appendSeperator(this, ", ");
            }
        }
	}

	/*
	public String toString()
	{
        StringBuffer buffer = new StringBuffer();
        int childNum = getChildNum();
        for(int i = 0; i < childNum; i++)
        {
            ExpressionNode child = getChild(i);
            buffer.append(child.toString());
            if(i != childNum - 1)
            {
                buffer.append(", ");
            }
        }
	
		return buffer.toString();
	}
	*/
	
	static final long serialVersionUID = 0x2EC43588D56D770EL;

    public static final String ___REVISION___ = "$Revision: 1.2 $";
}
