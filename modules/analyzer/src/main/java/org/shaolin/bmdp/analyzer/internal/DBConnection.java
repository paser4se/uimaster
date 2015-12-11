package org.shaolin.bmdp.analyzer.internal;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import scala.runtime.AbstractFunction0;

public class DBConnection extends AbstractFunction0<Connection> implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private String driverClassName;
	private String connectionUrl;
	private String userName;
	private String password;

	public DBConnection(String driverClassName, String connectionUrl,
			String userName, String password) {
		this.driverClassName = driverClassName;
		this.connectionUrl = connectionUrl;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Connection apply() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			LoggerFactory.getLogger(DBConnection.class).error("Failed to load driver class", e);
		}

		Properties properties = new Properties();
		properties.setProperty("user", userName);
		properties.setProperty("password", password);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionUrl, properties);
		} catch (SQLException e) {
			LoggerFactory.getLogger(DBConnection.class).error("Connection failed", e);
		}

		return connection;
	}
}
