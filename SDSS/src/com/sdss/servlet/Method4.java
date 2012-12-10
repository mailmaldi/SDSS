package com.sdss.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.sdss.auth.SDSSObjects;
import com.sdss.auth.SDSSObjectsTH;
import com.sdss.common.SDSSConstants;
import com.sdss.common.Utils;
import com.sdss.exception.ActionException;

public class Method4
{

	// rightAscension min,max & declension min,max

	public static void service(HttpServletRequest request, JSONObject temp) throws Exception
	{
		String dblRightAscensionMin, dblRightAscensionMax, dblDeclensionMin, dblDeclensionMax;
		double ascMin, ascMax, declMin, declMax;
		try
		{
			dblRightAscensionMin = request.getParameter(SDSSConstants.ASCENSION + "Min").trim();
			dblRightAscensionMax = request.getParameter(SDSSConstants.ASCENSION + "Max").trim();
			dblDeclensionMin = request.getParameter(SDSSConstants.DECLENSION + "Min").trim();
			dblDeclensionMax = request.getParameter(SDSSConstants.DECLENSION + "Max").trim();

			if (Utils.isEmpty(dblRightAscensionMin) || Utils.isEmpty(dblRightAscensionMax) || Utils.isEmpty(dblDeclensionMin) || Utils.isEmpty(dblDeclensionMax))
			{
				throw ActionException.PARAMETER_MISSING;
			}
			ascMin = Double.parseDouble(dblRightAscensionMin);
			ascMax = Double.parseDouble(dblRightAscensionMax);
			declMin = Double.parseDouble(dblDeclensionMin);
			declMax = Double.parseDouble(dblDeclensionMax);

		}
		catch (Exception e)
		{
			throw ActionException.PARAMETER_MISSING;
		}

		List<SDSSObjects> resultList = SDSSObjectsTH.getInstance().getUniqueImages(ascMin, ascMax, declMin, declMax);
		System.out.println("ResultSet size = " + resultList.size());

		JSONObject myLocal = new JSONObject();
		myLocal.put("size", resultList.size());
		int i = 0;
		for (SDSSObjects sdssObjects : resultList)
		{
			JSONObject here = sdssObjects.toJSONObjectImageParams();
			here.put("imageURL", SDSSConstants.getFrahaURL(here.getString("imageName")));
			myLocal.put("value" + i++, here);
		}

		temp.put("body", myLocal);

	}
}
