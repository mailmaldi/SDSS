package com.sdss.common;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sdss.auth.AuthTableHandler;
import com.sdss.exception.ActionException;

public class Authenticate
{
	private static Logger logger = Logger.getLogger(Authenticate.class);

	public static void authenticate(HttpServletRequest request) throws Exception
	{
		String skip_auth = request.getParameter(SDSSConstants.SKIP_AUTH);
		String userid = request.getParameter(SDSSConstants.USERID);
		String password = request.getParameter(SDSSConstants.PASSWORD);
		if (Utils.isEmpty(skip_auth))
		{
			if (Utils.isEmpty(userid) || Utils.isEmpty(password))
			{
				throw ActionException.AUTH_FAIL;
			}

			HashMap<String, String> authMapParam = AuthTableHandler.getInstance().getTupleByUserid(userid.trim());
			if (Utils.isEmpty(authMapParam))
			{
				throw ActionException.AUTH_FAIL;
			}

			String generatedPasswordHash = Utils.generateDBPassword(password.trim());
			if (!generatedPasswordHash.equals(authMapParam.get("password")))
			{
				throw ActionException.AUTH_FAIL;
			}
		}

		logger.info("userid:" + userid + " Authenticated");

	}
}
