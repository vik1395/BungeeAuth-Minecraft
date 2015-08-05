package me.vik1395.BungeeAuth.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
	public static Configuration message;
	
    public void saveDefaultConfig()
	{
		File file = new File(Main.plugin.getDataFolder(), "config.yml");
		ConfigurationProvider cProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
		
        if (!Main.plugin.getDataFolder().exists())
        {
			Main.plugin.getDataFolder().mkdir();
        }
		try
		{
	        if (!file.exists()) 
	        {
					Files.copy(Main.plugin.getResourceAsStream("config.yml"), file.toPath());
	        }
		    config = cProvider.load(file);
		}
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
    
    public void saveDefaultMessage()
	{
		File file = new File(Main.plugin.getDataFolder(), "messages.yml");
		ConfigurationProvider cProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
		
        if (!Main.plugin.getDataFolder().exists())
        {
			Main.plugin.getDataFolder().mkdir();
        }
		try
		{
	        if (!file.exists()) 
	        {
					Files.copy(Main.plugin.getResourceAsStream("messages.yml"), file.toPath());
	        }
		    message = cProvider.load(file);
		}
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
}
