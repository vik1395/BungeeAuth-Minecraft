BungeeAuth now has its own Database importer. You may find it [HERE](http://www.spigotmc.org/resources/bungeeauth-importer.6729/). It currently only supports xAuth's SQL Database.

Please rate the plugin once you have tried it so that it can help others who haven't tried it yet. ![:)](https://www.spigotmc.org/styles/default/xenforo/clear.png)

BungeeAuth is a useful authentication plugin for Bungee servers, especially for those that operate in offline mode. BungeeAuth is similar to xAuth/AuthMe plugins of Bukkit. It uses MySQL database to store and read player login data. The players' passwords are hashed and salted before storing adding them to the database. The main advantage of this plugin is that you only need to place it in the Bungee plugins folder and you are good to go. You dont have to place a plugin in every Bukkit server (Like xAuth).

I suggest that you use a separate void bukkit server for authentication where the players are stuck in one place, unable to move. This is because, since this is a Bungee plugin, it is not able to prevent players from moving before they login.

Please report any issues with this plugin[ ](https://github.com/vik1395/BungeeAuth/issues)[HERE](https://github.com/vik1395/BungeeAuth-Minecraft/issues)

If you like my work, please consider donating, I would greatly appreciate it. [![Image](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=EJSLPHPJQYUQ4&lc=US&item_name=Spigot%20and%20BungeeCord%20Plugin%20Dev%20%28Vik1395%29&item_number=Spigot&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted)

**Plugins For Additional Security**
-------------

I highly recommend that you use one or both of these plugins in your Bukkit/Spigot servers to protect them from being exploited.

[BungeeAuthValidator](https://www.spigotmc.org/resources/bungeeauthvalidator.10678/)

[IPWhitelist](https://www.spigotmc.org/resources/ipwhitelist.61/)

**Permissions**
-------------
bauth.reset - This permission is needed for players who can use the /reset command.

**Commands**
-------------
    /register [password] - This command is used by first time players to register themselves in the server.
    
    /login [password] - This command is used by returning users to login to the server.
    
    /changepw [old password] [new password] - This command is used by players who want to change their password. It can only be used once the player logs in.
    
    /reset [player name] - This command is used by admins who want to reset a player's password. Doing this un-registers the player and there is no way of getting his/her password back.
    
    A player needs to have the "bauth.reset" permission to use this command.
    
    /logout - This command successfully logs out the player and moves them to the AuthLobby.

**Config**
-------------

The config.yml for this plugin is located in its data folder, similar to bukkit plugins. It looks similar to this:

    # BungeeAuth Config File
    
    Host: 127.0.0.1
    
    # Please enter the Host of your MySQL Database here.
    
    Port: 3306
    
    # Please enter the port where your MySQL Database is hosted.
    
    Username: root
    
    # The Username which should be used to auth against the Database.
    
    Password: 'pass'
    
    # The Password which should be used to auth against the Database. If you don't have a password, please leave two single quotation marks (') in this field.
    
    DBName: Bungee
    
    #The name of the database where BungeeAuth's Tables shall be created.
    
    Lobby: Lobby
    
    # The name of the lobby server.
    
    Fallback Lobby: Lobby2
    
    # The name of the fallback lobby server in case the main lobby is down.
    
    AuthLobby: AuthLobby
    
    # The name of the lobby where players are pushed before they authenticate. leave it same as normal lobby if you don't have an auth lobby.
    
    Fallback AuthLobby: AuthLobby2
    
    # The name of the fallback AuthLobby server in case the main AuthLobby is down.
    
    Ask Email: false
    
    # Set this to True if you want the plugin to prompt users to register their email when they login to the server for the first time.
    
    Session Length: 5
    
    # How long (in minutes) does the user's session remains running after a player quits. This allows the user to log back in within the time frame without
    
    # having to type their password again. If user logs in from a different IP, they will be asked to type their password again for security reasons.
    
    Guest Session Length: 60
    
    # How long (in seconds) the user has before they have to register or login.
    
    Legal Usernames Characters: "[a-zA-Z0-9_]*"
    
    # Allowed Characters in Usernames.
    
    Users per IP: 5
    
    # Number of users that can register (not login) from the same IP.
    
    #---------------------------- PHP API AREA ----------------------------#
    
    Enable PHP API: false
    
    # Enabling this will make the plugin listen to API requests from a port.
    
    PHP API Port: 1395
    
    # The port that the plugin will be listening for API requests on.
    
    API Password: 'pZe8qNCC6s5NKvYj'
    
    # The password required in the API requests for a response. If you don't have a password, please leave two single quotation marks (') in this field.
    
    API Error Limit: 5
    
    # Set the maximum number of wrong api password attempts used by a php script before it's ip gets blocked. IP will be blocked until removed from apithreats.yml

The Plugin automatically creates the MySQL Tables. You just have to input the MySQL Database Host, Login details and Database name in the config file.

Until the player logs in, he/she wont be able to use any commands except for /register and /login. After disconnecting, depending on the Session Length set by the admin, the player will have a certain amount of time within which they can log back in to the server without having to retype their password, considering they log in from the same IP.

When the player logs in successfully, He/she will be teleported to the Lobby server (if it is different from the Authentication Lobby).

Lobby server and Auth Lobby server in the config **CANNOT** be the same.

You can check out a tutorial of the plugin in Spanish **[HERE](https://www.youtube.com/watch?v=5ptJhP31Oxo)**

This plugin is licensed under [CC Attribution-NonCommercial-ShareAlike 4.0 International](http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US). 

In very basic terms, Do whatever you want with the code of this plugin, as long as you give credits to the author and/or the plugin itself.

Please leave a comment or rate the plugin, and tell me what you think of it. I would really appreciate it.

To have a secure password, I suggest you follow this:
![enter image description here](http://imgs.xkcd.com/comics/password_strength.png)
