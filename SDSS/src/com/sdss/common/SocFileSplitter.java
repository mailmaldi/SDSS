package com.sdss.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class SocFileSplitter
{
	public static void main(String[] args) throws Exception
	{
		String PATH = "C:\\Users\\Milind\\Desktop\\value_listing_out\\";
		BufferedReader br = new BufferedReader(new FileReader(PATH + "value_listing_out.txt"));
		String line = null;
		String header = ""; // Milind - Akshay do I add header line to every file?
		int count = 0;
		int filename = 1;
		BufferedWriter bw = null;

		while ((line = br.readLine()) != null)
		{
			if (count == 0 || count >= 100) // Count is 0 , start a new file, with name filename +1,if 100 then flush, close & same
			{
				if (count >= 100)
				{
					bw.flush();
					bw.close();
					count = 0;
				}
				bw = new BufferedWriter(new FileWriter(PATH + filename++));
				if (header.length() > 0)
				{
					bw.write(header);
					bw.newLine();
				}
			}
			bw.write(line);
			bw.newLine();
			count++;
		}
		if (bw != null)
		{
			bw.flush();
			bw.close();
		}
		br.close();

	}
}
