// package
package org.shaolin.bmdp.persistence.query.mapping;

public interface ICompositeFieldHandler {
	
	public Object fromCache(Object[] values);

	public Object[] toCache(Object value);

}
