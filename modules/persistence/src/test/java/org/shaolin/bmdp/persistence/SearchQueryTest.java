package org.shaolin.bmdp.persistence;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;

public class SearchQueryTest {

	@Test
	public void testEmpty() {}
	
	public void testSingleQuery() {
		
		AppContext.register(IServerServiceManager.INSTANCE);
		
		HibernateUtil.getSession();
		
		Assert.assertEquals(HibernateUtil.getSession().hashCode(), HibernateUtil.getSession().hashCode());
		getProductTypeGroup();
		
		HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		
	}
	
	private void getProductTypeGroup() {
    	String sql = "SELECT p.type, count(p.type) FROM PROD_PRODUCTTEMPLATE p group by p.type;";
    	Session session = HibernateUtil.getSession();
    	SQLQuery sqlQuery = session.createSQLQuery(sql);
    	List<Object[]> list = sqlQuery.list();
    	System.out.println(list);
    }
	
	public void testMultipleQuery() {
		
		System.out.println("\n\n\n");
		System.out.println("-----------------testTransaction--------------");
		
		AppContext.register(IServerServiceManager.INSTANCE);
		
		for (int i =0; i<10; i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " start");
					HibernateUtil.getSession();
					getProductTypeGroup();
					HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
					System.out.println(Thread.currentThread().getName() + " end");
				}
			}).start();
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
	
	public void testTransactionTimeout() {
		AppContext.register(IServerServiceManager.INSTANCE);
		Session session = HibernateUtil.getSession();
		getProductTypeGroup();
		System.out.println("Hibernate Session Info: collections-{"+session.getStatistics().getCollectionCount()+
				"},entities-{"+session.getStatistics().getEntityCount()+"},transaction-{"+session.getTransaction().getLocalStatus()+"}"); 
		Assert.assertEquals("ACTIVE", session.getTransaction().getLocalStatus().toString());
		
		
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				HibernateUtil.getSession();
			}
		}).start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
	
}
