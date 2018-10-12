package org.shaolin.bmdp.persistence;

public class DataSourceConfig {
	private String className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";

	private String driver = "com.mysql.jdbc.Driver";

	private String password = "uimaster";

	private String url;

	private String username = "uimaster";

	private String name; // data source name.

	private String hbmRoot;

	private int maxPoolSize = 100;

	private int minPoolSize = 0;

	private HibernateProperties hibernate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public HibernateProperties getHibernate() {
		return hibernate;
	}

	public void setHibernate(HibernateProperties hibernate) {
		this.hibernate = hibernate;
	}

	public String getHbmRoot() {
		return hbmRoot;
	}

	public void setHbmRoot(String hbmRoot) {
		this.hbmRoot = hbmRoot;
	}
	
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
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
