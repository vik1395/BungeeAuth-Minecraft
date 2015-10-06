package me.vik1395.BungeeAuth.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import me.vik1395.BungeeAuth.Main;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Blacklist 
{
	String filename = "apiblacklist.txt";
	String fullpath = Main.plugin.getDataFolder() + System.getProperty("file.separator") + "apiblacklist.txt";
	
	public void create()
	{
		File file = new File(fullpath);
		
        if (!Main.plugin.getDataFolder().exists())
        {
			Main.plugin.getDataFolder().mkdir();
        }
        
		try
		{
	        if (!file.exists()) 
	        {
				Files.copy(Main.plugin.getResourceAsStream(filename), file.toPath());
	        }
		}
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void write(String ip, int count)
	{
		if(read(ip)!=-1)
		{
			delete(ip);
		}
		
		try
        {
            PrintWriter file1 = new PrintWriter(new FileWriter(fullpath, true));
            file1.println(ip + ": " + count);
            file1.close();
        }
		catch(Exception e)
		{
			System.out.println("Error writing to "  + filename);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public int read(String ip)
	{
		String s, readip;
		int readcount = -1;
		
		try
		{
	        BufferedReader readFile = new BufferedReader(new FileReader(fullpath));
	        
	        while ((s = readFile.readLine())!=null)
	        {
	        	if(!s.startsWith("#"))
	        	{
		            StringTokenizer str = new StringTokenizer (s,": ");
		            readip = str.nextToken().trim();
		
		            if(ip.equals(readip))
		            {
		                readcount = Integer.parseInt(str.nextToken().trim());
		            }
	        	}
	        }
		}
		catch(Exception e)
		{
			System.out.println("Error reading from " + filename);
			e.printStackTrace();
		}
		return readcount;
	}
	
	@SuppressWarnings("resource")
	public HashMap<String, Integer> readAll()
	{
		HashMap<String, Integer> data = new HashMap<>();
		String s;
		
		try
		{
	        BufferedReader readFile = new BufferedReader(new FileReader(fullpath));
	        
	        while ((s = readFile.readLine())!=null)
	        {
	        	if(!s.startsWith("#"))
	        	{
		            StringTokenizer str = new StringTokenizer (s,": ");
		            String ip = str.nextToken().trim();
		            int count = Integer.parseInt(str.nextToken().trim());
		            data.put(ip, count);
	        	}
	        }
		}
		catch(Exception e)
		{
			System.out.println("Error while importing data from " + filename);
			e.printStackTrace();
		}
		return data;
	}
	
	public void delete(String ip)
	{
		String s, readip;
		ArrayList<String> writelist = new ArrayList<>();
		
		try
		{
	        BufferedReader readFile = new BufferedReader(new FileReader(fullpath));
	        
	        while ((s = readFile.readLine())!=null)
	        {
	        	if(!s.startsWith("#"))
	        	{
		            StringTokenizer str = new StringTokenizer (s,": ");
		            readip = str.nextToken().trim();
		            
		            if(!ip.equals(readip))
		            {
		            	writelist.add(s);
		            }
	        	}
	        	else
	        	{
	            	writelist.add(s);
	        	}
	        }
	        readFile.close();

            PrintWriter writeFile = new PrintWriter(new FileWriter(fullpath));
            for(String line: writelist)
            {
            	writeFile.println(line);
            }
	        writeFile.close();
	        
		}
		catch(Exception e)
		{
			System.out.println("Error while deleting ip " + ip  + " from " + filename);
			e.printStackTrace();
		}
	}
}

