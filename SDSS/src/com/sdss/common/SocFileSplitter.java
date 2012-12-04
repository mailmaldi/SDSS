package com.sdss.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SocFileSplitter
{
	public static void main(String[] args) throws Exception
	{
		String PATH = "C:\\VirtualBox\\";
		BufferedReader br = new BufferedReader(new FileReader(PATH + "value_listing_out.txt"));
		String line = null;
		String header = ""; // Milind - Akshay do I add header line to every file?
		int count = 0;
		int filename = 0;
		BufferedWriter bw = null;
		int FILECOUNT = 100000;

		while ((line = br.readLine()) != null)
		{
			if (count == 0 || count >= FILECOUNT) // Count is 0 , start a new file, with name filename +1,if 100 then flush, close & same
			{
				if (count >= FILECOUNT)
				{
					bw.flush();
					bw.close();
					SolrUploader.solrUploader(PATH + filename);
					(new File(PATH + filename)).delete();
					count = 0;
				}
				bw = new BufferedWriter(new FileWriter(PATH + ++filename));
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
		SolrUploader.solrUploader(PATH + filename);
		(new File(PATH + filename)).delete();

	}
}
