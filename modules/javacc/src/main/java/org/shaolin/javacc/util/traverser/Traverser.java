//package
package org.shaolin.javacc.util.traverser;

import org.shaolin.javacc.symbol.ExpressionNode;

/**
 * The util interface which performs traversing the syntax tree
 *
 * @author Xiao Yi
 */
public interface Traverser
{
    public void traverse(ExpressionNode node);

    public static final String ___REVISION___ = "$Revision: 1.2 $";
}
