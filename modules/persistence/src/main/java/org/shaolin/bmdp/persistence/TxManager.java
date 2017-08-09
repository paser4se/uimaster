package org.shaolin.bmdp.persistence;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.TransactionManagerServices;

public class TxManager {

	private static final Logger logger = LoggerFactory.getLogger(TxManager.class);
	
	public static final String DATASOURCE_NAME = "uimaster";
	
	protected Context context = null;
	
	public Context getNamingContext() throws NamingException {
		if (context == null) {
			Hashtable<String, String> prop = new Hashtable<String, String>();
			prop.put("java.naming.factory.initial", "bitronix.tm.jndi.BitronixInitialContextFactory");
			context = new InitialContext(prop);
		}
        return context;
    }

    public UserTransaction getUserTransaction() {
        try {
            return (UserTransaction) getNamingContext()
                .lookup("java:comp/UserTransaction");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public DataSource getDataSource() {
        try {
            return (DataSource) getNamingContext().lookup(DATASOURCE_NAME);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public DataSource getDataSource(String name) {
        try {
            return (DataSource) getNamingContext().lookup(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void begin() throws Exception {
    	UserTransaction tx = getUserTransaction();
    	tx.begin();
    }

    public void commit() throws Exception {
    	UserTransaction tx = getUserTransaction();
    	tx.commit();
    }
    
    public void rollback() {
        UserTransaction tx = getUserTransaction();
        try {
            if (tx.getStatus() == Status.STATUS_ACTIVE ||
                tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                tx.rollback();
        } catch (Exception ex) {
            logger.warn("Rollback of transaction failed, trace follows!");
        }
    }

    public void stop() throws Exception {
        logger.info("Stopping database connection pool");
        TransactionManagerServices.getTransactionManager().shutdown();
    }

}
