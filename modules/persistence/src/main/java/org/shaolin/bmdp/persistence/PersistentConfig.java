package org.shaolin.bmdp.persistence;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConfigurationProperties("persistentConstant")
@EnableConfigurationProperties
@EnableTransactionManagement
public class PersistentConfig {

	private String hbmRoot;

	private DataSource dataSource = new DataSource();
	
	private HibernateProperties hibernate = new HibernateProperties();
	
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
		DriverManagerDataSource dataSource0 = new DriverManagerDataSource();
		dataSource0.setDriverClassName(this.getDataSource().getDriver());
		dataSource0.setUrl(this.getDataSource().getUrl());
		dataSource0.setUsername(this.getDataSource().getUsername());
		dataSource0.setPassword(this.getDataSource().getPassword());
		return dataSource0;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(this.dataSource());
		sessionFactoryBean.setPackagesToScan(this.getHibernate().getPackagesToScan());
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", this.getHibernate().getDialect());
		hibernateProperties.put("hibernate.show_sql", this.getHibernate().isShowsql());
		hibernateProperties.put("hibernate.format_sql", this.getHibernate().isFormatsql());
		hibernateProperties.put("hibernate.hbm2ddl.auto", this.getHibernate().getHbm2ddlauto());
		
		hibernateProperties.put("hibernate.connection.pool_size", this.getHibernate().getConnection_pool_size());
		hibernateProperties.put("hibernate.current_session_context_class", this.getHibernate().getCurrent_session_context_class());
		hibernateProperties.put("hibernate.cache.provider_class", this.getHibernate().getCache_provider_class());
		hibernateProperties.put("hibernate.enable_lazy_load_no_trans", this.getHibernate().isEnable_lazy_load_no_trans());
		hibernateProperties.put("hibernate.generate_statistics", this.getHibernate().isGenerate_statistics());
		hibernateProperties.put("hibernate.connection.autoReconnet", this.getHibernate().isAutoReconnet());
		hibernateProperties.put("hibernate.connection.autoReconnectForPools", this.getHibernate().isAutoReconnectForPools());
		hibernateProperties.put("hibernate.connection.is-connection-validation-required", this.getHibernate().isIsconnectionvalidationrequired());
		hibernateProperties.put("hibernate.connection.validationQuery", this.getHibernate().getValidationQuery());
		
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		sessionFactoryBean.setMappingDirectoryLocations(new FileSystemResource(this.getHbmRoot()));
		return sessionFactoryBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	public static class DataSource {
		private String driver;

		private String password;

		private String url;

		private String username;

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

	}
	
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
