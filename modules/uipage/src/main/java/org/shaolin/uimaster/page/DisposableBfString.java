package org.shaolin.uimaster.page;

import org.shaolin.bmdp.utils.ObjectPool;

/**
 * For good performance purpose, only internal used!
 *  
 * @author wushaol
 *
 */
public class DisposableBfString extends ObjectPool<StringBuilder> {

    private static final ThreadLocal<DisposableBfString> pool = new ThreadLocal<DisposableBfString>();

    private static final String EMTPY = "";
    
    public DisposableBfString(int minSize, int maxSize) {
    	super(minSize, maxSize);
    }
    
    @Override
	protected StringBuilder createObject() {
		return new StringBuilder(1024);
	}
    
    public static StringBuilder getBuffer() {
    	if (pool.get() == null) {
            pool.set(new DisposableBfString(3, -1));
        }
        return pool.get().acquire();
    }
    
    public static void release(StringBuilder sb)
    {
    	if (pool.get() != null) {
    		// better performance then setLength(0);
    		sb.delete(0, sb.length());
	    	pool.get().recycle(sb);
        }
    }

}
