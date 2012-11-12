package com.sdss.common;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class DBBase
{

	public static final String DB_CONFIG_FILE = "properties/conf.properties";

	public static final String DB_USER_NAME = "dbuser";

	public static final String DB_PATH = "dbpath";

	public static final String DB_PASSWORD = "dbpass";

	private static Logger LOG = Logger.getLogger(DBBase.class);

	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

	private static final String PROTOCOL_MYSQL = "jdbc:mysql:";

	String dbUser;

	String dbPass;

	String driver = DRIVER_MYSQL;

	String protocol = PROTOCOL_MYSQL;

	String dbName;

	String url;

	String params;

	DataSource dataSource = null;

	public DBBase(Properties prop) throws Exception
	{
		init(prop);
		initJDBCDriver();
		initDbConnection();
	}

	private void init(Properties prop) throws FileNotFoundException, IOException
	{
		if (prop == null)
		{
			prop = new Properties();
			prop.load(new FileInputStream(DB_CONFIG_FILE));
		}
		this.dbName = prop.getProperty(DB_PATH);
		this.dbUser = prop.getProperty(DB_USER_NAME);
		this.dbPass = prop.getProperty(DB_PASSWORD);
		params = "?cachePrepStmts=true&prepStmtCacheSize=4096&prepStmtCacheSqlLimit=4096&autoReconnect=true";
		url = protocol + dbName + params;
	}

	private void initJDBCDriver() throws Exception
	{
		try
		{
			Class.forName(driver).newInstance();
			LOG.info("Loaded the appropriate driver");
		}
		catch (ClassNotFoundException cnfe)
		{
			LOG.fatal("Unable to load the JDBC driver. Please check your CLASSPATH. ", cnfe);
			throw cnfe;
		}
		catch (InstantiationException ie)
		{
			LOG.fatal("Unable to instantiate the JDBC driver ", ie);
			throw ie;
		}
		catch (IllegalAccessException iae)
		{
			LOG.fatal("Not allowed to access the JDBC driver ", iae);
			throw iae;
		}
	}

	private void initDbConnection() throws SQLException, PropertyVetoException
	{
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass(DRIVER_MYSQL);
		cpds.setJdbcUrl(url);
		cpds.setUser(dbUser);
		cpds.setPassword(dbPass);

		// Keep the connections alive
		cpds.setIdleConnectionTestPeriod(30);
		cpds.setTestConnectionOnCheckin(true);
		cpds.setPreferredTestQuery("SELECT 1");
		dataSource = cpds;
		LOG.info("Connected to database " + url);
	}

	protected Connection getConnection() throws SQLException
	{
		Connection conn = dataSource.getConnection();
		return conn;
	}

	protected void releaseConnection(Connection conn)
	{
		try
		{
			conn.setAutoCommit(true);
			conn.close();
		}
		catch (SQLException e)
		{
			LOG.error("Failed to release connection ", e);
		}
		return;
	}

	protected void rollback(Connection conn)
	{
		try
		{
			conn.rollback();
		}
		catch (SQLException e)
		{
			printSQLException(e);
		}
		LOG.warn("WARNING: Transaction rolled back.");
	}

	protected void printSQLException(SQLException e)
	{
		// Unwraps the entire exception chain to unveil the real cause of the Exception.
		while (e != null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("\n----- SQLException -----");
			sb.append("  SQL State:  " + e.getSQLState());
			sb.append("  Error Code: " + e.getErrorCode());
			sb.append("  Message:    " + e.getMessage());
			LOG.error(sb.toString(), e);
			e = e.getNextException();
		}
	}

	protected void closePreparedStatement(PreparedStatement ps)
	{
		if (ps != null)
		{
			List<PreparedStatement> psList = new LinkedList<PreparedStatement>();
			psList.add(ps);
			closePreparedStatements(psList);
		}
	}

	protected void closePreparedStatements(List<PreparedStatement> psList)
	{
		for (PreparedStatement ps : psList)
		{
			try
			{
				ps.close();
			}
			catch (SQLException e)
			{
				/** Ignored */
			}
		}
	}

	protected void closeResultSet(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				/** Ignored */
			}
		}
	}

	protected void closeStatement(Statement stmt)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (SQLException e)
			{
				/** ignored */
			}
		}
	}

}
