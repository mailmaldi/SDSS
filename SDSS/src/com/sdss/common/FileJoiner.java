package com.sdss.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileJoiner
{
	public static String DIRPATH = "D:\\sdss\\1\\";

	public static String INSERT_STMT = "INSERT INTO sdss_run125_objects VALUES ";

	public static void main(String[] args) throws Exception
	{
		File fDir = new File(DIRPATH);
		BufferedReader br = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\sdss\\output.csv"));
		File[] fileList = fDir.listFiles();
		String line = null;

		for (File file : fileList)
		{
			try
			{
				br = new BufferedReader(new FileReader(file));

				while ((line = br.readLine()) != null)
				{
					if (line.length() > 5)
					{
						String[] splits = line.split(",");
						if (splits.length == 14)
						{
							splits[0] = "\"" + splits[0] + "\"";
							splits[1] = "\"" + splits[1] + "\"";
							splits[13] = "\"" + splits[13] + "\"";
							line = createLine(splits);
							bw.write(INSERT_STMT + "(" + line + ");");
						}
						else if (splits.length == 13)
						{
							splits[0] = "\"" + splits[0] + "\"";
							splits[1] = "\"" + splits[1] + "\"";
							line = createLine(splits);
							bw.write(INSERT_STMT + "(" + line + ",\"\");");
						}
						else
							System.out.println("WTF: " + splits.length + " line:" + line);
						bw.newLine();
					}
				}
				// System.out.println("DONE:" + file.getName());
				br.close();
				br = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		bw.flush();
		bw.close();
	}

	public static String createLine(String[] splits)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < splits.length; i++)
		{
			sb.append(splits[i]);
			if (i < splits.length - 1)
				sb.append(",");
		}

		return sb.toString();
	}
}
