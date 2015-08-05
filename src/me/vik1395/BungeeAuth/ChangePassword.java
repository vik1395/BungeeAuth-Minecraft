package me.vik1395.BungeeAuth;

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

public class ChangePassword extends Command
{
	public ChangePassword()
	{
		super("changepw", "", "cpw", "changepassword", "changepass");
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
			
			//Checks for player entry in Database
			if(pCheck)
			{
				String hash = "";
				if(args.length!=2)
				{
					p.sendMessage(new ComponentBuilder("Usage: /changepw [old password] [new password]").color(ChatColor.RED).create());
				}
				else
				{
					String oldPw = args[0];
					String pwType = t.getType(pName);
					String newPw = args[1];
					PasswordHandler ph = new PasswordHandler();
					
					//checks if current password is correct
					if(!ph.checkPassword(oldPw, pwType, pName))
					{
						p.sendMessage(new ComponentBuilder(Main.wrong_pass).color(ChatColor.RED).create());
					}
					//Hashes new password
					else
					{
						Random rand = new Random();
						int maxp = 7; //Total Password Hashing methods.
						String pType = "" + rand.nextInt(maxp+1);
						hash = ph.newHash(newPw, pType);
						
						t.updatePassword(pName, hash, pType);
						p.sendMessage(new ComponentBuilder(Main.pass_change_success).color(ChatColor.GOLD).create());
					}
				}
			}
			else
			{
				p.sendMessage(new ComponentBuilder(Main.register).color(ChatColor.RED).create());
			}
		}
	}
}
