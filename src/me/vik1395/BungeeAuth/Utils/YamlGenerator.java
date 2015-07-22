package me.vik1395.BungeeAuth.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import me.vik1395.BungeeAuth.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class YamlGenerator 
{
	public static Configuration config;
    public static ConfigurationProvider cProvider;
    public static File cFile;
    
	public void setup()
	{
		File cFolder = new File(Main.plugin.getDataFolder(),"");
		
		if (!cFolder.exists()) 
		{
	        cFolder.mkdir();
		}
		
		cFile = new File(Main.plugin.getDataFolder() + "/config.yml");
		
		if (!cFile.exists()) 
		{
	        save();
		}
		
		cProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
	    try 
	    {
	        config = cProvider.load(cFile);
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void save()
	{
		try 
        {
        	String file = ""
        			+ "Host: 127.0.0.1\n"
        			+ "# Please enter the Host of your MySQL Database here.\n"
        			+ "Port: \'3306\'\n"
        			+ "# Please enter the port where your MySQL Database is hosted.\n"
        			+ "Username: root\n"
        			+ "# The Username which should be used to auth against the Database.\n"
        			+ "Password: \'pass\'\n"
        			+ "# The Password which should be used to auth against the Database. If you don't have a password, please leave two quotation marks (\') in this field.\n"
        			+ "DBName: Bungee\n"
        			+ "# The name of the database where BungeeAuth\'s Tables shall be created.\n"
        			+ "Lobby: Lobby\n"
        			+ "# The name of the lobby server.\n"
        			+ "Fallback Lobby: Lobby2\n"
        			+ "# The name of the fallback lobby server in case the main lobby is down.\n"
        			+ "AuthLobby: AuthLobby\n"
        			+ "# The name of the lobby where players are pushed before they authenticate. leave it same as normal lobby if you don't have an auth lobby.\n"
        			+ "Fallback AuthLobby: AuthLobby2\n"
        			+ "# The name of the fallback AuthLobby server in case the main AuthLobby is down.\n"
        			+ "Ask Email: False\n"
        			+ "# Set this to True if you want the plugin to prompt users to register their email when they login to the server for the first time.\n"
        			+ "Session Length: \'5\'\n"
        			+ "# How long (in minutes) does the user's session remains running after a player quits. This allows the user to log back in withing the time frame without \n"
        			+ "# having to type their password again. If user logs in from a different IP, they will be asked to type their password again for security reasons.";
        	
            FileWriter fw = new FileWriter(cFile);
			BufferedWriter out = new BufferedWriter(fw);
            out.write(file);
            out.close();
            fw.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
	}
}
