package com.sdss.servlet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.sdss.common.HttpCaller;
import com.sdss.common.SDSSConstants;
import com.sdss.common.Utils;
import com.sdss.exception.ActionException;

public class Method1
{

	// QUERY FOR OBJECTS IN IMAGE (sorted by decreasing brightness)
	public static String URL1 = "http://bluegrit.cs.umbc.edu:8080/solr/search?q=dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]&fq=fltMagnitude:[*%20TO%205]&sort=fltMagnitude%20asc&start=0&rows=100&wt=json&indent=true";

	public static void service(HttpServletRequest request, JSONObject temp) throws Exception
	{
		String dblRightAscensionMin, dblRightAscensionMax, dblDeclensionMin, dblDeclensionMax, fltMagnitudeMin, fltMagnitudeMax, start, rows;

		try
		{
			dblRightAscensionMin = request.getParameter(SDSSConstants.ASCENSION + "Min").trim();
			dblRightAscensionMax = request.getParameter(SDSSConstants.ASCENSION + "Max").trim();
			dblDeclensionMin = request.getParameter(SDSSConstants.DECLENSION + "Min").trim();
			dblDeclensionMax = request.getParameter(SDSSConstants.DECLENSION + "Max").trim();
			fltMagnitudeMin = request.getParameter(SDSSConstants.MAGNITUDE + "Min").trim();
			fltMagnitudeMax = request.getParameter(SDSSConstants.MAGNITUDE + "Max").trim();
			start = request.getParameter(SDSSConstants.START).trim();
			rows = request.getParameter(SDSSConstants.ROWS).trim();

			if (Utils.isEmpty(dblRightAscensionMin) || Utils.isEmpty(dblRightAscensionMax) || Utils.isEmpty(dblDeclensionMin) || Utils.isEmpty(dblDeclensionMax)
					|| Utils.isEmpty(fltMagnitudeMin) || Utils.isEmpty(fltMagnitudeMax) || Utils.isEmpty(start) || Utils.isEmpty(rows))
			{
				throw ActionException.PARAMETER_MISSING;
			}
		}
		catch (Exception e)
		{
			throw ActionException.PARAMETER_MISSING;
		}

		StringBuffer sb = new StringBuffer(SDSSConstants.BLUEGRIT_SOLR_URL);
		sb.append("&q=");
		sb.append("dblRightAscension:[").append(dblRightAscensionMin).append("+TO+").append(dblRightAscensionMax).append("]");
		sb.append("+AND+");
		sb.append("dblDeclension:[").append(dblDeclensionMin).append("+TO+").append(dblDeclensionMax).append("]");
		sb.append("&start=").append(start);
		sb.append("&rows=").append(rows);
		sb.append("&fq=fltMagnitude:[").append(fltMagnitudeMin).append("+TO+").append(fltMagnitudeMax).append("]");
		sb.append("&sort=fltMagnitude+asc");

		HttpCaller.callSolr(sb.toString(), temp);
	}
}
