package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.vik1395.BungeeAuth.Utils.YamlGenerator;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Main extends Plugin implements Listener
{
	Tables ct = new Tables();
	Login ln = new Login();
	static List<String> plonline = new ArrayList<String>();
	
	public void onEnable()
	{
        getProxy().getPluginManager().registerListener(this, this);
		plugin = this;
		
		YamlGenerator yg = new YamlGenerator();
	    yg.setup();
	    
		try 
		{
		    host = YamlGenerator.config.getString("Host");
		    port = YamlGenerator.config.getString("Port");
		    dbName = YamlGenerator.config.getString("DBName");
		    username = YamlGenerator.config.getString("Username");
		    pass = YamlGenerator.config.getString("Password");
		    lobby = YamlGenerator.config.getString("Lobby");
		    lobby2 = YamlGenerator.config.getString("Fallback Lobby");
		    authlobby = YamlGenerator.config.getString("AuthLobby");
		    authlobby2 = YamlGenerator.config.getString("Fallback AuthLobby");
		    email  = YamlGenerator.config.getBoolean("Ask Email");
		    seshlength = YamlGenerator.config.getInt("Session Length");
		} 
		catch(Exception ex) 
		{
		    System.err.println("[BungeeAuth] Your Config file is missing or broken. Please reset the file.");
		    ex.printStackTrace();
		}
		
		try 
		{
			ct.Create();
		}
		catch (SQLException e) 
		{
			getLogger().info("There was an error while creating MySQL Tables.");
			e.printStackTrace();
		}
		
		getProxy().getPluginManager().registerCommand(this, new Register());
		getProxy().getPluginManager().registerCommand(this, new Login());
		getProxy().getPluginManager().registerCommand(this, new ChangePassword());
		getProxy().getPluginManager().registerCommand(this, new ResetPlayer());
		getProxy().getPluginManager().registerCommand(this, new Logout());
		
		getLogger().info("BungeeAuth has successfully started!");
		getLogger().info("Created by Vik1395");
		
	}
	
	public static Plugin plugin;
	public static String authlobby;
	public static String authlobby2;
	public static String lobby;
	public static String lobby2;
	public static String host;
	public static String port;
	public static String dbName;
	public static String username;
	public static String pass;
	public static int seshlength;
	public static boolean email;
}
