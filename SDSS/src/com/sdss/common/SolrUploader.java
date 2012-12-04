package com.sdss.common;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

public class SolrUploader
{
	public static void solrUploader(String filePath)
	{
		try
		{
			System.out.println("Test");
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpPost httpPost = new HttpPost(
					"http://127.0.0.1:8081/solr/update?fieldnames=longGscID,dblRightAscension,dblDeclension,fltPosError,fltMagnitude,fltMagnitudeError,intBand,intClass,strPlateNum,intMultipleObjects&commit=true");
			File file = new File(filePath);
			
			ContentBody cbFile = new FileBody(file, "text/csv","utf-8");

			MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			mpEntity.addPart("file", cbFile);
			mpEntity.addPart("commit", new StringBody("true"));
			mpEntity.addPart("fieldnames", new StringBody("longGscID,dblRightAscension,dblDeclension,fltPosError,fltMagnitude,fltMagnitudeError,intBand,intClass,strPlateNum,intMultipleObjects"));
			httpPost.setEntity(mpEntity);
			System.out.println("End");
			HttpResponse response = httpclient.execute(httpPost);
			System.out.println("RESPONSE:"+response.getStatusLine() );
			System.out.println("RESPONSE:"+response.toString() );
			System.out.println("RESPONSE:"+response.getAllHeaders() );
			System.out.println("RESPONSE:"+response.getParams());
			System.out.println("RESPONSE:"+response.getEntity().toString() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		//solrUploader("c:\\VirtualBox\\value_listing_out.txt");
		solrUploader("c:\\VirtualBox\\test.csv");

	}

}
