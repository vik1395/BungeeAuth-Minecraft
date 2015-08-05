package me.vik1395.BungeeAuth;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

public class Register extends Command
{
	public Register() 
	{
		super("register", "");
	}

	@Override
	public void execute(CommandSender s, String[] args) 
	{
		if(s instanceof ProxiedPlayer)
		{
			boolean pCheck = false;
			ProxiedPlayer p = (ProxiedPlayer)s;
			String pName = p.getName();
			Tables t = new Tables();
			pCheck = t.checkPlayerEntry(pName);
			
			if(pCheck)
			{
				p.sendMessage(new ComponentBuilder(Main.already_reg).color(ChatColor.RED).create());
			}
			
			else if(Main.plonline.contains(p.getName()))
		    {
		    	p.sendMessage(new ComponentBuilder(Main.already_in).color(ChatColor.GREEN).create());
		    }
			
			else
			{
				String email = "";
				boolean ch = true;
				try
				{
					//checks if email is required in the config. player@localhost is used as a placeholder
					if(Main.email)
					{
						email = args[1];
					}
					
					else
					{
						email = "player@localhost";
					}
					@SuppressWarnings("unused")
					String argcheck = args[0];
					
				}
				catch(Exception e)
				{
					ch = false;
					if(Main.email)
					{
				    	p.sendMessage(new ComponentBuilder("Usage: /register [password] [email]").color(ChatColor.RED).create());
					}
					else
					{
				    	p.sendMessage(new ComponentBuilder("Usage: /register [password]").color(ChatColor.RED).create());
					}
				}
				
				//checks if its ok to hash and save the player's password
				if(ch == true)
				{
					PasswordHandler ph = new PasswordHandler();
					Random rand = new Random();
					int maxp = 7; //Total Password Hashing methods.
					Date dNow = new Date();
				    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
					
					String Pw = args[0];
					String pType = "" + rand.nextInt(maxp+1);
					String regip = p.getAddress().getAddress().getHostAddress();
					String regdate = ft.format(dNow);
					String lastip = regip;
					String lastseen = regdate;
					String hash = ph.newHash(Pw, pType);
					
					//creates a new SQL entry with the player's details.
					try 
					{
						t.newPlayerEntry(pName, hash, pType, email, regip, regdate, lastip, lastseen);
						p.sendMessage(new ComponentBuilder(Main.reg_success).color(ChatColor.GOLD).create());
					} 
					catch (SQLException e) 
					{
						System.err.println("[BungeeAuth] Error when creating a new player in the MySQL Database");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
