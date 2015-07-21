package me.vik1395.BungeeAuth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import me.vik1395.BungeeAuth.Password.PBKDF2Hash;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
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

public class ChangePassword 
{
	public void onCommand(CommandSender s, String args[])
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
				String hash = "";
				boolean ch = true;
				try
				{
					@SuppressWarnings("unused")
					String argcheck = args[0];
					@SuppressWarnings("unused")
					String argcheck2 = args[1];
					
				}
				catch(Exception e)
				{
					ch = false;
					p.sendMessage(new ComponentBuilder("Usage: /changepw [old password] [new password]").color(ChatColor.RED).create());
				}
				if(ch == true)
				{
					String oldPw = args[0];
					String storedPw = t.getPassword(pName);
					String newPw = args[1];
					boolean PwCheck = false;
					
					PBKDF2Hash ph = new PBKDF2Hash();
					try 
					{
						PwCheck = ph.validatePassword(oldPw, storedPw);
					} 
					catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
					{
						System.out.println("Error in Validation");
						e.printStackTrace();
					}
					
					if(!PwCheck)
					{
						p.sendMessage(new ComponentBuilder("The current password you entered is wrong! Please enter it again.").color(ChatColor.RED).create());
					}
					
					else
					{
						PBKDF2Hash ph2 = new PBKDF2Hash();
						try 
						{
							hash = ph2.generateStrongPasswordHash(newPw);
						} 
						catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
						{
							e.printStackTrace();
						}
						
						t.updatePassword(pName, hash);
						p.sendMessage(new ComponentBuilder("Password was changed successfully.").color(ChatColor.GOLD).create());
					}
				}
			}
			else
			{
				p.sendMessage(new ComponentBuilder("You have not registered yet. please use the /register command first.").color(ChatColor.RED).create());
			}
		}
	}
}
