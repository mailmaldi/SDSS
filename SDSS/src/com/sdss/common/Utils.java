package com.sdss.common;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

import com.sdss.exception.ActionException;

public class Utils
{
	private static final Logger logger = Logger.getLogger(Utils.class);

	public static enum STATUS
	{
		SUCCESS, ERROR
	};

	public static final String salt = "zP9UFJOklQLePOqf0lSh0NgdlXWAt8qhIq4adcP1opdkz8UwVz";

	public static boolean isEmpty(String value)
	{
		if (isEmpty((Object) value))
			return true;
		if (value.trim().length() == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object value)
	{
		if (value == null)
			return true;
		return false;
	}

	public static boolean isEmpty(Object[] values)
	{
		if (values == null)
			return true;
		for (Object value : values)
		{
			if (!isEmpty(value))
				return false;
		}
		return true;
	}

	public static <T> boolean isOneOf(T item, T... items)
	{
		if (items != null && item != null)
		{
			for (T itemToCheck : items)
			{
				if (item.equals(itemToCheck))
					return true;
			}
		}
		return false;
	}

	public static String generateDBPassword(String plainPassword)
	{
		String dbPassword = "";

		if ((plainPassword != null) && (plainPassword.trim().length() > 0))
		{
			try
			{
				MessageDigest md5 = MessageDigest.getInstance("SHA-1");
				md5.update((plainPassword.trim() + salt).getBytes());
				dbPassword = new sun.misc.BASE64Encoder().encode(md5.digest());
			}
			catch (Exception e)
			{
				logger.error("", e);
			}
		}

		return dbPassword;
	}

	public static void main(String[] args)
	{
		String password = "password";
		String dbpass = generateDBPassword(password);
		System.out.println(dbpass);
	}

	private String bytes2String(byte[] bytes)
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}
}
