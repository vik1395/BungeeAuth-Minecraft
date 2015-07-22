package me.vik1395.BungeeAuth;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Logout extends Command
{
	public Logout() {
		super("logout");
	}

	Tables ct = new Tables();
	
	@Override
	public void execute(CommandSender s, String[] args) 
	{
		if(s instanceof ProxiedPlayer)
		{
			ProxiedPlayer p = (ProxiedPlayer)s;
			String status = ct.getStatus(p.getName());
		    if(status.equalsIgnoreCase("online")||Main.plonline.contains(p.getName()))
		    {
		    	ct.setStatus(p.getName(), "logout");
		    	if(Main.plonline.contains(p.getName()))
			    {
		    		Main.plonline.remove(p.getName());
			    }
		    	
		    	ProxyServer ps = Main.plugin.getProxy();
		    	String sname = p.getServer().getInfo().getName();
				
		    	if(!(ps.getServerInfo(Main.authlobby)==null))
				{
					ServerInfo sinf = ps.getServerInfo(Main.authlobby);
					if(!sname.equals(Main.authlobby))
					{
						p.connect(sinf);
					}
				}
				else if(!(ps.getServerInfo(Main.authlobby2)==null))
				{
					ServerInfo sinf = ps.getServerInfo(Main.authlobby2);
					if(!sname.equals(Main.authlobby2))
					{
						p.connect(sinf);
					}
				}
				else
				{
					p.sendMessage(new ComponentBuilder("Error! Unable to connect to AuthLobby.").color(ChatColor.DARK_RED).create());
					System.err.println("[BungeeAuth] AuthLobby and Fallback AuthLobby not found!");
				}
				p.sendMessage(new ComponentBuilder("You have been successfully logged out!").color(ChatColor.GREEN).create());
			}
		    
		    else
		    {
		    	p.sendMessage(new ComponentBuilder("You have already logged out!").color(ChatColor.RED).create());
		    }
		}
	}
	
}
