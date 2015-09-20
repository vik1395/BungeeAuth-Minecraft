package me.vik1395.BungeeAuthAPI.PHP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import me.vik1395.BungeeAuth.Main;
import me.vik1395.BungeeAuthAPI.RequestHandler;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

/*
 * Based on code from:
 * http://goo.gl/NEJphz
*/

public class APISockets 
{
	private static boolean run = false;
	
	public static void disable()
	{
		Main.plugin.getLogger().info("Closing API port...");
		run = false;
	}
	
	public void enable(final int port)
	{
		Main.plugin.getProxy().getScheduler().runAsync(Main.plugin, new Runnable() {
			@Override
			public void run()
			{
				run = true;
				ServerSocket listenSock = null;
				Socket sock = null;
			
				try 
				{
					Main.plugin.getLogger().info("BungeeAuth is now listening on port " + port + " for API requests");
					listenSock = new ServerSocket(port);
					
					while (run) 
					{
						sock = listenSock.accept();
						
						BufferedReader reader =	new BufferedReader(new InputStreamReader(sock.getInputStream()));
						BufferedWriter writer =	new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
						String response = "";
						String pass = reader.readLine();
						if(pass.equals(Main.phppass))
						{
							String test = reader.readLine();
							response = ProcessRequest(test);
							writer.write(response + "\n");
						}
						writer.close();
						reader.close();
						sock.close();
					}
					
				} 
				catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}
		});
	}
	
	private String ProcessRequest(String strreq)
	{
		RequestHandler rh = new RequestHandler();
		String[]rawreq = strreq.split(":");
		
		if(rawreq.length<2)
		{
			return "Invalid Request!";
		}
		
		switch(rawreq[0])
		{
			case("checkPassword"):
			{
				if(rawreq.length<3)
				{
					return "Illegal format! Usage: checkPassword:playerName:password";
				}
				
				if(rh.checkPassword(rawreq[1], rawreq[2]))
				{
					return "true";
				}
				else
				{
					return "false";
				}
			}
			
			case("checkPlayerEntry"):
			{
				if(rh.checkPlayerEntry(rawreq[1]))
				{
					return "true";
				}
				else
				{
					return "false";
				}
			}
			
			case("getLastIP"):
			{
				return rh.getLastIP(rawreq[1]);
			}
			
			case("getLastSeen"):
			{
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH);
				return df.format(rh.getLastSeen(rawreq[1]));
			}
			
			case("getStatus"):
			{
				return rh.getStatus(rawreq[1]);
			}
			
			case("getEmail"):
			{
				return rh.getEmail(rawreq[1]);
			}
			
			case("getRegisteredIP"):
			{
				return rh.getRegisteredIP(rawreq[1]);
			}
			
			case("getRegisterDate"):
			{
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH);
				return df.format(rh.getRegisterDate(rawreq[1]));
			}
			
			case(""):
			{
				return "Empty request received!";
			}
		}
		
		return "Invalid Request!";
	}
}
