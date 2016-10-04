package me.vik1395.BungeeAuth;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
	public Logout() 
	{
		super("logout", "");
	}

	private Tables ct = new Tables();
	
	@Override
	public void execute(CommandSender s, String[] args) 
	{
		if(s instanceof ProxiedPlayer)
		{
			ProxiedPlayer p = (ProxiedPlayer)s;
			String status = ct.getStatus(p.getName());
			
			if(args.length>0&&args[0].equals("force"))
			{
				if(p.hasPermission("bauth.forcelogout"))
				{
					if(args.length==2)
					{
						String pname = args[1];
						ct.setStatus(pname, "logout");
				    	ct.setLastSeen(pname, null, "1001-01-01 01:01:01");
				    	if(Main.plonline.contains(pname))
					    {
				    		Main.plonline.remove(pname);
					    }
				    	
				    	if(Main.plugin.getProxy().getPlayer(pname)!=null)
				    	{
				    		ProxiedPlayer pl = Main.plugin.getProxy().getPlayer(pname);
							ListenerClass.movePlayer(pl, true);
							ListenerClass.startTask(pl);
							ListenerClass.guest.add(pl.getName());
							pl.sendMessage(new ComponentBuilder(Main.logout_success).color(ChatColor.GREEN).create());
				    	}
				    	p.sendMessage(new ComponentBuilder(Main.force_logout).color(ChatColor.GREEN).create());
					}
					else
					{
						p.sendMessage(new ComponentBuilder("Usage: /logout force [player]").color(ChatColor.RED).create());
					}
				}
				else
				{
					p.sendMessage(new ComponentBuilder(Main.no_perm).color(ChatColor.RED).create());
				}
				return;
			}
			
		    if(status.equalsIgnoreCase("online")||Main.plonline.contains(p.getName()))
		    {
		    	ct.setStatus(p.getName(), "logout");
		    	ct.setLastSeen(p.getName(), null, "1001-01-01 01:01:01");
		    	if(Main.plonline.contains(p.getName()))
			    {
		    		Main.plonline.remove(p.getName());
			    }
		    	
				ListenerClass.movePlayer(p, true);
				ListenerClass.startTask(p);
				p.sendMessage(new ComponentBuilder(Main.logout_success).color(ChatColor.GREEN).create());
			}
		    
		    else
		    {
		    	p.sendMessage(new ComponentBuilder(Main.already_out).color(ChatColor.RED).create());
		    }
		}
	}
	
}
