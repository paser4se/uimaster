//package
package org.shaolin.javacc.util;

import java.util.ArrayList;
import java.util.List;

import org.shaolin.javacc.symbol.ExpressionNode;


/**
 * The util class to regenerate an expression string from the parsed expression node
 * 
 * @author shaolin
 *
 */
public interface ExpressionStringBuffer
{
	public void appendExpressionNode(ExpressionNode node);
	public void appendSeperator(ExpressionNode currentNode, String seperator);

    public static final String ___REVISION___ = "$Revision: 1.3 $";
}
