package me.vik1395.BungeeAuth;

import java.util.HashMap;

import me.vik1395.BungeeAuth.Password.PasswordHandler;
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

public class Login extends Command
{
	public Login() 
	{
		super("login", "");
	}

	private static Tables ct = new Tables();
	HashMap<String, Integer> wrongpass = new HashMap<>();
	
	@Override
	public void execute(CommandSender s, String[] args) 
	{
		if(s instanceof ProxiedPlayer)
		{
			boolean pCheck = false;
			ProxiedPlayer p = (ProxiedPlayer)s;
			String pName = p.getName();
			String status = ct.getStatus(p.getName());
			
			if(args.length>0&&args[0].equals("force"))
			{
				if(p.hasPermission("bauth.forcelogin"))
				{
					if(args.length>1)
					{
						if(forceLogin(args[1]))
						{
							p.sendMessage(new ComponentBuilder(Main.force_login).color(ChatColor.GREEN).create());
						}
						else
						{
							p.sendMessage(new ComponentBuilder(Main.reset_noreg.replace("%player%", args[1])).color(ChatColor.RED).create());
						}
					}
					else
					{
						p.sendMessage(new ComponentBuilder("Usage: /login force [player]").color(ChatColor.RED).create());
					}
				}
				else
				{
					p.sendMessage(new ComponentBuilder(Main.no_perm).color(ChatColor.RED).create());
				}
				return;
			}
			
		    if(status.equalsIgnoreCase("online") || Main.plonline.contains(p.getName()))
		    {
				p.sendMessage(new ComponentBuilder(Main.already_in).color(ChatColor.GREEN).create());
			}
			
			else
			{
				if(Main.muted.contains(pName))
				{
					p.sendMessage(new ComponentBuilder(Main.spammed_password).color(ChatColor.RED).create());
					return;
				}
				
				pCheck = ct.checkPlayerEntry(pName);
				
				if(!pCheck)
				{
					String emailCh = "";
					if(Main.email)
					{
						emailCh = " [email]";
					}
					p.sendMessage(new ComponentBuilder(Main.register.replace("%email%", emailCh)).color(ChatColor.RED).create());
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
							if(Main.pwtries>0)
							{
								if(Main.pwspam.containsKey(pName))
								{
									int tries = Main.pwspam.get(pName)+1;
									 
									if(tries>=Main.pwtries)
									{
										Main.startTimeout(pName);
										Main.muted.add(pName);
									}
									else
									{
										Main.pwspam.put(pName, tries);
									}
								}
								else
								{
									Main.pwspam.put(pName, 1);
								}
							}
							p.sendMessage(new ComponentBuilder(Main.wrong_pass).color(ChatColor.RED).create());
						}
						
						else
						{
							if(ListenerClass.guest.contains(pName))
							{
								for(int i=0; i<ListenerClass.guest.size();i++)
								{
									if(ListenerClass.guest.get(i).equals(pName))
									{
										ListenerClass.guest.remove(i);
									}
								}
							}
							Main.plonline.add(pName);
							ct.setStatus(pName, "online");
							ListenerClass.movePlayer(p, false);
							ListenerClass.prelogin.get(pName).cancel();
							p.sendMessage(new ComponentBuilder(Main.login_success).color(ChatColor.GREEN).create());
						}
					}
				}
			}
		}
	}
	
	public static boolean forceLogin(String pName)
	{
		if(!ct.checkPlayerEntry(pName))
		{
			return false;
		}
		Main.plonline.add(pName);
		ct.setStatus(pName, "online");
		ct.setLastSeen(pName, null, null);
		ProxiedPlayer target = Main.plugin.getProxy().getPlayer(pName);
		if(target!=null)
		{
			ListenerClass.prelogin.get(pName).cancel();
			ListenerClass.movePlayer(target, false);
			if(ListenerClass.guest.contains(pName))
			{
				for(int i=0; i<ListenerClass.guest.size();i++)
				{
					if(ListenerClass.guest.get(i).equals(pName))
					{
						ListenerClass.guest.remove(i);
					}
				}
			}
			target.sendMessage(new ComponentBuilder(Main.login_success).color(ChatColor.GREEN).create());
		}
		return true;
	}
}
