package org.shaolin.bmdp.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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
		prop.put("java.naming.factory.initial", "bitronix.tm.jndi.BitronixInitialContextFactory");
		Context context = new InitialContext(prop);
		return (UserTransaction) context.lookup("java:comp/UserTransaction");
	}
	
	ReentrantLock lock = new ReentrantLock();
	
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
		tx.commit();
	}
	
	@Test
	public void testMultiStepsForCommit() throws Exception {
		UserTransaction tx = getUserTransaction();
		try {
			tx.begin();
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
            
			tx.commit();
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	
	@Test
	public void testMultiStepsForRollback() throws Exception {
		UserTransaction tx = getUserTransaction();
		try {
			tx.begin();
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
			
			tx.commit();
			Assert.fail();
			//HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		} catch (Throwable e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
		}
	}
	
}
