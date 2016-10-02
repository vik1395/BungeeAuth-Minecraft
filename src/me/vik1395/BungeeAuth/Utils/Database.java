package me.vik1395.BungeeAuth.Utils;

import java.sql.Connection;
import java.sql.ResultSet;

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
 * Abstract Database class, serves as a base for any connection method (MySQL, SQLite, etc.)
 * 
 * @author -_Husky_-
 * @author tips48
 * @author Vik1395
 */
public abstract class Database {

    /*
     * Plugin instance, use for plugin.getDataFolder() and plugin.getLogger()
     */

    /*
     * Creates a new Database
     * 
     * @param plugin
     *            Plugin instance
     */
    protected Database() {
    }

    /*
     * Opens a connection with the database
     * 
     * @return Connection opened
     */
    public abstract Connection openConnection();

    /*
     * Checks if a connection is open with the database
     * 
     * @return true if a connection is open
     */
    public abstract boolean checkConnection();

    /*
     * Gets the connection with the database
     * 
     * @return Connection with the database, null if none
     */
    public abstract Connection getConnection();

    /*
     * Closes the connection with the database
     */
    public abstract void closeConnection();
    
    /*
     * Query the SQL database
     * 
     * @param query
     * @return Connection with the database, null if none
     */
    public abstract ResultSet querySQL(String query);
    
    /*
     * Update the SQL database
     * 
     * @param update
     */
    public abstract void updateSQL(String update);
}