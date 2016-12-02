package com.oranda.util;

import java.io.*;

public class FileUtils 
{
    public static void copy(String path1, String path2) throws IOException 
    {
        FileReader in = new FileReader(path1);
        FileWriter out = new FileWriter(path2);
        int c;

        while ((c = in.read()) != -1)
           out.write(c);

        in.close();
        out.close();
    }
	
	public static void writeToFile(String path, String str) throws IOException
	{
		FileWriter out = new FileWriter(path);
		out.write(str, 0, str.length());
		out.close();
	}
}
