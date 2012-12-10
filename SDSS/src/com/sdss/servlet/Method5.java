package com.sdss.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.sdss.auth.SDSSObjects;
import com.sdss.auth.SDSSObjectsTH;
import com.sdss.common.SDSSConstants;
import com.sdss.common.Utils;
import com.sdss.exception.ActionException;

public class Method5
{

	public static void service(HttpServletRequest request, JSONObject temp) throws Exception
	{
		String imageName;
		try
		{
			imageName = request.getParameter(SDSSConstants.IMAGENAME).trim();

			if (Utils.isEmpty(imageName))
			{
				throw ActionException.PARAMETER_MISSING;
			}

		}
		catch (Exception e)
		{
			throw ActionException.PARAMETER_MISSING;
		}

		List<SDSSObjects> resultList = SDSSObjectsTH.getInstance().getObjectsForImage(imageName);
		System.out.println("ResultSet size = " + resultList.size());

		JSONObject myLocal = new JSONObject();
		myLocal.put("size", resultList.size());
		int i = 0;
		for (SDSSObjects sdssObjects : resultList)
		{
			JSONObject here = sdssObjects.toJSONObjectObjectParams();
			myLocal.put("value" + i++, here);
		}

		temp.put("body", myLocal);

	}
}
