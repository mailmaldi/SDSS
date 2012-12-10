package com.sdss.auth;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sdss.common.DBBase;

public class SDSSObjectsTH extends DBBase
{
	private static Logger logger = Logger.getLogger(SDSSObjectsTH.class);

	private static final String table_name = "sdss_run125_objects";

	private static volatile SDSSObjectsTH instance = null;

	public static SDSSObjectsTH getInstance() throws Exception
	{
		if (null == instance)
		{
			synchronized (SDSSObjectsTH.class)
			{
				if (null == instance)
				{
					Properties prop = new Properties();
					try
					{
						InputStream is = SDSSObjectsTH.class.getClassLoader().getResourceAsStream("conf.properties");
						prop.load(is);
					}
					catch (Exception e)
					{
						logger.fatal("Couldnt load prop for AuthTH: ", e);
						try
						{
							prop.put("dbuser", "test");
							prop.put("dbpass", "test");
							prop.put("dbpath", "//localhost:3306/test");
						}
						catch (Exception ne)
						{
							logger.fatal("Couldnt initialize AuthTH: ", ne);
						}
					}
					instance = new SDSSObjectsTH(prop);
				}
			}
		}
		return instance;
	}

	private SDSSObjectsTH(Properties prop) throws Exception
	{
		super(prop);
	}

	public List<SDSSObjects> getImages(double rightAscensionMin, double rightAscensionMax, double declensionMin, double declensionMax)
	{
		List<SDSSObjects> resultList = new ArrayList<SDSSObjects>();
		String query = "SELECT * FROM " + table_name + " WHERE rightAscension >= ? AND rightAscension <= ? AND declension >= ? AND declension <= ?";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, rightAscensionMin);
			stmt.setDouble(2, rightAscensionMax);
			stmt.setDouble(3, declensionMin);
			stmt.setDouble(4, declensionMax);
			rs = stmt.executeQuery();

			if (rs == null)
			{
				return resultList; // TODO comment this
			}

			while (rs.next())
			{

				SDSSObjects obj = new SDSSObjects();
				obj.setBasePath(rs.getString(1));
				obj.setImageName(rs.getString(2));
				obj.setOjbId(rs.getInt(3));
				obj.setRightAscension(rs.getDouble(4));
				obj.setDeclension(rs.getDouble(5));
				obj.setMajorAxis(rs.getDouble(6));
				obj.setMinorAxis(rs.getDouble(7));
				obj.setEccentricity(rs.getDouble(8));
				obj.setTheta(rs.getDouble(9));
				obj.setSolidAngle(rs.getInt(10));
				obj.setCount(rs.getInt(11));
				obj.setNumPixels(rs.getInt(12));
				obj.setMagnitude(rs.getDouble(13));
				obj.setConstellation(rs.getString(14));
				resultList.add(obj);
			}
			return resultList;
		}
		catch (SQLException e)
		{
			logger.error("Failed to get SDSSObjects ", e);
			e.printStackTrace();
		}
		finally
		{
			releaseConnection(conn);
			closePreparedStatement(stmt);
			closeResultSet(rs);
		}

		return resultList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		List<SDSSObjects> resultList = SDSSObjectsTH.getInstance().getImages(160.47, 160.49, -1.25203455719994, -0.00411655398905);
		System.out.println("ResultSet size = " + resultList.size());
		for (SDSSObjects sdssObjects : resultList)
		{
			System.out.println("MALDI:" + sdssObjects.toString());
		}
	}

}
