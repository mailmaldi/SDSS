package com.sdss.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sdss.auth.AuthTableHandler;
import com.sdss.common.SDSSConstants;
import com.sdss.common.Utils;
import com.sdss.exception.ActionException;

public class RestServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(RestServlet.class);

	private static final long serialVersionUID = 1997662500046608293L;

	public static String servletContextPath; // This is set in the Constructor.

	@Override
	public void init() throws ServletException
	{
		servletContextPath = getServletConfig().getServletContext().getRealPath("");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject returnObj = new JSONObject();
		returnObj.optString("status", "error");
		returnObj.optString("code", "100");
		returnObj.optString("cause", "Unknown Error has occured");

		try
		{
			String userid = request.getParameter(SDSSConstants.USERID);
			String password = request.getParameter(SDSSConstants.PASSWORD);
			if (Utils.isEmpty(userid) || Utils.isEmpty(password))
			{
				returnObj.put("code", "101");
				returnObj.put("cause", "userid or password missing");
				throw new ActionException();
			}

			HashMap<String, String> authMapParam = AuthTableHandler.getInstance().getTupleByUserid(userid.trim());
			if (Utils.isEmpty(authMapParam))
			{
				returnObj.put("code", "102");
				returnObj.put("cause", "userid not found in DB");
				throw new ActionException();
			}

			String generatedPasswordHash = Utils.generateDBPassword(password.trim());
			if (!generatedPasswordHash.equals(authMapParam.get("password")))
			{
				returnObj.put("code", "103");
				returnObj.put("cause", "password doesnt match");
				throw new ActionException();
			}

			logger.info("userid:" + userid + " Authenticated");

			returnObj.put("status", "success");
			returnObj.put("code", "0");
			returnObj.put("cause", "successfully authenticated");
			returnObj.put("body", "");

		}
		catch (ActionException ae)
		{

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			response.getWriter().write(returnObj.toString());
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
