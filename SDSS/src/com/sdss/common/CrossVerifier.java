package com.sdss.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;

import org.json.JSONObject;

public class CrossVerifier
{
	public static String SOLR_URL = "http://bluegrit.cs.umbc.edu:8080/solr/search?&wt=json&q=";

	// dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]

	public static void main(String[] args) throws Exception
	{
		File dir = new File("F:\\SDSS\\");
		File[] fList = dir.listFiles();
		BufferedReader br = null;
		String line = null;
		String[] splits = null;

		// /bluegrit/nfs1/fraha1/sdss/data/000125/40/1,/bluegrit/nfs1/fraha1/sdss/data/000125/40/1/fpC-000125-g1-0126,1,7.672488,-1.223441,0.000000,0.000906,0.004280,1.000000,0
		for (File file : fList)
		{
			br = new BufferedReader(new FileReader(file));
			System.out.println("FILE=" + file.getAbsolutePath());
			while ((line = br.readLine()) != null)
			{
				try
				{
					splits = line.split(",");
					String rightAsc = splits[3];
					String decl = splits[4];
					// dblRightAscension:[15%20TO%2030]%20AND%20dblDeclension:[20%20TO%20*]
					String queryEnd = "dblRightAscension:\"" + rightAsc + "\" AND " + "dblDeclension:\"" + decl + "\"";

					JSONObject jObj = new JSONObject();

					int status = HttpCaller.callSolr(SOLR_URL + URLEncoder.encode(queryEnd, "UTF-8"), jObj);
					// System.out.println("status=" + status + " response=" + jObj.toString());

					JSONObject body = (JSONObject) jObj.get("body");
					JSONObject response = new JSONObject(body.getString("response"));
					// System.out.println(body.toString());
					int numFound = response.getInt("numFound");
					if (numFound > 0)
					{
						System.out.println("FOUND:" + " Key=" + line);
					}
				}
				catch (Exception e)
				{
					System.out.println("Exception:" + e.getMessage());
				}

			}

		}
	}

	public static String testJsonStr = "\"{\"responseHeader\":{\"status\":0,\"QTime\":1,\"params\":{\"q\":\"dblRightAscension:\"350.471130\" AND dblDeclension:\"-1.196931\"\",\"wt\":\"json\"}},\"response\":{\"numFound\":0,\"start\":0,\"docs\":[]}}\"";

	// public static JSONObject testJson = new JSONObject(testJsonStr);
}
