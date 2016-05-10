package org.shaolin.bmdp.persistence;

import org.hibernate.Session;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;

public class SearchQueryTest {

	@Test
	public void sqContentTest() {
		
		AppContext.register(new AppServiceManagerImpl("test", SearchQueryTest.class.getClassLoader()));
		
		HibernateUtil.getSession();
		HibernateUtil.getSession();
		HibernateUtil.getSession();
		HibernateUtil.getSession();
		HibernateUtil.getSession();
		HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		
	}
	
}
