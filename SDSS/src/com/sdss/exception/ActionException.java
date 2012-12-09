package com.sdss.exception;

import org.json.JSONException;
import org.json.JSONObject;

import com.sdss.common.Utils;

public class ActionException extends Exception
{

	private static final long serialVersionUID = -8593776683715704058L;

	private int id;

	private String status;

	private String text;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	private String body;

	public ActionException()
	{
		this.id = 100;
		this.status = Utils.STATUS.ERROR.toString();
		this.text = "An Unknown Exception has occured";
		this.body = "";
	}

	public ActionException(int id, String status, String text)
	{
		this.id = id;
		this.status = status;
		this.text = text;
		this.body = "";
	}

	public ActionException(int id, String status, String text, String body)
	{
		this.id = id;
		this.status = status;
		this.text = text;
		this.body = body;
	}

	public JSONObject toJSONObject()
	{
		JSONObject returnObj = new JSONObject();
		try
		{
			returnObj.put("status", this.status);
			returnObj.put("code", this.id + "");
			returnObj.put("cause", this.text);
			returnObj.put("body", this.body);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return returnObj;
	}

	public static ActionException SUCCESS = new ActionException(0, Utils.STATUS.SUCCESS.toString(), "success", new JSONObject().toString());

	public static ActionException UNKNOWN_EXCEPTION = new ActionException(100, Utils.STATUS.ERROR.toString(), "An Unknown Error has Occured");

	public static ActionException AUTH_FAIL = new ActionException(101, Utils.STATUS.ERROR.toString(), "userid or password missing or mismatch");

	public static ActionException METHOD_FAIL = new ActionException(102, Utils.STATUS.ERROR.toString(), "method missing or wrong value");

}
