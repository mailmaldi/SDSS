package com.sdss.servlet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.sdss.common.HttpCaller;

public class Method2
{

	// QUERY FOR OBJECTS NOT IN IMAGE (sorted by decreasing brightness)
	public static String URL2 = "http://bluegrit.cs.umbc.edu:8080/solr/search?q=dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]&fq=fltMagnitude:[5%20TO%20*]&sort=fltMagnitude%20asc&start=0&rows=100&wt=json&indent=true";

	public static void service(HttpServletRequest request, JSONObject temp) throws Exception
	{
		HttpCaller.callSolr(URL2, temp);

	}

}
