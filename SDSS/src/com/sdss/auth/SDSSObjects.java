package com.sdss.auth;

import org.json.JSONException;
import org.json.JSONObject;

public class SDSSObjects
{

	private String basePath;

	private String imageName;

	private int ojbId;

	private double rightAscension;

	private double declension;

	private double majorAxis;

	private double minorAxis;

	private double eccentricity;

	private double theta;

	private int solidAngle;

	private int count;

	private int numPixels;

	private double magnitude;

	private String constellation;

	public SDSSObjects()
	{

	}

	public SDSSObjects(String basePath, String imageName, int ojbId, double rightAscension, double declension, double majorAxis, double minorAxis, double eccentricity,
			double theta, int solidAngle, int count, int numPixels, double magnitude, String constellation)
	{
		this.basePath = basePath;
		this.imageName = imageName;
		this.ojbId = ojbId;
		this.rightAscension = rightAscension;
		this.declension = declension;
		this.majorAxis = majorAxis;
		this.minorAxis = minorAxis;
		this.eccentricity = eccentricity;
		this.theta = theta;
		this.solidAngle = solidAngle;
		this.count = count;
		this.numPixels = numPixels;
		this.magnitude = magnitude;
		this.constellation = constellation;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(imageName).append(",");
		sb.append(ojbId).append(",");
		sb.append(rightAscension).append(",");
		sb.append(declension).append(",");
		sb.append(majorAxis).append(",");
		sb.append(minorAxis).append(",");
		sb.append(eccentricity).append(",");
		sb.append(theta).append(",");
		sb.append(solidAngle).append(",");
		sb.append(count).append(",");
		sb.append(numPixels).append(",");
		sb.append(magnitude).append(",");
		sb.append(constellation);
		return sb.toString();
	}

	public JSONObject toJSONObject() throws JSONException
	{
		JSONObject jObj = new JSONObject();
		jObj.put("basePath", basePath);
		jObj.put("imageName", imageName);
		jObj.put("ojbId", ojbId);
		jObj.put("rightAscension", rightAscension);
		jObj.put("declension", declension);
		jObj.put("majorAxis", majorAxis);
		jObj.put("minorAxis", minorAxis);
		jObj.put("eccentricity", eccentricity);
		jObj.put("theta", theta);
		jObj.put("solidAngle", solidAngle);
		jObj.put("count", count);
		jObj.put("numPixels", numPixels);
		jObj.put("magnitude", magnitude);
		jObj.put("constellation", constellation);
		return jObj;
	}

	public JSONObject toJSONObjectObjectParams() throws JSONException
	{
		JSONObject jObj = new JSONObject();
		jObj.put("ojbId", ojbId);
		jObj.put("rightAscension", rightAscension);
		jObj.put("declension", declension);
		jObj.put("majorAxis", majorAxis);
		jObj.put("minorAxis", minorAxis);
		jObj.put("eccentricity", eccentricity);
		jObj.put("theta", theta);
		jObj.put("solidAngle", solidAngle);
		jObj.put("count", count);
		jObj.put("numPixels", numPixels);
		jObj.put("magnitude", magnitude);
		jObj.put("constellation", constellation);
		return jObj;
	}

	public JSONObject toJSONObjectImageParams() throws JSONException
	{
		JSONObject jObj = new JSONObject();
		jObj.put("imageName", imageName);
		jObj.put("constellation", constellation);
		return jObj;
	}

	/**
	 * 
	 * getters & setters
	 */

	public String getBasePath()
	{
		return basePath;
	}

	public void setBasePath(String basePath)
	{
		this.basePath = basePath;
	}

	public String getImageName()
	{
		return imageName;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	public int getOjbId()
	{
		return ojbId;
	}

	public void setOjbId(int ojbId)
	{
		this.ojbId = ojbId;
	}

	public double getRightAscension()
	{
		return rightAscension;
	}

	public void setRightAscension(double rightAscension)
	{
		this.rightAscension = rightAscension;
	}

	public double getDeclension()
	{
		return declension;
	}

	public void setDeclension(double declension)
	{
		this.declension = declension;
	}

	public double getMajorAxis()
	{
		return majorAxis;
	}

	public void setMajorAxis(double majorAxis)
	{
		this.majorAxis = majorAxis;
	}

	public double getMinorAxis()
	{
		return minorAxis;
	}

	public void setMinorAxis(double minorAxis)
	{
		this.minorAxis = minorAxis;
	}

	public double getEccentricity()
	{
		return eccentricity;
	}

	public void setEccentricity(double eccentricity)
	{
		this.eccentricity = eccentricity;
	}

	public double getTheta()
	{
		return theta;
	}

	public void setTheta(double theta)
	{
		this.theta = theta;
	}

	public int getSolidAngle()
	{
		return solidAngle;
	}

	public void setSolidAngle(int solidAngle)
	{
		this.solidAngle = solidAngle;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getNumPixels()
	{
		return numPixels;
	}

	public void setNumPixels(int numPixels)
	{
		this.numPixels = numPixels;
	}

	public double getMagnitude()
	{
		return magnitude;
	}

	public void setMagnitude(double magnitude)
	{
		this.magnitude = magnitude;
	}

	public String getConstellation()
	{
		return constellation;
	}

	public void setConstellation(String constellation)
	{
		this.constellation = constellation;
	}

}
