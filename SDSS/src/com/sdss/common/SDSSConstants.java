package com.sdss.common;

public class SDSSConstants
{
	public static final String SKIP_AUTH = "skip_auth";

	public static final String USERID = "userid";

	public static final String PASSWORD = "password";

	public static final String METHOD = "method";

	public static final String FORMAT = "format";

	public static final String ASCENSION = "dblRightAscension";

	public static final String DECLENSION = "dblDeclension";

	public static final String MAGNITUDE = "fltMagnitude";

	public static final String START = "start";

	public static final String ROWS = "rows";

	public static final String IMAGENAME = "imageName";

	public static final String BLUEGRIT_SOLR_URL = "http://bluegrit.cs.umbc.edu:8080/solr/search?wt=json&";

	public static final String FRAHA_URL = "http://bluegrit.cs.umbc.edu/~fraha1/share";

	public static final String EXTENSION = ".mask.png";

	public static enum METHOD_TYPES
	{
		METHOD1, METHOD2, METHOD3, METHOD4, METHOD5
	};

	public static String getFrahaURL(String imagePath)
	{
		return FRAHA_URL + imagePath + EXTENSION;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String givenPath = "/sdss/data/000745/40/1/fpC-000745-g1-0011";
		System.out.println(getFrahaURL(givenPath));
	}
}
