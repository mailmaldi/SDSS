package com.sdss.auth;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sdss.common.DBBase;

public class AuthTableHandler extends DBBase
{
	private static Logger logger = Logger.getLogger(AuthTableHandler.class);

	private static final String table_name = "SDSSAuth";

	private static volatile AuthTableHandler instance = null;

	public static AuthTableHandler getInstance() throws Exception
	{
		if (null == instance)
		{
			synchronized (AuthTableHandler.class)
			{
				if (null == instance)
				{
					Properties prop = new Properties();
					try
					{
						InputStream is = AuthTableHandler.class.getClassLoader().getResourceAsStream("conf.properties");
						prop.load(is);
					}
					catch (Exception e)
					{
						logger.fatal("Couldnt load prop for AuthTH: ", e);
					}
					try
					{
						prop.put("dbuser", "test");
						prop.put("dbpass", "test");
						prop.put("dbpath", "//localhost:3306/test");
						instance = new AuthTableHandler(prop);
					}
					catch (Exception e)
					{
						logger.fatal("Couldnt initialize AuthTH: ", e);
					}
				}
			}
		}
		return instance;
	}

	private AuthTableHandler(Properties prop) throws Exception
	{
		super(prop);
	}

	public HashMap<String, String> getTupleByUserid(String userid)
	{
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String query = "SELECT * FROM " + table_name + " WHERE userid = ?";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			if (rs == null || !rs.next())
			{
				return null;
			}

			int orderID = rs.getInt("id");
			resultMap.put("id", orderID + "");
			resultMap.put("userid", userid);
			String password = rs.getString("password");
			resultMap.put("password", password);
			return resultMap;
		}
		catch (SQLException e)
		{
			logger.error("Failed to get call reports ", e);
		}
		finally
		{
			releaseConnection(conn);
			closePreparedStatement(stmt);
			closeResultSet(rs);
		}

		return null;
	}

	/**
	 * id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, emailid CHAR(200) UNIQUE, userid CHAR(20) UNIQUE DEFAULT NULL, password VARCHAR(200), salt VARCHAR(200), timestamp
	 * BIGINT, metadata VARCHAR(200) default "{}"
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> myMap = AuthTableHandler.getInstance().getTupleByUserid("milindp1");
		if (myMap == null)
			System.out.println("NULL response");
		else
			for (String string : myMap.keySet())
			{
				System.out.println("key = " + string + " , value = " + myMap.get(string));
			}

	}

}
