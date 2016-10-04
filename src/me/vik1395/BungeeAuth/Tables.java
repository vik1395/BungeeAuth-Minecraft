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

import me.vik1395.BungeeAuth.Utils.Database;
import me.vik1395.BungeeAuth.Utils.MySQL;
import me.vik1395.BungeeAuth.Utils.SQLite;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Tables 
{
	Database db;
	
	public Tables()
	{
		if(!Main.sqlite)
		{
			db = new MySQL(Main.host, Main.port, Main.dbName, Main.username, Main.pass);
		}
		else
		{
			String s = Main.plugin.getDataFolder().getAbsolutePath()+"/SQLite.db";
			System.out.println(s);
			db = new SQLite(s);
		}
	}
	
	protected void Create() throws SQLException
	{
		Connection c = db.openConnection();
		Statement statement = c.createStatement();
		
		if(!Main.sqlite)
		{
			ResultSet bacheck = statement.executeQuery("SHOW TABLES LIKE 'BungeeAuth';");
			if(!bacheck.next())
			{
				statement.execute("CREATE TABLE `BungeeAuth` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `playername` VARCHAR(255) NOT NULL, `password` VARCHAR(255) NOT NULL, `pwtype` TINYINT(2) UNSIGNED NOT NULL, "
						+ "`email` VARCHAR(255) NOT NULL DEFAULT 'player@localhost', `registeredip` VARCHAR(30) NOT NULL, `registerdate` DATE NOT NULL DEFAULT '0001-01-01', `lastip` VARCHAR(30) NOT NULL, `lastseen` DATETIME NOT NULL DEFAULT '0001-01-01 01:01:01', "
						+ "`version` VARCHAR(10) NOT NULL, `status` VARCHAR(10) NOT NULL, PRIMARY KEY (`id`));");
				statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `lastip`, `version`, `status`) VALUES ('bungeeauth','1000:5b42403130656137333635:c23a89d4186147d701d71e2036defc00c76438e33ec5e38ed5e8310b9e378d20b290c9ecad558b488acf0012c774d52b2aa9918c40b2a091febf2f963a6567b2',"
						+ "'6','player@localhost','1.1.1.1','1.1.1.1','" + Main.version + "','ver'" +");");
			}
		}
		
		else
		{
			ResultSet bacheck = statement.executeQuery("SELECT * FROM sqlite_master WHERE name ='BungeeAuth' and type='table'; ");
			if(!bacheck.next())
			{
				statement.execute("CREATE TABLE `BungeeAuth` (`id` INTEGER NOT NULL , `playername` VARCHAR(255) NOT NULL, `password` VARCHAR(255) NOT NULL, `pwtype` TINYINT(2) NOT NULL, "
						+ "`email` VARCHAR(255) NOT NULL DEFAULT 'player@localhost', `registeredip` VARCHAR(30) NOT NULL, `registerdate` DATE NOT NULL DEFAULT '0001-01-01', `lastip` VARCHAR(30) NOT NULL, `lastseen` DATETIME NOT NULL DEFAULT '0001-01-01 01:01:01', "
						+ "`version` VARCHAR(10) NOT NULL, `status` VARCHAR(10) NOT NULL, PRIMARY KEY (`id`));");
				statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `lastip`, `version`, `status`) VALUES ('bungeeauth','1000:5b42403130656137333635:c23a89d4186147d701d71e2036defc00c76438e33ec5e38ed5e8310b9e378d20b290c9ecad558b488acf0012c774d52b2aa9918c40b2a091febf2f963a6567b2',"
						+ "'6','player@localhost','1.1.1.1','1.1.1.1','" + Main.version + "','ver'" +");");
			}
		}
		
		statement.close();
		c.close();
		db.closeConnection();
	}
	
	
	public boolean checkPlayerEntry(String playername)
	{
		boolean check = false;
		Connection c = db.openConnection();
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
		
		statement.close();
		c.close();
		
		} catch (SQLException e) 
		{
			Main.plugin.getLogger().severe("[BungeeAuth] There is a problem with the connection to the MySQL Database!");
			e.printStackTrace();
		}
		
		return check;
	}
	
	public void newPlayerEntry(String player, String phash, String ptype, String email, String regip, String regdate, String lastip, String lastseen) throws SQLException
	{
		Connection c = db.openConnection();
		Statement statement = c.createStatement();
		statement.execute("INSERT INTO BungeeAuth (`playername`,`password`,`pwtype`, `email`, `registeredip`, `registerdate`, `lastip`, `lastseen`, `version`, `status`) VALUES ('" + player.toLowerCase() + "','" + phash + "','" + ptype +  "','" + email + "','" + regip + "','" + regdate + "','" + lastip + "','" + lastseen + "','" + Main.version + "','logout'" +");");
		statement.close();
		c.close();
	}
	
	protected void removePlayerEntry(String playername)
	{
		Connection c = db.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("DELETE FROM BungeeAuth WHERE playername='" + playername.toLowerCase() + "';");
			statement.close();
			c.close();
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
		Connection c = db.openConnection();
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
			
			statement.close();
			c.close();
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
		Connection c = db.openConnection();
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
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return pwType;
	}
	
	protected void updatePassword(String playername, String newPw, String pwType)
	{
		Connection c = db.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET password='" + newPw + "',pwtype='" + pwType + "' WHERE playername='" + playername.toLowerCase() + "';");
			statement.close();
			c.close();
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
		Connection c = db.openConnection();
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
			
			statement.close();
			c.close();
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
			Main.plugin.getLogger().severe("[BungeeAuth] Unable to parse last seen data from MySQL database for " + playername);
		}
		return lastseendate;
	}
	
	public Date getRegisterDate(String playername)
	{
		boolean check = false;
		String regdate = "";
		Connection c = db.openConnection();
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
				regdate = pcheck.getString("registerdate");
				check=true;
			}
			if(!check)
			{
				regdate = "";
			}
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date registerdate = null;
		try 
		{
			registerdate = df.parse(regdate);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			Main.plugin.getLogger().severe("[BungeeAuth] Unable to parse last seen data from MySQL database for " + playername);
		}
		return registerdate;
	}
	
	public String getEmail(String playername)
	{
		boolean check = false;
		String email = "";
		Connection c = db.openConnection();
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
				email = pcheck.getString("email");
				check=true;
			}
			if(!check)
			{
				email = "";
			}
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return email;
	}
	
	public String getLastIP(String playername)
	{
		boolean check = false;
		String lastip = "";
		Connection c = db.openConnection();
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
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return lastip;
	}
	
	public String getRegisteredIP(String playername)
	{
		boolean check = false;
		String regip = "";
		Connection c = db.openConnection();
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
				regip = pcheck.getString("registeredip");
				check=true;
			}
			if(!check)
			{
				regip = "";
			}
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return regip;
	}
	
	public String getStatus(String playername)
	{
		boolean check = false;
		String status = "";
		Connection c = db.openConnection();
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
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
	protected void setStatus(String playername, String status)
	{
		Connection c = db.openConnection();
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET status='" + status + "' WHERE playername='" + playername.toLowerCase() + "';");
			
			statement.close();
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	protected void setLastSeen(String playername, String ip, String date)
	{
		Connection c = db.openConnection();
		
		if(date==null)
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			date = df.format(new Date());
		}
		
		if(ip==null)
		{
			ip="1.1.1.1";
		}
		
		try
		{
			Statement statement = c.createStatement();
			statement.execute("UPDATE BungeeAuth SET lastip='" + ip + "' WHERE playername='" + playername.toLowerCase() + "';");
			statement.execute("UPDATE BungeeAuth SET lastseen='" + date + "' WHERE playername='" + playername.toLowerCase() + "';");
			
			statement.close();
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean reachedLimit(String ip)
	{
		int entries = 0;
		Connection c = db.openConnection();
		try
		{
			Statement statement = c.createStatement();
			ResultSet pcheck = statement.executeQuery("SELECT COUNT(*) FROM BungeeAuth WHERE registeredip = '" + ip + "';");
			if(pcheck.next())
			{
				entries = pcheck.getInt(1);
			}
			statement.close();
			c.close();
			
			if(entries>=Main.entperip)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}