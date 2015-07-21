package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.vik1395.BungeeAuth.Utils.YamlGenerator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2014

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
		    seshlength = YamlGenerator.config.getString("Session Length");
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
		
		getProxy().getPluginManager().registerCommand(this, new Command("register") 
				
		{

			@Override
			public void execute(CommandSender arg0, String[] arg1) 
			{
				Register r = new Register();
				r.onCommand(arg0, arg1);
			}
		});
		
		getProxy().getPluginManager().registerCommand(this, new Command("login") 
		{

			@Override
			public void execute(CommandSender arg0, String[] arg1) 
			{
				Login l = new Login();
				l.onCommand(arg0, arg1);
			}
		});
		
		getProxy().getPluginManager().registerCommand(this, new Command("changepw") 
		{

			@Override
			public void execute(CommandSender arg0, String[] arg1) 
			{
				ChangePassword cpw = new ChangePassword();
				cpw.onCommand(arg0, arg1);
			}
		});
		
		getProxy().getPluginManager().registerCommand(this, new Command("reset") 
		{

			@Override
			public void execute(CommandSender arg0, String[] arg1) 
			{
				ResetPlayer r = new ResetPlayer();
				r.onCommand(arg0, arg1);
			}
		});
		
		getProxy().getPluginManager().registerCommand(this, new Command("logout") 
		{

			@Override
			public void execute(CommandSender arg0, String[] arg1) 
			{
				Logout lg = new Logout();
				lg.onCommand(arg0);
			}
		});
		
		getLogger().info("BungeeAuth has successfully started!");
		getLogger().info("Created by Vik1395");
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerDisconnectEvent pde)
	{
		ProxiedPlayer pl = pde.getPlayer();
		plonline.remove(pl.getName());
		ct.setLastSeen(pl.getName(), pl.getAddress().getHostName());
		if(ct.getStatus(pl.getName()).equalsIgnoreCase("online"))
		{
			ct.setStatus(pl.getName(), "offline");
		}

	}
	
	@EventHandler
	public void onLogin(ServerConnectEvent sce)
	{
		ProxiedPlayer pl = sce.getPlayer();
		String sname = sce.getTarget().getName();
		boolean check = ct.checkPlayerEntry(pl.getName());
		
		if(plonline.contains(pl.getName()))
		{}
		
		else if(check)
		{
			if(ct.getStatus(pl.getName()).equalsIgnoreCase("preauth"))
			{
				ProxyServer ps = Main.plugin.getProxy();
				
				if(!(ps.getServerInfo(authlobby)==null))
				{
					ServerInfo sinf = ps.getServerInfo(authlobby);
					if(!sname.equalsIgnoreCase(authlobby))
					{
						pl.connect(sinf);
					}
					ct.setStatus(pl.getName(), "preauth");
				}
				else if(!(ps.getServerInfo(authlobby2)==null))
				{
					ServerInfo sinf = ps.getServerInfo(authlobby2);
					if(!sname.equalsIgnoreCase(authlobby2))
					{
						pl.connect(sinf);
					}
					ct.setStatus(pl.getName(), "preauth");
				}
				else
				{
					pl.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
					System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
					ct.setStatus(pl.getName(), "preauth");
				}
			}
			else
			{
				Date lastseen = ct.getLastSeen(pl.getName());
				String lastip = ct.getLastIP(pl.getName());
				String currip = pl.getAddress().getHostString();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				String sdatenow = df.format(new Date());
				Date datenow = null;
				try 
				{
					datenow = df.parse(sdatenow);
				} 
				catch (ParseException e) 
				{
					e.printStackTrace();
				}
				
				long difference = datenow.getTime() - lastseen.getTime();
				long diffmin = (difference/1000)/60;
				long slen = Long.parseLong(seshlength);
				
				if(currip.equals(lastip) && diffmin<=slen)
				{
						pl.sendMessage(new ComponentBuilder("Welcome back " + pl.getName() + "! Your session has been resumed.").color(ChatColor.GREEN).create());
						ct.setStatus(pl.getName(), "online");
						plonline.add(pl.getName());
				}
				
				else
				{
					if(plonline.contains(pl.getName()))
					{
						plonline.remove(pl.getName());
					}
					ProxyServer ps = Main.plugin.getProxy();
					
					if(!(ps.getServerInfo(authlobby)==null))
					{
						ServerInfo sinf = ps.getServerInfo(authlobby);
						if(!sname.equalsIgnoreCase(authlobby))
						{
							pl.connect(sinf);
						}
						ct.setStatus(pl.getName(), "preauth");
					}
					else if(!(ps.getServerInfo(authlobby2)==null))
					{
						ServerInfo sinf = ps.getServerInfo(authlobby2);
						if(!sname.equalsIgnoreCase(authlobby2))
						{
							pl.connect(sinf);
						}
						ct.setStatus(pl.getName(), "preauth");
					}
					else
					{
						pl.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
						System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
						ct.setStatus(pl.getName(), "preauth");
					}
					
					pl.sendMessage(new ComponentBuilder("Welcome back!, please type /login [password]").color(ChatColor.RED).create());
				}
			}
			
		}
		else if(!check)
		{
			ProxyServer ps = Main.plugin.getProxy();
			
			if(!(ps.getServerInfo(authlobby)==null))
			{
				ServerInfo sinf = ps.getServerInfo(authlobby);
				if(!sname.equalsIgnoreCase(authlobby))
				{
					pl.connect(sinf);
				}
				ct.setStatus(pl.getName(), "preauth");
			}
			else if(!(ps.getServerInfo(authlobby2)==null))
			{
				ServerInfo sinf = ps.getServerInfo(authlobby2);
				if(!sname.equalsIgnoreCase(authlobby2))
				{
					pl.connect(sinf);
				}
				ct.setStatus(pl.getName(), "preauth");
			}
			else
			{
				pl.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
				System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
				ct.setStatus(pl.getName(), "preauth");
			}
			
			if(plonline.contains(pl.getName()))
			{
				plonline.remove(pl.getName());
			}
			
			String emailCh = "";
			if(email)
			{
				emailCh = " [email]";
			}
			pl.sendMessage(new ComponentBuilder("Welcome " + pl.getName() + "! Please type /register [password]" + emailCh + " to register yourself on this server.").color(ChatColor.RED).create());
			//pl.sendMessage(new ComponentBuilder("").color(ChatColor.RED).create());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatEvent(ChatEvent event) {
	  ProxiedPlayer p = (ProxiedPlayer) event.getSender();
	  String msg = event.getMessage();
	  String arr[] = msg.split(" ");
	  String cmd = arr[0];
	  
	  if(plonline.contains(p.getName()))
	  {}
	  else if(cmd.equalsIgnoreCase("/login"))
	  {}
	  else if(cmd.equalsIgnoreCase("/register"))
	  {}
	  else
	  {
		  event.setCancelled(true);
		  p.sendMessage(new ComponentBuilder("You must login to chat or execute commands.").color(ChatColor.GRAY).create());
	  }
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
	public static String seshlength;
	public static boolean email;
}
