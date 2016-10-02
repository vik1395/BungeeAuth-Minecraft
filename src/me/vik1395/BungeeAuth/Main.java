package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import me.vik1395.BungeeAuth.Password.PasswordHandler;
import me.vik1395.BungeeAuth.Utils.YamlGenerator;
import me.vik1395.BungeeAuthAPI.PHP.APISockets;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
	private static Tables ct;
	public static List<String> plonline = new ArrayList<String>();
	public static Plugin plugin;
	public static int seshlength, phpport, gseshlength, entperip, errlim, pwtimeout, pwtries;
	public static boolean sqlite, email, phpapi;
	public static String version, authlobby, authlobby2, lobby, lobby2, host, port, dbName, username, pass, register, 
    reg_success, already_reg, login_success, already_in, logout_success, already_out, reset_noreg, reset_success,
    no_perm, pass_change_success, wrong_pass, welcome_resume, welcome_login, welcome_register, pre_login,
    error_authlobby, error_lobby, phppass, reg_limit, illegal_name, nologin_kick, allowedun, spammed_password;
	public static HashMap<ProxiedPlayer, Integer> pwspam = new HashMap<>();
	public static List<ProxiedPlayer> muted = new ArrayList<ProxiedPlayer>();
	
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
	}
	
	protected static void startTimeout(final ProxiedPlayer pl)
	{
		Main.plugin.getProxy().getScheduler().schedule(Main.plugin, new Runnable() {

			@Override
			public void run() 
			{
				pwspam.remove(pl);
				muted.remove(pl);
			}
			
		}, (long) Main.pwtimeout, TimeUnit.MINUTES);
	}
	
	public static boolean registerPlayer(ProxiedPlayer p, String pName, String email, String regip, String pw)
	{
		PasswordHandler ph = new PasswordHandler();
		Random rand = new Random();
		int maxp = 7; //Total Password Hashing methods.
		Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
		
		String pType = "" + rand.nextInt(maxp+1);
		String regdate = ft.format(dNow);
		String lastip = regip;
		String lastseen = regdate;
		String hash = ph.newHash(pw, pType);
		
		//creates a new SQL entry with the player's details.
		try 
		{
			ct.newPlayerEntry(pName, hash, pType, email, regip, regdate, lastip, lastseen);
			ListenerClass.prelogin.get(p.getName()).cancel();
			p.sendMessage(new ComponentBuilder(Main.reg_success).color(ChatColor.GOLD).create());
			return true;
		} 
		catch (SQLException e) 
		{
			Main.plugin.getLogger().severe("[BungeeAuth] Error when creating a new player in the MySQL Database");
			e.printStackTrace();
			return false;
		}
	}
	
	
}
