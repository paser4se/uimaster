package org.shaolin.bmdp.persistence;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

@Configuration
@ConfigurationProperties(prefix = "persistentConstant")
//@EnableTransactionManagement
public class PersistentConfig {

	private static final String DEFAULT_DATASOURCE = "dataSource";

	@Autowired
	private ApplicationContext springContext;
	
	private List<DataSourceConfig> dataSources;
	
	private JtaTransactionManager txManager;// share it.
	
	public PersistentConfig() {
	}
	
	public ApplicationContext getSpringContext() {
		return springContext;
	}

	public void setSpringContext(ApplicationContext springContext) {
		this.springContext = springContext;
	}

	public List<DataSourceConfig> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSourceConfig> dataSources) {
		this.dataSources = dataSources;
	}

	@Bean("dataSource")
	@Primary
	public DataSource dataSource() {
		for (DataSourceConfig ds : this.getDataSources()) {
			if (ds.getName().equals(DEFAULT_DATASOURCE)) {
				continue;
			}
			ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) springContext;  
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext  
			        .getBeanFactory();  
			// register all data sources except the default.
			DataSource dataSource = null;
			if (ds.getClassName().equals("org.apache.commons.dbcp.BasicDataSource")) {
				//design-time case supported!
				org.apache.commons.dbcp.BasicDataSource derbyDataSource = new org.apache.commons.dbcp.BasicDataSource(); 
				derbyDataSource.setDriverClassName(ds.getDriver());
				derbyDataSource.setUrl(ds.getUrl());
				derbyDataSource.setUsername(ds.getUsername());
				derbyDataSource.setPassword(ds.getPassword());
				dataSource = derbyDataSource;
			} else {
				PoolingDataSource dataSource0 = new PoolingDataSource();
				dataSource0.setClassName(ds.getClassName());
				dataSource0.setAutomaticEnlistingEnabled(true);
				dataSource0.setAllowLocalTransactions(true);
				dataSource0.setShareTransactionConnections(true);
				dataSource0.setUseTmJoin(true);
				dataSource0.setMinPoolSize(1);
				dataSource0.setMaxPoolSize(100);
				dataSource0.setUniqueName("jdbc/"+ ds.getName());
				Properties driverProperties = new Properties();
				driverProperties.put("url", ds.getUrl());
				driverProperties.put("user", ds.getUsername());
				driverProperties.put("password", ds.getPassword());
	//			driverProperties.put("journal", this.getDataSource().getPassword());
	//			driverProperties.put("log-part1-filename", "btm1.tlog");
	//			driverProperties.put("log-part2-filename", "btm2.tlog");
				
				dataSource0.setDriverProperties(driverProperties);
				dataSource0.init();
				defaultListableBeanFactory.registerSingleton(ds.getName(), dataSource0);
				dataSource = dataSource0;
			}
			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			vendorAdapter.setGenerateDdl(true);
			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setJpaVendorAdapter(vendorAdapter);
			factory.setDataSource(dataSource);
			factory.setPackagesToScan(ds.getHibernate().getPackagesToScan());
//				factory.setPersistenceUnitManager(persistenceUnitManager);
//				factory.setMappingResources(mappingResources);
			factory.afterPropertiesSet();
			defaultListableBeanFactory.registerSingleton(ds.getName() + "EntityManagerFactory", factory.getObject());
				
			LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
			sessionFactoryBean.setDataSource(dataSource);
			sessionFactoryBean.setJtaTransactionManager(txManager());
			sessionFactoryBean.setPackagesToScan(ds.getHibernate().getPackagesToScan());
			Properties hibernateProperties = new Properties();
			hibernateProperties.put("hibernate.dialect", ds.getHibernate().getDialect());
			hibernateProperties.put("hibernate.show_sql", ds.getHibernate().isShowsql());
			hibernateProperties.put("hibernate.format_sql", ds.getHibernate().isFormatsql());
			hibernateProperties.put("hibernate.hbm2ddl.auto", ds.getHibernate().getHbm2ddlauto());
			
			hibernateProperties.put("hibernate.connection.pool_size", ds.getHibernate().getConnection_pool_size());
			//Spring + Bitronix manages the transaction by default!
			//sessionFactoryBean.setJtaTransactionManager(jtaTransactionManager);
			//org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory
			//org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory
			hibernateProperties.put("hibernate.jndi.class", "bitronix.tm.jndi.BitronixInitialContextFactory");
			hibernateProperties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory");
			hibernateProperties.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
			hibernateProperties.put("hibernate.current_session_context_class", "jta"); //or thread binding.
			
			hibernateProperties.put("hibernate.cache.provider_class", ds.getHibernate().getCache_provider_class());
			hibernateProperties.put("hibernate.enable_lazy_load_no_trans", ds.getHibernate().isEnable_lazy_load_no_trans());
			hibernateProperties.put("hibernate.generate_statistics", ds.getHibernate().isGenerate_statistics());
			hibernateProperties.put("hibernate.connection.autoReconnet", ds.getHibernate().isAutoReconnet());
			hibernateProperties.put("hibernate.connection.autoReconnectForPools", ds.getHibernate().isAutoReconnectForPools());
			hibernateProperties.put("hibernate.connection.is-connection-validation-required", ds.getHibernate().isIsconnectionvalidationrequired());
			hibernateProperties.put("hibernate.connection.validationQuery", ds.getHibernate().getValidationQuery());
			
			sessionFactoryBean.setHibernateProperties(hibernateProperties);
			if (ds.getHbmRoot() != null && ds.getHbmRoot().trim().length() > 0) {
				sessionFactoryBean.setMappingDirectoryLocations(new FileSystemResource(ds.getHbmRoot()));
			}
			defaultListableBeanFactory.registerSingleton(ds.getName() + "SessionFactory", sessionFactoryBean);
			try {
				sessionFactoryBean.afterPropertiesSet();
			} catch (IOException e) {
				throw new IllegalStateException("Error to initialize session factory", e);
			}
			if (ds.getHibernate().getPackagesToScan().indexOf(',') == -1) {
				HibernateUtil.addSessionFactory(ds.getHibernate().getPackagesToScan(), sessionFactoryBean.getObject());
			} else {
				String[] packages = ds.getHibernate().getPackagesToScan().split(",");
				for (String p: packages) {
					HibernateUtil.addSessionFactory(p, sessionFactoryBean.getObject());
				}
			}
		}
		
		for (DataSourceConfig ds : this.getDataSources()) {
			if (ds.getName().equals(DEFAULT_DATASOURCE)) {
				// register the default data source.
				if (ds.getClassName().equals("org.apache.commons.dbcp.BasicDataSource")) {
					org.apache.commons.dbcp.BasicDataSource derbyDataSource = new org.apache.commons.dbcp.BasicDataSource(); 
					derbyDataSource.setDriverClassName(ds.getDriver());
					derbyDataSource.setUrl(ds.getUrl());
					derbyDataSource.setUsername(ds.getUsername());
					derbyDataSource.setPassword(ds.getPassword());
					return derbyDataSource;
				} else {
					PoolingDataSource dataSource0 = new PoolingDataSource();
					dataSource0.setClassName(ds.getClassName());
					dataSource0.setAutomaticEnlistingEnabled(true);
					dataSource0.setAllowLocalTransactions(true);
					dataSource0.setShareTransactionConnections(true);
					dataSource0.setUseTmJoin(true);
					dataSource0.setMinPoolSize(1);
					dataSource0.setMaxPoolSize(100);
					dataSource0.setUniqueName("jdbc/"+ ds.getName());
					
					Properties driverProperties = new Properties();
					driverProperties.put("url", ds.getUrl());
					driverProperties.put("user", ds.getUsername());
					driverProperties.put("password", ds.getPassword());
					driverProperties.put("password", ds.getPassword());
	//				driverProperties.put("journal", this.getDataSource().getPassword());
	//				driverProperties.put("log-part1-filename", "btm1.tlog");
	//				driverProperties.put("log-part2-filename", "btm2.tlog");
					
					dataSource0.setDriverProperties(driverProperties);
					dataSource0.init();
					return dataSource0;
				}
			}
		}
		throw new IllegalStateException("Default data source does not configure!");
	}
	
	@Bean("sessionFactory")
	@Primary
	@ConditionalOnBean(name = "dataSource")
	public LocalSessionFactoryBean sessionFactory() {
		DataSourceConfig ds0 = null;
		for (DataSourceConfig ds : this.getDataSources()) {
			if (ds.getName().equals(DEFAULT_DATASOURCE)) {
				ds0 = ds;
				break;
			}
		}
		if (ds0 == null) {
			throw new IllegalStateException("Default data source does not configure!");
		}
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource((DataSource)springContext.getBean(DEFAULT_DATASOURCE));
		sessionFactoryBean.setJtaTransactionManager(txManager());
		sessionFactoryBean.setPackagesToScan(ds0.getHibernate().getPackagesToScan());
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", ds0.getHibernate().getDialect());
		hibernateProperties.put("hibernate.show_sql", ds0.getHibernate().isShowsql());
		hibernateProperties.put("hibernate.format_sql", ds0.getHibernate().isFormatsql());
		hibernateProperties.put("hibernate.hbm2ddl.auto", ds0.getHibernate().getHbm2ddlauto());
		
		hibernateProperties.put("hibernate.connection.pool_size", ds0.getHibernate().getConnection_pool_size());
		//Spring + Bitronix manages the transaction by default!
		//sessionFactoryBean.setJtaTransactionManager(jtaTransactionManager);
		//org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory
		//org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory
		hibernateProperties.put("hibernate.jndi.class", "bitronix.tm.jndi.BitronixInitialContextFactory");
		hibernateProperties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory");
		hibernateProperties.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		hibernateProperties.put("hibernate.current_session_context_class", "jta"); //or thread binding.
		
		hibernateProperties.put("hibernate.cache.provider_class", ds0.getHibernate().getCache_provider_class());
		hibernateProperties.put("hibernate.enable_lazy_load_no_trans", ds0.getHibernate().isEnable_lazy_load_no_trans());
		hibernateProperties.put("hibernate.generate_statistics", ds0.getHibernate().isGenerate_statistics());
		hibernateProperties.put("hibernate.connection.autoReconnet", ds0.getHibernate().isAutoReconnet());
		hibernateProperties.put("hibernate.connection.autoReconnectForPools", ds0.getHibernate().isAutoReconnectForPools());
		hibernateProperties.put("hibernate.connection.is-connection-validation-required", ds0.getHibernate().isIsconnectionvalidationrequired());
		hibernateProperties.put("hibernate.connection.validationQuery", ds0.getHibernate().getValidationQuery());
		
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		if (ds0.getHbmRoot() != null && ds0.getHbmRoot().trim().length() > 0) {
			sessionFactoryBean.setMappingDirectoryLocations(new FileSystemResource(ds0.getHbmRoot()));
		}
		try {
			sessionFactoryBean.afterPropertiesSet();
		} catch (IOException e) {
			throw new IllegalStateException("Error to initialize session factory", e);
		}
		HibernateUtil.addSessionFactory(HibernateUtil.DEFAULT_SESSION, sessionFactoryBean.getObject());
		if (ds0.getHibernate().getPackagesToScan().indexOf(',') == -1) {
			HibernateUtil.addSessionFactory(ds0.getHibernate().getPackagesToScan(), sessionFactoryBean.getObject());
		} else {
			String[] packages = ds0.getHibernate().getPackagesToScan().split(",");
			for (String p: packages) {
				HibernateUtil.addSessionFactory(p, sessionFactoryBean.getObject());
			}
		}
		return sessionFactoryBean;
	}
	
	@Bean
	@Primary
	@ConditionalOnBean(name = "dataSource")
	public EntityManagerFactory entityManagerFactory() {
		DataSourceConfig ds0 = null;
		for (DataSourceConfig ds : this.getDataSources()) {
			if (ds.getName().equals(DEFAULT_DATASOURCE)) {
				ds0 = ds;
				break;
			}
		}
		if (ds0 == null) {
			throw new IllegalStateException("Default data source does not configure!");
		}
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(ds0.getHibernate().getPackagesToScan());
		factory.setDataSource((DataSource)springContext.getBean(DEFAULT_DATASOURCE));
//		factory.setPersistenceUnitManager(persistenceUnitManager);
//		factory.setMappingResources(mappingResources);
		if (ds0.getClassName().equals("org.apache.commons.dbcp.BasicDataSource")) {
			Properties hibernateProperties = new Properties();
			hibernateProperties.put("hibernate.dialect", ds0.getHibernate().getDialect());
			factory.setJpaProperties(hibernateProperties);
		}
		factory.afterPropertiesSet();

		return factory.getObject();
	}
	

	/**
	 * create Bitronix transaction manager.
	 * @param sessionFactory
	 * @return
	 */
	@Bean
	public JtaTransactionManager txManager() {
		if (txManager != null) {
			return txManager;
		}
		JtaTransactionManager txManager = new JtaTransactionManager();
		BitronixTransactionManager bitxManager = TransactionManagerServices.getTransactionManager();
		txManager.setTransactionManager(bitxManager);
		txManager.setUserTransaction(bitxManager);
		this.txManager = txManager;
		
		return txManager;
	}
	
}
