<%@page import="java.util.HashMap"%>
<%@page import="com.sdss.auth.AuthTableHandler"%>
<%@page import="com.sdss.common.Utils"%>
<%@page import="com.sdss.exception.ActionException"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.util.HashMap"%>

<%
	JSONObject returnObj = new JSONObject();
	returnObj.put("status", "error");
	returnObj.put("code", "100");
	returnObj.put("cause", "Unknown Error has occured");

	String userid = request.getParameter("un");
	String password = request.getParameter("pw");
	System.out.println("check.jsp userid=" + userid);
	System.out.println("check.jsp password=" + password);

	try
	{

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

		System.out.println("userid:" + userid + " Authenticated");

		returnObj.put("status", "success");
		returnObj.put("code", "0");
		returnObj.put("cause", "successfully authenticated");
		returnObj.put("body", "");

		System.out.println("check.jsp setting session attributes userid,password" + userid + password);

		session.setAttribute("userid", userid.trim());
		session.setAttribute("password", password.trim());

	}
	catch (ActionException ae)
	{

	}
	finally
	{
		response.getWriter().write(returnObj.toString());
		response.flushBuffer();
	}
%>

