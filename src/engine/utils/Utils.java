package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils
{
	public static String loadFileAsString(String path)
	{
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader(new File(path)));
			String line;
			while((line = br.readLine()) != null)
			{
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}


	public static int parseInt(String number)
	{
		try
		{
			return Integer.parseInt(number);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
