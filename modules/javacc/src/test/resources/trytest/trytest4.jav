{
	try {
	// Improvement null pointer exception!
    java.util.Date d = null;
    d.setTime(432432L);  
    } finally {
       System.out.println("print before null point exception.");
    }
}