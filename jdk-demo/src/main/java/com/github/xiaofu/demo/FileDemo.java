/**
 * 
 */
package com.github.xiaofu.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author fulaihua
 * 
 */
public class FileDemo
{

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{

		FileReader reader = new FileReader("tt.txt");
		BufferedReader buffer = new BufferedReader(reader);
		String id = buffer.readLine();
		StringBuilder builder = new StringBuilder(100);
		while (id != null)
		{
			if (builder.length() == 0)
				builder.append("'"+id+"'");
			else
			{
				builder.append(",");
				builder.append("'"+id+"'");
			}
			id = buffer.readLine();

		}
		FileWriter writer=new FileWriter("ids.txt");
		writer.write(builder.toString());
		writer.close();buffer.close();
		System.out.println(builder.toString());
	}

}
