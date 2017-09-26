package org.shaolin.bmdp.persistence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

@Configuration
@ConfigurationProperties("persistentConstant")
@EnableConfigurationProperties
@EnableTransactionManagement
public class PersistentConfig {

	private String hbmRoot;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private HibernateProperties hibernate;
	
	public String getHbmRoot() {
		return hbmRoot;
	}

	public void setHbmRoot(String hbmRoot) {
		this.hbmRoot = hbmRoot;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public HibernateProperties getHibernate() {
		return hibernate;
	}

	public void setHibernate(HibernateProperties hibernate) {
		this.hibernate = hibernate;
	}

	@Bean
	public javax.sql.DataSource dataSource() {
		PoolingDataSource dataSource0 = new PoolingDataSource();
		dataSource0.setClassName(this.getDataSource().getClassName());
		dataSource0.setAutomaticEnlistingEnabled(true);
		dataSource0.setAllowLocalTransactions(true);
		dataSource0.setShareTransactionConnections(true);
		dataSource0.setUseTmJoin(true);
		dataSource0.setMinPoolSize(1);
		dataSource0.setMaxPoolSize(100);
		try {
			dataSource0.setUniqueName("Unique:" + InetAddress.getLocalHost().getHostAddress() + ":" + (Math.random() * 10000));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		Properties driverProperties = new Properties();
		driverProperties.put("url", this.getDataSource().getUrl());
		driverProperties.put("user", this.getDataSource().getUsername());
		driverProperties.put("password", this.getDataSource().getPassword());
		driverProperties.put("password", this.getDataSource().getPassword());
//		driverProperties.put("journal", this.getDataSource().getPassword());
//		driverProperties.put("log-part1-filename", "btm1.tlog");
//		driverProperties.put("log-part2-filename", "btm2.tlog");
		
		dataSource0.setDriverProperties(driverProperties);
		dataSource0.init();
		return dataSource0;
	}

	@Bean
	@ConditionalOnBean(name = "dataSource")
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("org.shaolin");
		factory.setDataSource(dataSource());
//		factory.setPersistenceUnitManager(persistenceUnitManager);
//		factory.setMappingResources(mappingResources);
		factory.afterPropertiesSet();

		return factory.getObject();
	}
	
	@Bean
	@ConditionalOnBean(name = "dataSource")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(this.dataSource());
		sessionFactoryBean.setJtaTransactionManager(txManager());
		sessionFactoryBean.setPackagesToScan(this.getHibernate().getPackagesToScan());
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", this.getHibernate().getDialect());
		hibernateProperties.put("hibernate.show_sql", this.getHibernate().isShowsql());
		hibernateProperties.put("hibernate.format_sql", this.getHibernate().isFormatsql());
		hibernateProperties.put("hibernate.hbm2ddl.auto", this.getHibernate().getHbm2ddlauto());
		
		hibernateProperties.put("hibernate.connection.pool_size", this.getHibernate().getConnection_pool_size());
		//Spring + Bitronix manages the transaction by default!
		//sessionFactoryBean.setJtaTransactionManager(jtaTransactionManager);
		//org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory
		//org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory
		hibernateProperties.put("hibernate.jndi.class", "bitronix.tm.jndi.BitronixInitialContextFactory");
		hibernateProperties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory");
		hibernateProperties.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		hibernateProperties.put("hibernate.current_session_context_class", "jta"); //or thread binding.
		
		hibernateProperties.put("hibernate.cache.provider_class", this.getHibernate().getCache_provider_class());
		hibernateProperties.put("hibernate.enable_lazy_load_no_trans", this.getHibernate().isEnable_lazy_load_no_trans());
		hibernateProperties.put("hibernate.generate_statistics", this.getHibernate().isGenerate_statistics());
		hibernateProperties.put("hibernate.connection.autoReconnet", this.getHibernate().isAutoReconnet());
		hibernateProperties.put("hibernate.connection.autoReconnectForPools", this.getHibernate().isAutoReconnectForPools());
		hibernateProperties.put("hibernate.connection.is-connection-validation-required", this.getHibernate().isIsconnectionvalidationrequired());
		hibernateProperties.put("hibernate.connection.validationQuery", this.getHibernate().getValidationQuery());
		
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		if (this.getHbmRoot() != null && this.getHbmRoot().trim().length() > 0) {
			sessionFactoryBean.setMappingDirectoryLocations(new FileSystemResource(this.getHbmRoot()));
		}
		return sessionFactoryBean;
	}

	/**
	 * Hibernate transaction manager.
	 */
//	@Bean
//	@ConditionalOnBean(name = "sessionFactory")
//	@Autowired
//	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
//		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//		transactionManager.setSessionFactory(sessionFactory);
//		transactionManager.setDataSource(this.dataSource());
//		transactionManager.setNestedTransactionAllowed(false);
//		transactionManager.afterPropertiesSet();
//		return transactionManager;
//	}
	
	/**
	 * create Bitronix transaction manager.
	 * @param sessionFactory
	 * @return
	 */
	@Bean
	public JtaTransactionManager txManager() {
		JtaTransactionManager txManager = new JtaTransactionManager();
		BitronixTransactionManager bitxManager = TransactionManagerServices.getTransactionManager();
		txManager.setTransactionManager(bitxManager);
		txManager.setUserTransaction(bitxManager);
		return txManager;
	}
	
	@Configuration
	@ConfigurationProperties("persistentConstant.datasource")
	public static class DataSource {
		private String className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
		
		private String driver = "com.mysql.jdbc.Driver";

		private String password = "uimaster";

		private String url;

		private String username = "uimaster";

		public String getDriver() {
			return driver;
		}

		public void setDriver(String driver) {
			this.driver = driver;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
		
	}
	
	@Configuration
	@ConfigurationProperties("persistentConstant.hibernate")
	public static class HibernateProperties {

		private String dialect = "org.hibernate.dialect.MySQLInnoDBDialect";

		private String packagesToScan = "org.shaolin";
		
		private String hbm2ddlauto = "update";
		
		private String current_session_context_class = "thread";
		
		private String cache_provider_class = "org.hibernate.cache.internal.NoCacheProvider";
		
		private String validationQuery = "SELECT 1";
		
		private int connection_pool_size = 10;
		
		private boolean showsql = true;
		
		private boolean formatsql = true;

		private boolean generate_statistics = false;
		
		private boolean autoReconnet = true;

		private boolean autoReconnectForPools = true;
		
		private boolean enable_lazy_load_no_trans = true;
		
		private boolean isconnectionvalidationrequired = true;
		
		public String getDialect() {
			return dialect;
		}

		public void setDialect(String dialect) {
			this.dialect = dialect;
		}

		public boolean isShowsql() {
			return showsql;
		}

		public void setShowsql(boolean showsql) {
			this.showsql = showsql;
		}

		public boolean isAutoReconnet() {
			return autoReconnet;
		}

		public void setAutoReconnet(boolean autoReconnet) {
			this.autoReconnet = autoReconnet;
		}

		public String getPackagesToScan() {
			return packagesToScan;
		}

		public void setPackagesToScan(String packagesToScan) {
			this.packagesToScan = packagesToScan;
		}

		public String getHbm2ddlauto() {
			return hbm2ddlauto;
		}

		public void setHbm2ddlauto(String hbm2ddlauto) {
			this.hbm2ddlauto = hbm2ddlauto;
		}

		public String getCurrent_session_context_class() {
			return current_session_context_class;
		}

		public void setCurrent_session_context_class(String current_session_context_class) {
			this.current_session_context_class = current_session_context_class;
		}

		public String getCache_provider_class() {
			return cache_provider_class;
		}

		public void setCache_provider_class(String cache_provider_class) {
			this.cache_provider_class = cache_provider_class;
		}

		public String getValidationQuery() {
			return validationQuery;
		}

		public void setValidationQuery(String validationQuery) {
			this.validationQuery = validationQuery;
		}

		public int getConnection_pool_size() {
			return connection_pool_size;
		}

		public void setConnection_pool_size(int connection_pool_size) {
			this.connection_pool_size = connection_pool_size;
		}

		public boolean isFormatsql() {
			return formatsql;
		}

		public void setFormatsql(boolean formatsql) {
			this.formatsql = formatsql;
		}

		public boolean isAutoReconnectForPools() {
			return autoReconnectForPools;
		}

		public void setAutoReconnectForPools(boolean autoReconnectForPools) {
			this.autoReconnectForPools = autoReconnectForPools;
		}

		public boolean isEnable_lazy_load_no_trans() {
			return enable_lazy_load_no_trans;
		}

		public void setEnable_lazy_load_no_trans(boolean enable_lazy_load_no_trans) {
			this.enable_lazy_load_no_trans = enable_lazy_load_no_trans;
		}

		public boolean isGenerate_statistics() {
			return generate_statistics;
		}

		public void setGenerate_statistics(boolean generate_statistics) {
			this.generate_statistics = generate_statistics;
		}

		public boolean isIsconnectionvalidationrequired() {
			return isconnectionvalidationrequired;
		}

		public void setIsconnectionvalidationrequired(boolean isconnectionvalidationrequired) {
			this.isconnectionvalidationrequired = isconnectionvalidationrequired;
		}
		
	}
	
}
