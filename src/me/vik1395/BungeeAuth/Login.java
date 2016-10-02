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

	private Tables ct = new Tables();
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
		    if(status.equalsIgnoreCase("online") || Main.plonline.contains(p.getName()))
		    {
				p.sendMessage(new ComponentBuilder(Main.already_in).color(ChatColor.GREEN).create());
			}
			
			else
			{
				if(Main.muted.contains(p))
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
								if(Main.pwspam.containsKey(p))
								{
									int tries = Main.pwspam.get(p)+1;
									 
									if(tries>=10)
									{
										Main.startTimeout(p);
										Main.muted.add(p);
										return;
									}
									else
									{
										Main.pwspam.put(p, tries);
									}
								}
								else
								{
									Main.pwspam.put(p, 1);
								}
							}
							p.sendMessage(new ComponentBuilder(Main.wrong_pass).color(ChatColor.RED).create());
						}
						
						else
						{
							Main.plonline.add(p.getName());
							ct.setStatus(p.getName(), "online");
							ListenerClass.movePlayer(p, false);
							ListenerClass.prelogin.get(p.getName()).cancel();
							p.sendMessage(new ComponentBuilder(Main.login_success).color(ChatColor.GREEN).create());
						}
					}
				}
			}
		}
	}
}
