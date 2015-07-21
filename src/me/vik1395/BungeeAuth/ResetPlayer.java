package me.vik1395.BungeeAuth;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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

public class ResetPlayer 
{
	public void onCommand(CommandSender s, String args[])
	{
		if(s instanceof ProxiedPlayer)
		{
			boolean pCheck = false;
			ProxiedPlayer p = (ProxiedPlayer)s;
			Tables t = new Tables();
			String status = t.getStatus(p.getName());
			
			if(!p.hasPermission("bauth.reset"))
			{
				p.sendMessage(new ComponentBuilder("You do not have permission to use this command!").color(ChatColor.RED).create());
			}
			else if(status.equalsIgnoreCase("offline"))
			{
				p.sendMessage(new ComponentBuilder("You have to login before you can use this command!").color(ChatColor.RED).create());
			}
			else
			{
				boolean ch = true;
				try
				{
					@SuppressWarnings("unused")
					String argcheck = args[0];
					
				}
				catch(Exception e)
				{
					ch = false;
					p.sendMessage(new ComponentBuilder("Usage: /reset [Player Name]").color(ChatColor.RED).create());
				}
				if(ch == true)
				{
					String pName = args[0];
					
					pCheck = t.checkPlayerEntry(pName);
					
					if(!pCheck)
					{
						p.sendMessage(new ComponentBuilder(pName + " has not been registered on this server.").color(ChatColor.RED).create());
					}
					else
					{
						t.removePlayerEntry(pName);
						p.sendMessage(new ComponentBuilder("Successfully reset " + pName + "'s password.").color(ChatColor.GOLD).create());
						try
						{
							ProxiedPlayer resetp = ProxyServer.getInstance().getPlayer(pName);
							resetp.disconnect(TextComponent.fromLegacyText("You have been kicked from the server!"));
						}
						catch(Exception e)
						{
							
						}
					}
				}
			}
		}
	}
}
