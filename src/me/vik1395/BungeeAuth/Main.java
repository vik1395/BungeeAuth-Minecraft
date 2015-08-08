package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.vik1395.BungeeAuth.Utils.YamlGenerator;
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

public class Main extends Plugin
{
	Tables ct = new Tables();
	Login ln = new Login();
	public static List<String> plonline = new ArrayList<String>();
	public static Plugin plugin;
	public static int seshlength;
	public static boolean email;
	public static String authlobby, authlobby2, lobby, lobby2, host, port, dbName, username, pass, register, 
    reg_success, already_reg, login_success, already_in, logout_success, already_out, reset_noreg, reset_success,
    no_perm, pass_change_success, wrong_pass, welcome_resume, welcome_login, welcome_register, pre_login,
    error_authlobby, error_lobby;
	
	public void onEnable()
	{
        getProxy().getPluginManager().registerListener(this, new ListenerClass());
		plugin = this;
		
		YamlGenerator yg = new YamlGenerator();
		yg.saveDefaultConfig();
		yg.saveDefaultMessage();
		loadYaml();
		
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
	
	public void onDisable()
	{
		if(plonline.size()>0)
		{
			for(int i=0; i<plonline.size();i++)
			{
				ct.setLastSeen(plonline.get(i), null, "1001-01-01 01:01:01");
			}
		}
	}
	
	public static void loadYaml()
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
	    
		register = YamlGenerator.message.getString("register");
		reg_success = YamlGenerator.message.getString("reg_success");
		already_reg = YamlGenerator.message.getString("already_reg");
		login_success = YamlGenerator.message.getString("register");
		already_in = YamlGenerator.message.getString("already_in");
		logout_success = YamlGenerator.message.getString("logout_success");
		already_out = YamlGenerator.message.getString("already_out");
		reset_noreg = YamlGenerator.message.getString("reset_noreg");
		reset_success = YamlGenerator.message.getString("reset_success");
		no_perm = YamlGenerator.message.getString("no_perm");
		pass_change_success = YamlGenerator.message.getString("pass_change_success");
		wrong_pass = YamlGenerator.message.getString("wrong_pass");
		welcome_resume = YamlGenerator.message.getString("welcome_resume");
		welcome_login = YamlGenerator.message.getString("welcome_login");
		welcome_register = YamlGenerator.message.getString("welcome_register");
		pre_login = YamlGenerator.message.getString("pre_login");
	    error_authlobby = YamlGenerator.message.getString("error_authlobby");
	    error_lobby = YamlGenerator.message.getString("error_lobby");
	}
}
