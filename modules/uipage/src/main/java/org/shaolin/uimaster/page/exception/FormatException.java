package org.shaolin.uimaster.page.exception;

public class FormatException extends UIPageException
{
    public FormatException(String msg)
    {
        super(msg);   
    }
    
    public FormatException(String msg,Object[]args)
    {
    	super(msg,args);
    }
    
    public FormatException(String msg, Throwable e)
    {
        super(msg, e);   
    }
    
    public FormatException(String msg, Throwable t, Object[] args)
    {
        super(msg, t, args);
    }
}