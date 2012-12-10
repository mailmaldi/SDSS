package com.sdss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;

import com.sdss.common.Authenticate;
import com.sdss.common.SDSSConstants;
import com.sdss.common.SDSSConstants.METHOD_TYPES;
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
		JSONObject returnObj = ActionException.UNKNOWN_EXCEPTION.toJSONObject();

		try
		{
			Authenticate.authenticate(request); // will throw an ActionException if not authenticated

			String method = request.getParameter(SDSSConstants.METHOD);
			// TODO method names need to be put in a enum or something
			if (Utils.isEmpty(method))
			{
				throw ActionException.METHOD_FAIL;
			}

			METHOD_TYPES methodType = null;
			try
			{
				methodType = SDSSConstants.METHOD_TYPES.valueOf(method);

			}
			catch (Exception e)
			{
				throw ActionException.METHOD_FAIL;
			}

			returnObj = ActionException.SUCCESS.toJSONObject();

			JSONObject temp = new JSONObject();
			switch (methodType)
			{
			case METHOD1:
				Method1.service(request, temp);
				returnObj.put("body", temp.get("body"));
				break;

			case METHOD2:
				Method2.service(request, temp);
				returnObj.put("body", temp.get("body"));
				break;

			case METHOD3:
				Method3.service(request, temp);
				returnObj.put("body", temp.get("body"));
				break;

			case METHOD4:
				Method4.service(request, temp);
				returnObj.put("body", temp.get("body"));
				break;

			case METHOD5:
				Method5.service(request, temp);
				returnObj.put("body", temp.get("body"));
				break;
			}

		}
		catch (ActionException ae)
		{
			returnObj = ae.toJSONObject();
			System.out.println("Caught ae in Rest:" + ae.toJSONObject().toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			returnObj = ActionException.UNKNOWN_EXCEPTION.toJSONObject();
		}
		finally
		{
			String responseString = returnObj.toString();
			String format = request.getParameter(SDSSConstants.FORMAT);
			if (!Utils.isEmpty(format) && format.trim().equalsIgnoreCase("xml"))
			{
				try
				{
					responseString = "<myResponse>" + XML.toString(returnObj) + "</myResponse>";
				}
				catch (Exception e2)
				{
				}
			}

			response.getWriter().write(responseString);
			// String xml = XML.toString(returnObj);
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
