<?php
/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */


//BungeeAuth PHP API Sample Script 1.0
$HOST = "127.0.0.1"; //Bungee Server IP
$PORT = 1395; //PHP API Port
$PASS = "pZe8qNCC6s5NKvYj"; //API Password
$REQUEST = ""; //The request message being sent

/*
Request Method - "request:variable1:variable2..."

----------------------------------------
Requests:

checkPassword:playername:password       (Return "true" or "false")

checkPlayerEntry:playername             (Return "true" or "false")

getLastIP:playername                    (Return "xx.xx.xx.xx")

getLastSeen:playername                  (Return "MM-dd-yyyy HH:mm:ss")

getStatus:playername                    (Return Status)

getEmail:playername                     (Return "suggest@vik1395.me")

getRegisteredIP:playername              (Return "xx.xx.xx.xx")

getRegisterDate:playername              (Return "MM-dd-yyyy HH:mm:ss")
---------------------------------------

Empty requests like " :playername" will get a message that says 
"Empty request received!".

Requests with empty variables like "getEmail: " will get a message that 
says "Invalid Request!".

Sending only a request or a variable like "getEmail" or "playername" 
will get a message saying "Invalid Request!".

Sending only 1 variable with a checkPassword request like "CheckPassword:password" 
will get a message saying "Illegal format! Usage: checkPassword:playerName:password".

*/

$sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creates a new Socket
		or die("Error! Socket could not be created!\n");

$succ = socket_connect($sock, $HOST, $PORT) //Connects to API Socket
		or die("Error! Could not establish a connection to the host!\n");

socket_write($sock, $PASS . "\n" . $REQUEST . "\n", strlen($PASS.$REQUEST) + 2) //Writes to API Socket
		or die("Error! Failed to write to socket!\n");

$reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Stores the reply from the server.
		or die("Error! Failed to read from socket!\n");

echo $reply; //Prints reply

?>
