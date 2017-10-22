package org.shaolin.bmdp.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.UserTransaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.shaolin.bmdp.persistence.be.OrganizationImpl;
import org.shaolin.bmdp.persistence.be.PersonalInfoImpl;
import org.shaolin.bmdp.persistence.be2.OrganizationImpl2;
import org.shaolin.bmdp.persistence.be2.PersonalInfoImpl2;
import org.shaolin.bmdp.persistence.query.operator.Operator;
import org.shaolin.bmdp.runtime.SpringBootTestRoot;

/**
 * 
 * 
 * @author wushaol
 *
 */
public class TransactionTest extends SpringBootTestRoot {

	/**
	-- TEST_ORGANIZATION
	CREATE TABLE TEST_ORGANIZATION
	(ID BIGINT(38) NOT NULL AUTO_INCREMENT,
	 PARENTID BIGINT(38),
	 ORGCODE VARCHAR(38),
	 NAME VARCHAR(255),
	 DESCRIPTION VARCHAR(255),
	 TYPE VARCHAR(100) DEFAULT -1,
	 ORGTYPE INT(2) DEFAULT 0,
	 VERISTATE INT(2) DEFAULT 0,
	 VERIFIER BIGINT(38),
	 CREATEDATE DATETIME,
	 _enable INT(2) DEFAULT 1,
	 PRIMARY KEY(ID)
	);
	
	-- TEST_PERSONINFO
	CREATE TABLE TEST_PERSONINFO
	(ID BIGINT(38) NOT NULL AUTO_INCREMENT,
	 FIRSTNAME VARCHAR(20),
	 LASTNAME VARCHAR(50),
	 ORGCODE VARCHAR(38),
	 ORGID BIGINT(38),
	 EMPLEVEL INT(2),
	 GENDER INT(2),
	 MARRIED TINYINT(1),
	 IDENTITYCARDID VARCHAR(255),
	 DISCRIPTION VARCHAR(255),
	 BIRTHDAY DATETIME,
	 LANGUAGE INT(2),
	 EDUCATION INT(2),
	 MAJOR VARCHAR(255),
	 COMMENT VARCHAR(255),
	 CREATEDATE DATETIME,
	 TYPE VARCHAR(100) DEFAULT -1,
	 _enable INT(2) DEFAULT 1,
	 PRIMARY KEY(ID)
	);
	*/
	
	private UserTransaction getUserTransaction() throws NamingException {
		Hashtable<String, String> prop = new Hashtable<String, String>();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
		prop.put(Context.URL_PKG_PREFIXES, "bitronix.tm.jndi");
		Context context = new InitialContext(prop);
		return (UserTransaction) context.lookup("java:comp/UserTransaction");
	}
	
	@Test
	public void testNoTransaction() {
		OrganizationImpl org = new OrganizationImpl();
		org.setName("test" + (int)(Math.random()*1000));
		org.setDescription("test org for no transaction!");
		Session session = HibernateUtil.openSession();
		session.save(org);
		session.close();
	}
	
	@Test
	public void testMultipleThread() {
		final CountDownLatch latch = new CountDownLatch(20);
		for(int i=0; i<20; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						UserTransaction tx = getUserTransaction();
						System.out.println(tx.hashCode() + "--" + tx.toString());
						tx.begin();
						tx.commit();
						latch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
						Assert.fail();
					}
				}
			}).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
		}
		HibernateUtil.printThreadCounter();
	}
	
	@Test
	public void testNestedTransaction() throws Exception {
		UserTransaction tx = getUserTransaction();
		tx.begin();
		
		UserTransaction tx1 = getUserTransaction();
		try {
			tx1.begin();
			tx1.commit();
			Assert.fail();
		} catch (NotSupportedException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("before commit: " + tx.getStatus());
		tx.commit();
		System.out.println("after commit: " + tx.getStatus());
		
		// again.
		tx.begin();
		System.out.println("before commit: " + tx.getStatus());
		tx.commit();
		System.out.println("after commit: " + tx.getStatus());
		
		// again.
		tx.begin();
		System.out.println("before commit: " + tx.getStatus());
		tx.commit();
		System.out.println("after commit: " + tx.getStatus());
		
		// again.
		tx.begin();
		System.out.println("before rollback: " + tx.getStatus());
		tx.rollback();
		System.out.println("after rollback: " + tx.getStatus());
		
		// again.
		tx.begin();
		System.out.println("before rollback: " + tx.getStatus());
		tx.rollback();
		System.out.println("after rollback: " + tx.getStatus());
		
	}
	
	@Test
	public void testGetSessions() throws Exception {
		//case 1
		HibernateUtil.getSession();
		HibernateUtil.getSession(OrganizationImpl.class.getPackage().getName());
		HibernateUtil.getSession(OrganizationImpl.class.getPackage().getName());
		HibernateUtil.getSession(OrganizationImpl2.class.getPackage().getName());
		HibernateUtil.releaseSession(true);
		//again.
		HibernateUtil.releaseSession(true);
		
		//case 2
		HibernateUtil.getSession();
		HibernateUtil.getSession(OrganizationImpl.class.getPackage().getName());
		HibernateUtil.getSession(OrganizationImpl2.class.getPackage().getName());
		HibernateUtil.releaseSession(false);
		//again.
		HibernateUtil.releaseSession(false);
		
		//case 3
		HibernateUtil.getSession();
		HibernateUtil.getSession();
		HibernateUtil.releaseSession(true);
		
		HibernateUtil.printThreadCounter();
	}
	
	/**
	 * Caused by: bitronix.tm.internal.BitronixRollbackException: transaction timed out and has been rolled back
		at bitronix.tm.BitronixTransaction.commit(BitronixTransaction.java:250)
		at bitronix.tm.BitronixTransactionManager.commit(BitronixTransactionManager.java:143)
		at org.shaolin.bmdp.persistence.HibernateUtil.releaseSession(HibernateUtil.java:135)
		... 26 more
	 * @throws InterruptedException
	 */
	public void testTransactionTimeOut() throws InterruptedException {
		HibernateUtil.getSession();
		
		BEEntityDaoObject daoService = new BEEntityDaoObject();
		
		OrganizationImpl org = new OrganizationImpl();
		org.setName("test" + (int)(Math.random()*1000));
		org.setDescription("test org for testOnlyBeginTx()!");
		daoService.create(org);
		
		Thread.sleep(12000);
		HibernateUtil.releaseSession(true);
		HibernateUtil.releaseSession(true);
	}
	
	public void testOnlyBeginTx() throws Exception {
		try {
			BEEntityDaoObject daoService = new BEEntityDaoObject();
			
			OrganizationImpl org = new OrganizationImpl();
			org.setName("test" + (int)(Math.random()*1000));
			org.setDescription("test org for testOnlyBeginTx()!");
			daoService.create(org);
			
			Assert.assertTrue(org.getId() > 0);
			Assert.assertNotNull(daoService.get(org.getId(), OrganizationImpl.class));
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	
	@Test
	public void testMultiStepsFor2DBCommit() throws Exception {
		UserTransaction tx = HibernateUtil.getUserTransaction();
		try {
			BEEntityDaoObject daoService = new BEEntityDaoObject();
			
			OrganizationImpl org = new OrganizationImpl();
			org.setName("test" + (int)(Math.random()*1000));
			org.setDescription("test org!");
			daoService.create(org);
			
			Assert.assertTrue(org.getId() > 0);
			System.out.println("org.getId(): " + org.getId());
			
			PersonalInfoImpl info = new PersonalInfoImpl();
			info.setOrgId(org.getId());
			info.setFirstName("test" + (int)(Math.random()*1000));
			info.setLastName("aaaaa" + info.getFirstName());
			daoService.create(info);
			
			info.setDiscription("update for test");
			daoService.update(info);
			
			Criteria criteria = daoService._createCriteria(PersonalInfoImpl.class, "inFlow");
			criteria.add(daoService.createCriterion(Operator.START_WITH_RIGHT, "inFlow.firstName", info.getFirstName()));
            List result = daoService._list(0, -1, criteria);
            Assert.assertTrue(result.size() > 0);
            Assert.assertEquals("update for test", ((PersonalInfoImpl)result.get(0)).getDiscription());
            System.out.println("result: " + result);
            
            OrganizationImpl2 org2 = new OrganizationImpl2();
			org2.setName("test" + (int)(Math.random()*1000));
			org2.setDescription("test org!");
			daoService.create(org2);
			
			Assert.assertTrue(org2.getId() > 0);
			System.out.println("org.getId(): " + org2.getId());
			
			PersonalInfoImpl2 info2 = new PersonalInfoImpl2();
			info2.setOrgId(org2.getId());
			info2.setFirstName("test" + (int)(Math.random()*1000));
			info2.setLastName("aaaaa" + info2.getFirstName());
			daoService.create(info2);
			
			info2.setDiscription("update for test");
			daoService.update(info2);
			
			Criteria criteria1 = daoService._createCriteria(PersonalInfoImpl2.class, "inFlow");
			criteria1.add(daoService.createCriterion(Operator.START_WITH_RIGHT, "inFlow.firstName", info2.getFirstName()));
            List result1 = daoService._list(0, -1, criteria1);
            Assert.assertTrue(result1.size() > 0);
            Assert.assertEquals("update for test", ((PersonalInfoImpl2)result1.get(0)).getDiscription());
            System.out.println("result: " + result1);
            
			tx.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateUtil.printThreadCounter();
		}
	}
	
	@Test
	public void testMultiStepsFor2DBRollback() throws Exception {
		UserTransaction tx = HibernateUtil.getUserTransaction();
		try {
			BEEntityDaoObject daoService = new BEEntityDaoObject();
			
			OrganizationImpl org = new OrganizationImpl();
			org.setName("test" + (int)(Math.random()*1000));
			org.setDescription("test org!");
			daoService.create(org);
			
			System.out.println("org.getId(): " + org.getId());
			org.setDescription("update for org test");
			daoService.update(org);
			
			PersonalInfoImpl info = new PersonalInfoImpl();
			info.setOrgId(org.getId());
			info.setFirstName("test" + (int)(Math.random()*1000));
			info.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			daoService.create(info);
			
			OrganizationImpl2 org2 = new OrganizationImpl2();
			org2.setName("test" + (int)(Math.random()*1000));
			org2.setDescription("test org!");
			daoService.create(org2);
			
			tx.commit();
			Assert.fail();
			//HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		} catch (Throwable e) {
			tx.rollback();
			System.err.println(e.getMessage());
		} finally {
		}
	}
	
}
