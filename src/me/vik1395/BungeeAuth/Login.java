package me.vik1395.BungeeAuth;

import me.vik1395.BungeeAuth.Password.PasswordHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2014

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Login 
{
	Tables ct = new Tables();
	public void onCommand(CommandSender s, String args[])
	{
		if(s instanceof ProxiedPlayer)
		{
			boolean pCheck = false;
			ProxiedPlayer p = (ProxiedPlayer)s;
			String pName = p.getName();
			String status = ct.getStatus(p.getName());
		    if(status.equalsIgnoreCase("online"))
		    {
				p.sendMessage(new ComponentBuilder("You are already logged in!").color(ChatColor.GREEN).create());
			}
		    
		    else if(Main.plonline.contains(p.getName()))
		    {
		    	p.sendMessage(new ComponentBuilder("You are already logged in!").color(ChatColor.GREEN).create());
		    }
			
			else
			{
				
				pCheck = ct.checkPlayerEntry(pName);
				
				
				if(!pCheck)
				{
					String emailCh = "";
					if(Main.email)
					{
						emailCh = " [email]";
					}
					p.sendMessage(new ComponentBuilder("You haven't registered yet! Please type /register [password]" + emailCh).color(ChatColor.RED).create());
				}
				
				else
				{
					boolean PwCheck = false;
					String pwType = ct.getType(pName);
					
					boolean ch = true;
					try
					{
						@SuppressWarnings("unused")
						String argcheck = args[0];
						
					}
					catch(Exception e)
					{
						ch = false;
						p.sendMessage(new ComponentBuilder("Usage: /login [password]").color(ChatColor.RED).create());
					}
					if(ch == true)
					{
						String currentPw = args[0];
						PasswordHandler ph = new PasswordHandler();
						PwCheck = ph.checkPassword(currentPw, pwType, pName);
						
						if(!PwCheck)
						{
							p.sendMessage(new ComponentBuilder("The password you entered is wrong! Please enter it again.").color(ChatColor.RED).create());
						}
						
						else
						{
							Main.plonline.add(p.getName());
							ct.setStatus(p.getName(), "online");
							ProxyServer ps = Main.plugin.getProxy();
							if(!(ps.getServerInfo(Main.lobby)==null))
							{
								ServerInfo sinf = ps.getServerInfo(Main.lobby);
								String sname = p.getServer().getInfo().getName();
								if(!sname.equals(Main.lobby))
								{
									p.connect(sinf);
								}
							}
							else if(!(ps.getServerInfo(Main.lobby2)==null))
							{
								ServerInfo sinf = ps.getServerInfo(Main.lobby2);
								String sname = p.getServer().getInfo().getName();
								if(!sname.equals(Main.lobby2))
								{
									p.connect(sinf);
								}
							}
							else
							{
								p.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
								System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
							}
							p.sendMessage(new ComponentBuilder("You have logged in successfully! You may now chat and use commands.").color(ChatColor.GREEN).create());
						}
					}
				}
			}
		}
	}
}
