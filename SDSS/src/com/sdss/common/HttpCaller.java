package com.sdss.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpCaller
{
	public static int callSolr(String URL, JSONObject jObj)
	{
		int statusCode = -1;
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpGet httpGet = new HttpGet(URL);

			HttpResponse response = httpclient.execute(httpGet);

			System.out.println("CALLING URL: " + URL);

			HttpEntity entity = response.getEntity();

			String responseBody = EntityUtils.toString(entity);

			JSONObject temp = new JSONObject(responseBody.trim());

			jObj.put("body", temp);
			statusCode = response.getStatusLine().getStatusCode();

			System.out.println("RESPONSE code:" + statusCode);
			System.out.println("RESPONSE body:" + responseBody);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return statusCode;
	}

	// QUERY FOR OBJECTS IN IMAGE (sorted by decreasing brightness)
	public static String URL1 = "http://bluegrit.cs.umbc.edu:8080/solr/search?q=dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]&fq=fltMagnitude:[*%20TO%205]&sort=fltMagnitude%20asc&start=0&rows=100&wt=json&indent=true";

	// QUERY FOR OBJECTS NOT IN IMAGE (sorted by decreasing brightness)
	public static String URL2 = "http://bluegrit.cs.umbc.edu:8080/solr/search?q=dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]&fq=fltMagnitude:[5%20TO%20*]&sort=fltMagnitude%20asc&start=0&rows=100&wt=json&indent=true";

	public static void main(String[] args)
	{

	}
}
