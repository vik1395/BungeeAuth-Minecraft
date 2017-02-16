package me.vik1395.BungeeAuth.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Level;
import me.vik1395.BungeeAuth.Main;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2016

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class SQLite extends Database {
    private final String database;
    private Connection connection;
    
    public SQLite(String database) {
        this.database = database;
        this.connection = null;
    }

    @Override
    public Connection openConnection() {
    	Connection c = null;
        try 
        {
	        Class.forName("org.sqlite.JDBC");
	        String url = "jdbc:sqlite:" + this.database;
	        c = DriverManager.getConnection(url);
	    } 
        catch ( Exception e ) 
        {
            Main.plugin.getLogger().log(Level.SEVERE, "Error while creating SQLite Database", e);
        }

        return this.connection = c;
    }

    @Override
    public boolean checkConnection() {
        return this.connection != null;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void closeConnection() {
        if (this.connection != null) {
            try {
            	this.connection.close();
            } catch (SQLException e) {
            	Main.plugin.getLogger().severe("Error closing the MySQL Connection!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResultSet querySQL(String query) {
        Connection c = null;

        if (checkConnection()) {
            c = getConnection();
        } else {
            c = openConnection();
        }

        Statement s = null;

        try {
            s = c.createStatement();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        ResultSet ret = null;

        try {
            ret = s.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return ret;
    }

    @Override
    public void updateSQL(String update) {

        Connection c = null;

        if (checkConnection()) {
            c = getConnection();
        } else {
            c = openConnection();
        }

        Statement s = null;

        try {
            s = c.createStatement();
            s.executeUpdate(update);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        closeConnection();

    }

}