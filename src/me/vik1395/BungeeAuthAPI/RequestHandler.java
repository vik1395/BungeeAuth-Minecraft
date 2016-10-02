package me.vik1395.BungeeAuthAPI;

import java.util.Date;

import me.vik1395.BungeeAuth.Login;
import me.vik1395.BungeeAuth.Register;
import me.vik1395.BungeeAuth.Tables;
import me.vik1395.BungeeAuth.Password.PasswordHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
*/

/**
 * @author Vik1395
 * @version 1.4
 */
public class RequestHandler 
{
	Tables ct = new Tables();
	
	/**
	 * Check if the given password matches the one stored in the Database.
	 * 
	 * @param playerName The name of the player who's password is being checked.
	 * @param pwToCheck The password that will be checked against the database.
	 * 
	 * @return A boolean value showing whether the password matched the stored one.
	 */
	public boolean checkPassword(String playerName, String pwToCheck)
	{
		String pwType = ct.getType(playerName);
		PasswordHandler ph = new PasswordHandler();
		return ph.checkPassword(pwToCheck, pwType, playerName);
	}
	
	/**
	 * Check if a player is registered on BungeeAuth.
	 * 
	 * @param playerName The name of the player who's data entry is being checked.
	 * 
	 * @return A boolean value showing whether there's an entry of the player in the database or not
	 */
	@Deprecated
	public boolean checkPlayerEntry(String playerName)
	{
		return ct.checkPlayerEntry(playerName);
	}
	
	
	/**
	 * Check if a given player is registered on BungeeAuth.
	 * 
	 * @param playerName The name of the player who's data entry is being checked.
	 * 
	 * @return A boolean value showing whether there's an entry of the player in the database or not
	 */
	public boolean isRegistered(String playerName)
	{
		return ct.checkPlayerEntry(playerName);
	}
	
	/**
	 * Force register a player with a given password.
	 * 
	 * @param player The player who is being force registered.
	 * @param password The password used to register the player.
	 * 
	 * @return A boolean value representing whether the registration was successful.
	 */
	public boolean forceRegister(ProxiedPlayer player, String password)
	{
		return Register.registerPlayer(player, player.getName(), "player@localhost", "1.1.1.1", password);
	}
	
	/**
	 * Force register a player with a given password and email.
	 * 
	 * @param player The player who is being force registered.
	 * @param password The password used to register the player.
	 * @param email The email address of the player being registered.
	 * 
	 * @return A boolean value representing whether the registration was successful.
	 */
	public boolean forceRegister(ProxiedPlayer player, String password, String email)
	{
		return Register.registerPlayer(player, player.getName(), email, "1.1.1.1", password);
	}
	
	/**
	 * Force login a player without requiring a password.
	 * 
	 * @param player The player who is being force registered.
	 * 
	 * @return A boolean value representing whether the login was successful, or failed because the player hasn't been registered yet.
	 */
	public boolean forceLogin(String player)
	{
		return Login.forceLogin(player);
	}
	
	/**
	 * Get the last seen IP address of a player.
	 * 
	 * @param playerName The name of the player who's IP is being retrieved.
	 * 
	 * @return A String representation of the player's last seen IP (192.168.1.1)
	 */
	public String getLastIP(String playerName)
	{
		return ct.getLastIP(playerName);
	}
	
	/**
	 * Get the last seen date of a player.
	 * 
	 * @param playerName The name of the player who's last seen date is being retrieved.
	 * 
	 * @return The player's last seen Date. (2015-09-15 02:16:34)
	 */
	public Date getLastSeen(String playerName)
	{
		return ct.getLastSeen(playerName);
	}
	
	/**
	 * Get the current status of a given player.
	 * 
	 * @param playerName The name of the player who's status is being retrieved.
	 * 
	 * @return A string representation of the player's current status ("online", "offline" or "logout").
	 */
	public String getStatus(String playerName)
	{
		return ct.getStatus(playerName);
	}
	
	/**
	 * Gets the player's registered email address.
	 * 
	 * @param playerName The name of the player who's email is being retrieved.
	 * 
	 * @return A string representation of the player's email (suggest@vik1395.me).
	 */
	public String getEmail(String playerName)
	{
		return ct.getEmail(playerName);
	}
	
	/**
	 * Gets the IP address of a player at the time of registration.
	 * 
	 * @param playerName The name of the player who's IP at the time of registration is being retrieved.
	 * 
	 * @return A String representation of the player's IP at registration (192.168.1.1)
	 */
	public String getRegisteredIP(String playerName)
	{
		return ct.getRegisteredIP(playerName);
	}
	
	/**
	 * Get when the player first registered on BungeeAuth.
	 * 
	 * @param playerName The name of the player who's registration date is being retrieved.
	 * 
	 * @return The player's registration date (2014-07-10 14:10:00)
	 */
	public Date getRegisterDate(String playerName)
	{
		return ct.getRegisterDate(playerName);
	}
}
