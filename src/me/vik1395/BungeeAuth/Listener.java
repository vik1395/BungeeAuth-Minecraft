package me.vik1395.BungeeAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Listener 
{
	Tables ct = new Tables();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(ServerConnectEvent sce)
	{
		ProxiedPlayer pl = sce.getPlayer();
		String sname = sce.getTarget().getName();
		boolean check = ct.checkPlayerEntry(pl.getName());
		
		//Checks for player entry in Database
		if(check)
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
			
			//checks if player's session is still available
			if(currip.equals(lastip) && diffmin<=Main.seshlength)
			{
					pl.sendMessage(new ComponentBuilder("Welcome back " + pl.getName() + "! Your session has been resumed.").color(ChatColor.GREEN).create());
					ct.setStatus(pl.getName(), "online");
					if(!Main.plonline.contains(pl.getName()))
					{
						Main.plonline.add(pl.getName());
					}
			}
			else
			{
				if(Main.plonline.contains(pl.getName()))
				{
					Main.plonline.remove(pl.getName());
				}
				ProxyServer ps = Main.plugin.getProxy();
				
				if(!(ps.getServerInfo(Main.authlobby)==null))
				{
					ServerInfo sinf = ps.getServerInfo(Main.authlobby);
					if(!sname.equalsIgnoreCase(Main.authlobby))
					{
						pl.connect(sinf);
					}
				}
				else if(!(ps.getServerInfo(Main.authlobby2)==null))
				{
					ServerInfo sinf = ps.getServerInfo(Main.authlobby2);
					if(!sname.equalsIgnoreCase(Main.authlobby2))
					{
						pl.connect(sinf);
					}
				}
				else
				{
					pl.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
					System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
				}
				
				pl.sendMessage(new ComponentBuilder("Welcome back!, please type /login [password]").color(ChatColor.RED).create());
			}
		}
		
		else if(!check)
		{
			ProxyServer ps = Main.plugin.getProxy();
			
			if(!(ps.getServerInfo(Main.authlobby)==null))
			{
				ServerInfo sinf = ps.getServerInfo(Main.authlobby);
				if(!sname.equalsIgnoreCase(Main.authlobby))
				{
					pl.connect(sinf);
				}
			}
			else if(!(ps.getServerInfo(Main.authlobby2)==null))
			{
				ServerInfo sinf = ps.getServerInfo(Main.authlobby2);
				if(!sname.equalsIgnoreCase(Main.authlobby2))
				{
					pl.connect(sinf);
				}
			}
			else
			{
				pl.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
				System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
			}
			
			String emailCh = "";
			if(Main.email)
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
	  
	  if(!Main.plonline.contains(p.getName()) && !cmd.equalsIgnoreCase("/login") && !cmd.equalsIgnoreCase("/register"))
	  {
		  event.setCancelled(true);
		  p.sendMessage(new ComponentBuilder("You must login to chat or execute commands.").color(ChatColor.GRAY).create());
	  }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerDisconnectEvent pde)
	{
		ProxiedPlayer pl = pde.getPlayer();
		Main.plonline.remove(pl.getName());
		ct.setLastSeen(pl.getName(), pl.getAddress().getAddress().getHostAddress());
		ct.setStatus(pl.getName(), "offline");
	}
}
