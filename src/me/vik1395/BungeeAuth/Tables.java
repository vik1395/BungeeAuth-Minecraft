package me.vik1395.BungeeAuth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.vik1395.BungeeAuth.Utils.MySQL;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2014

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Tables 
{
	public void Create() throws SQLException
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		Statement statement = c.createStatement();
		
		ResultSet bacheck = statement.executeQuery("SHOW TABLES LIKE 'BungeeAuth';");
		if(!bacheck.next())
		{
			statement.execute("CREATE TABLE `BungeeAuth` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `playername` VARCHAR(255) NOT NULL, `password` VARCHAR(255) NOT NULL, `pwtype` TINYINT(2) UNSIGNED NOT NULL, "
					+ "`email` VARCHAR(255) NOT NULL DEFAULT 'player@localhost', `registeredip` VARCHAR(30) NOT NULL, `registerdate` DATE NOT NULL DEFAULT '0001-01-01', `lastip` VARCHAR(30) NOT NULL, `lastseen` DATETIME NOT NULL DEFAULT '0001-01-01 01:01:01', "
					+ "`version` VARCHAR(10) NOT NULL, `status` VARCHAR(10) NOT NULL, PRIMARY KEY (`id`));");
			statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `lastip`, `version`, `status`) VALUES ('bungeeauth','1000:5b42403130656137333635:c23a89d4186147d701d71e2036defc00c76438e33ec5e38ed5e8310b9e378d20b290c9ecad558b488acf0012c774d52b2aa9918c40b2a091febf2f963a6567b2',"
					+ "'6','player@localhost','1.1.1.1','1.1.1.1','1.0.1','ver'" +");");
		}
		else
		{
			ResultSet verCheck = c.getMetaData().getColumns(null, null, "BungeeAuth", "version");
			ResultSet bunCheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = 'bungeeauth';");
			
			if(!verCheck.next())
			{
				Update();
			}
			else if(!bunCheck.next())
			{
				Update2();
			}
			else
			{}
			
		}
		
		MySQL.closeConnection();
	}
	
	public void Update() throws SQLException
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		Statement statement = c.createStatement();
		
		ResultSet bacheck = statement.executeQuery("SHOW TABLES LIKE 'BungeeAuth';");
		if(bacheck.next())
		{
			//statement.execute("CREATE TABLE `BungeeAuth` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `playername` VARCHAR(255) NOT NULL, `password` VARCHAR(255) NOT NULL, `pwtype` TINYINT(2) UNSIGNED NOT NULL DEFAULT 0, PRIMARY KEY (`id`));");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `pwtype` = '6'");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `email` VARCHAR(255) NOT NULL DEFAULT 'player@localhost'");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `registeredip` VARCHAR(30) NOT NULL");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `registerdate` DATE NOT NULL DEFAULT '0001-01-01'");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `lastip` VARCHAR(30) NOT NULL");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `lastseen` DATETIME NOT NULL DEFAULT '0001-01-01 01:01:01'");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `version` VARCHAR(10) NOT NULL");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `status` VARCHAR(10) NOT NULL");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `version` = '1.0.1'");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `registerdate` = '0001-01-01'");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `lastseen` = '0001-01-01 01:01:01'");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `email` = 'player@localhost'");
			statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `lastip`, `version`, `status`) VALUES ('bungeeauth','1000:5b42403130656137333635:c23a89d4186147d701d71e2036defc00c76438e33ec5e38ed5e8310b9e378d20b290c9ecad558b488acf0012c774d52b2aa9918c40b2a091febf2f963a6567b2',"
					+ "'6','player@localhost','1.1.1.1','1.1.1.1','1.0.1','ver'" +");");
		}
	}
	
	public void Update2() throws SQLException
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		Statement statement = c.createStatement();
		
		ResultSet bacheck = statement.executeQuery("SHOW TABLES LIKE 'BungeeAuth';");
		if(bacheck.next())
		{
			statement.executeUpdate("ALTER TABLE `BungeeAuth` DROP `registerdate`");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `registerdate` DATE NOT NULL DEFAULT '0001-01-01' AFTER `registeredip`");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` DROP `lastseen`");
			statement.executeUpdate("ALTER TABLE `BungeeAuth` ADD `lastseen` DATETIME NOT NULL DEFAULT '0001-01-01 01:01:01' AFTER `lastip`");
			//statement.executeUpdate("UPDATE `BungeeAuth` SET `registerdate` = '0001-01-01'");
			//statement.executeUpdate("UPDATE `BungeeAuth` SET `lastseen` = '0001-01-01 01:01:01'");
			statement.executeUpdate("UPDATE `BungeeAuth` SET `version` = '1.0.1'");
			statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `lastip`, `version`, `status`) VALUES ('bungeeauth','1000:5b42403130656137333635:c23a89d4186147d701d71e2036defc00c76438e33ec5e38ed5e8310b9e378d20b290c9ecad558b488acf0012c774d52b2aa9918c40b2a091febf2f963a6567b2',"
					+ "'6','player@localhost','1.1.1.1','1.1.1.1','1.0.1','ver'" +");");
		}
	}
	
	
	public boolean checkPlayerEntry(String playername)
	{
		boolean check = false;
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		Statement statement;
		try {
			statement = c.createStatement();
		
		ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
		if(!pcheck.next())
		{
			check=false;
		}
		else
		{
			check=true;
		}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("[BungeeAuth] There is a problem with the connection to the MySQL Database!");
		}
		
		return check;
	}
	
	public void newPlayerEntry(String player, String phash, String ptype, String email, String regip, String regdate, String lastip, String lastseen) throws SQLException
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		Statement statement = c.createStatement();
		statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `registerdate`, `lastip`, `lastseen`, `version`, `status`) VALUES ('" + player.toLowerCase() + "','" + phash + "','" + ptype +  "','" + email + "','" + regip + "','" + regdate + "','" + lastip + "','" + lastseen + "','1.0','logout'" +");");
		
	}
	
	public void removePlayerEntry(String playername)
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("DELETE FROM BungeeAuth WHERE playername='" + playername.toLowerCase() + "';");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getPassword(String playername)
	{
		boolean check = false;
		String hashedPW = "";
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
			if(!pcheck.next())
			{
				check=false;
			}
			else
			{
				hashedPW = pcheck.getString("password");
				check=true;
			}
			if(!check)
			{
				hashedPW = "";
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return hashedPW;
	}
	
	public String getType(String playername)
	{
		boolean check = false;
		String pwType = "";
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
			if(!pcheck.next())
			{
				check=false;
			}
			else
			{
				pwType = pcheck.getString("pwtype");
				check=true;
			}
			if(!check)
			{
				pwType = "";
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return pwType;
	}
	
	public void updatePassword(String playername, String newPw)
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET password='" + newPw + "' WHERE playername='" + playername.toLowerCase() + "';");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Date getLastSeen(String playername)
	{
		boolean check = false;
		String lastseen = "";
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
			if(!pcheck.next())
			{
				check=false;
			}
			else
			{
				lastseen = pcheck.getString("lastseen");
				check=true;
			}
			if(!check)
			{
				lastseen = "";
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date lastseendate = null;
		try 
		{
			lastseendate = df.parse(lastseen);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			System.err.println("[BungeeAuth] Unable to parse last seen data from MySQL database for " + playername);
		}
		
		return lastseendate;
	}
	
	public String getLastIP(String playername)
	{
		boolean check = false;
		String lastip = "";
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
			if(!pcheck.next())
			{
				check=false;
			}
			else
			{
				lastip = pcheck.getString("lastip");
				check=true;
			}
			if(!check)
			{
				lastip = "";
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return lastip;
	}
	
	public String getStatus(String playername)
	{
		boolean check = false;
		String status = "";
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT * FROM BungeeAuth WHERE playername = '" + playername.toLowerCase() + "';");
			if(!pcheck.next())
			{
				check=false;
			}
			else
			{
				status = pcheck.getString("status");
				check=true;
			}
			if(!check)
			{
				status = "";
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return status;
	}
	public void setStatus(String playername, String status)
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET status='" + status + "' WHERE playername='" + playername.toLowerCase() + "';");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void setLastSeen(String playername, String ip)
	{
		MySQL MySQL = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		Connection c = null;
		c = MySQL.openConnection();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String seen = df.format(new Date());
		
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET lastip='" + ip + "' WHERE playername='" + playername.toLowerCase() + "';");
			statement.execute("UPDATE BungeeAuth SET lastseen='" + seen + "' WHERE playername='" + playername.toLowerCase() + "';");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}