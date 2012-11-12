<!--login.jsp-->

<%@page session="true"%>
<%
	System.out.println("session userid=" + session.getAttribute("userid"));
	System.out.println("session password=" + session.getAttribute("password"));
	response.sendRedirect("success.jsp");
%>

