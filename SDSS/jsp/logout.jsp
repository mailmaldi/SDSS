<!--logout.jsp-->

<%@page import="java.util.Date" session="true"%>
<%
	session.setAttribute("userid", null);
	session.setAttribute("password", null);
	session.invalidate();
	response.sendRedirect("index.jsp");
%>

