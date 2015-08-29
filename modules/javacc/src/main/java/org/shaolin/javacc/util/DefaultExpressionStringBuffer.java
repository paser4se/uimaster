//package
package org.shaolin.javacc.util;

import java.util.ArrayList;
import java.util.List;

import org.shaolin.javacc.symbol.ExpressionNode;


/**
 * The default implemention class for interface ExpressionStringBuffer
 * 
 * @author Xiao Yi
 *
 */
public class DefaultExpressionStringBuffer implements ExpressionStringBuffer
{
	StringBuffer buffer = new StringBuffer();
	
	public void appendExpressionNode(ExpressionNode node)
	{
		node.appendToBuffer(this);
	}
	
	public void appendSeperator(ExpressionNode currentNode, String seperator)
	{
		buffer.append(seperator);
	}
	
	public String getBufferString()
	{
		return buffer.toString();
	}

    public static final String ___REVISION___ = "$Revision: 1.3 $";
}
