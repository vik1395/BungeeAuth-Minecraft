package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.vik1395.BungeeAuth.Utils.YamlGenerator;
import me.vik1395.BungeeAuthAPI.PHP.APISockets;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
	private Tables ct;
	public static List<String> plonline, muted;
	public static HashMap<String, Integer> pwspam;
	public static HashMap<String, Runnable> guestserverchecker;
	public static Plugin plugin;
	public static int seshlength, phpport, gseshlength, entperip, errlim, pwtimeout, pwtries;
	public static boolean sqlite, email, phpapi, guestfailsafe;
	public static String version, authlobby, authlobby2, lobby, lobby2, host, port, dbName, username, pass, register, 
    reg_success, already_reg, login_success, already_in, logout_success, already_out, reset_noreg, reset_success,
    no_perm, pass_change_success, wrong_pass, welcome_resume, welcome_login, welcome_register, pre_login,
    error_authlobby, error_lobby, phppass, reg_limit, illegal_name, nologin_kick, allowedun, spammed_password, force_register, force_login, force_logout;
	
	public void onEnable()
	{
		plugin = this;
		version = this.getDescription().getVersion();
		
		YamlGenerator yg = new YamlGenerator();
		yg.saveDefaultConfig();
		yg.saveDefaultMessage();
		loadYaml();
		
		ct = new Tables();
		
		try 
		{
			ct.Create();
		}
		catch (SQLException e) 
		{
			getLogger().info("Unable to create MySQL table! Please make sure login details are correct and the SQL server accepts connections!");
			e.printStackTrace();
		}

		plonline = new ArrayList<String>();
		muted = new ArrayList<String>();
		pwspam = new HashMap<String, Integer>();
		guestserverchecker = new HashMap<String, Runnable>();
		
        getProxy().getPluginManager().registerListener(this, new ListenerClass());
		getProxy().getPluginManager().registerCommand(this, new Register());
		getProxy().getPluginManager().registerCommand(this, new Login());
		getProxy().getPluginManager().registerCommand(this, new ChangePassword());
		getProxy().getPluginManager().registerCommand(this, new ResetPlayer());
		getProxy().getPluginManager().registerCommand(this, new Logout());
		
		if(phpapi)
		{
			getLogger().info("Enabling PHP API...");
			
			APISockets sockets = new APISockets();
			sockets.enable(phpport);
		}
		
		if(guestfailsafe)
		{
			checkGuestServer();
		}
		
		getLogger().info("BungeeAuth has successfully started!");
		getLogger().info("Created by Vik1395");
		
	}
	
	public void onDisable()
	{
		APISockets.disable();
		if(plonline.size()>0)
		{
			for(int i=0; i<plonline.size();i++)
			{
				ct.setLastSeen(plonline.get(i), null, "1001-01-01 01:01:01");
			}
		}
	}
	
	private void loadYaml()
	{

	    sqlite = YamlGenerator.config.getBoolean("Use SQLite");
		host = YamlGenerator.config.getString("Host");
	    port = "" + YamlGenerator.config.getInt("Port");
	    dbName = YamlGenerator.config.getString("DBName");
	    username = YamlGenerator.config.getString("Username");
	    pass = YamlGenerator.config.getString("Password");
	    lobby = YamlGenerator.config.getString("Lobby");
	    lobby2 = YamlGenerator.config.getString("Fallback Lobby");
	    authlobby = YamlGenerator.config.getString("AuthLobby");
	    authlobby2 = YamlGenerator.config.getString("Fallback AuthLobby");
	    email = YamlGenerator.config.getBoolean("Ask Email");
	    seshlength = YamlGenerator.config.getInt("Session Length");
	    gseshlength = YamlGenerator.config.getInt("Guest Session Length");
	    allowedun = YamlGenerator.config.getString("Legal Usernames Characters");
	    entperip = YamlGenerator.config.getInt("Users per IP"); 
	    phpapi = YamlGenerator.config.getBoolean("Enable PHP API");
	    phpport = YamlGenerator.config.getInt("PHP API Port");
	    phppass = YamlGenerator.config.getString("API Password");
	    errlim = YamlGenerator.config.getInt("API Error Limit");
	    pwtries = YamlGenerator.config.getInt("Password Tries");
	    pwtimeout = YamlGenerator.config.getInt("Wrong Password Timeout");
	    guestfailsafe = YamlGenerator.config.getBoolean("Guest Server Failsafe Check");

		illegal_name = YamlGenerator.message.getString("illegal_name");
		register = YamlGenerator.message.getString("register");
		reg_success = YamlGenerator.message.getString("reg_success");
		already_reg = YamlGenerator.message.getString("already_reg");
	    reg_limit = YamlGenerator.message.getString("reg_limit");
	    nologin_kick = YamlGenerator.message.getString("nologin_kick");
		login_success = YamlGenerator.message.getString("login_success");
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
	    spammed_password = YamlGenerator.message.getString("spammed_password");
	    force_register = YamlGenerator.message.getString("force_register");
	    force_login = YamlGenerator.message.getString("force_login");
	    force_logout = YamlGenerator.message.getString("force_logout");
	}
	
	protected static void startTimeout(final String p)
	{
		Main.plugin.getProxy().getScheduler().schedule(Main.plugin, new Runnable() {

			@Override
			public void run() 
			{
				pwspam.remove(p);
				muted.remove(p);
			}
			
		}, (long) Main.pwtimeout, TimeUnit.MINUTES);
	}
	
	private void checkGuestServer()
	{
		ServerInfo sinf = null;
		if(Main.plugin.getProxy().getServerInfo(Main.authlobby)!=null)
		{
			sinf = Main.plugin.getProxy().getServerInfo(Main.authlobby);
		}
		else if(Main.plugin.getProxy().getServerInfo(Main.authlobby2)!=null)
		{
			sinf = Main.plugin.getProxy().getServerInfo(Main.authlobby2);
		}
		
		final ServerInfo sinfo = sinf;
		
		Main.plugin.getProxy().getScheduler().schedule(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				for(int i=0; i<ListenerClass.guest.size();i++)
				{
					ProxiedPlayer p = getProxy().getPlayer(ListenerClass.guest.get(i));
					if(p!=null)
					{
						if(sinfo==null)
						{
							p.disconnect(new TextComponent(Main.error_authlobby));
							System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
						}
						else if(!p.getServer().getInfo().equals(sinfo))
						{
							p.connect(sinfo);
						}
					}
					else
					{
						ListenerClass.guest.remove(i);
					}
				}
			}
		}, 0l, 10l, TimeUnit.SECONDS);
	}
}
