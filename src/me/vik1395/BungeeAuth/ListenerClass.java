package me.vik1395.BungeeAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/*

Author: Vik1395
Project: BungeeAuth

Copyright 2015

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class ListenerClass implements Listener
{
	private Tables ct = new Tables();
	public static HashMap<String, ScheduledTask> prelogin = new HashMap<>();
	public static List<String> guest = new ArrayList<String>();
	public static HashMap<String, String> sendbackto = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreLogin(PreLoginEvent ple)
	{
		if(!ple.getConnection().getName().matches(Main.allowedun))
		{
			ple.setCancelReason(ChatColor.RED + Main.illegal_name);
			ple.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent sce)
	{
		String servername = sce.getTarget().getName();
		String status = ct.getStatus(sce.getPlayer().getName());
		if (!(sce.getTarget().getName().equals(Main.authlobby)))
		{
			sendbackto.put(sce.getPlayer().getName(), servername);
			if (!(status.equals("online")))
			{
				sce.setCancelled(true);
				movePlayer(sce.getPlayer(), true);
				kickPlayerIfServerDead(sce.getPlayer(), sce.getTarget());
			}
			return;
		}
		// if no return server, kick player
		else if (Main.strict_authlobby && (status.equals("online") || !(sendbackto.containsKey(sce.getPlayer().getName()))))
		{
			sce.setCancelled(true);
			sce.getPlayer().disconnect(new TextComponent(Main.error_no_server));
			return;
		}
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PostLoginEvent ple)
	{
		ProxiedPlayer pl = ple.getPlayer();
		boolean check = ct.checkPlayerEntry(pl.getName());

		if(!check)
		{
			String emailCh = "";
			if(Main.email)
			{
				emailCh = " [email]";
			}
			pl.sendMessage(new ComponentBuilder(Main.welcome_register.replace("%player%", pl.getName()).replace("%email%", emailCh)).color(ChatColor.RED).create());
			startTask(pl);
		}

		//Checks for player entry in Database
		else if(check)
		{
			ct.setStatus(pl.getName(), "offline");
			Date lastseen = ct.getLastSeen(pl.getName());
			String lastip = ct.getLastIP(pl.getName());
			String currip = pl.getAddress().getHostString();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			String sdatenow = df.format(new Date());
			Date datenow = null;
			try
			{
				datenow = df.parse(sdatenow);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}

			long difference = datenow.getTime() - lastseen.getTime();
			long diffmin = (difference/1000)/60;

			//checks if player's session is still available
			if(currip.equals(lastip) && diffmin<=Main.seshlength)
			{
					pl.sendMessage(new ComponentBuilder(Main.welcome_resume.replace("%player%", pl.getName())).color(ChatColor.GREEN).create());
					ct.setStatus(pl.getName(), "online");
					if(!Main.plonline.contains(pl.getName()))
					{
						Main.plonline.add(pl.getName());
					}
					movePlayer(pl, false);
			}
			else
			{
				if(Main.plonline.contains(pl.getName()))
				{
					Main.plonline.remove(pl.getName());
				}
				pl.sendMessage(new ComponentBuilder(Main.welcome_login).color(ChatColor.RED).create());
				startTask(pl);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatEvent(ChatEvent event) {
		ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		String msg = event.getMessage();
		String arr[] = msg.split(" ");
		String cmd = arr[0];

		if(!Main.plonline.contains(p.getName()) && !cmd.equalsIgnoreCase("/login") && !cmd.equalsIgnoreCase("/register"))
		{
			event.setCancelled(true);
			p.sendMessage(new ComponentBuilder(Main.pre_login).color(ChatColor.GRAY).create());
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerDisconnectEvent pde)
	{
		ProxiedPlayer pl = pde.getPlayer();
		sendbackto.remove(pl.getName());
		if(Main.plonline.contains(pl.getName()))
		{
			Main.plonline.remove(pl.getName());
			ct.setLastSeen(pl.getName(), pl.getAddress().getAddress().getHostAddress(), null);
		}
		ct.setStatus(pl.getName(), "offline");
		if(guest.contains(pl.getName()))
		{
			for(int i=0; i<guest.size();i++)
			{
				if(guest.get(i).equals(pl.getName()))
				{
					guest.remove(i);
				}
			}
		}
	}

	public static void movePlayer(ProxiedPlayer pl, boolean authlobby)
	{
		ProxyServer ps = Main.plugin.getProxy();
		Tables ct = new Tables();
		if(authlobby)
		{
			ServerInfo sinf = null;
			if(!(ps.getServerInfo(Main.authlobby)==null))
			{
				sinf = ps.getServerInfo(Main.authlobby);
				pl.connect(sinf);
			}
			else
			{
				pl.disconnect(new TextComponent(Main.error_authlobby));
				System.err.println("[BungeeAuth] AuthLobby not found!");
			}

		}
		else if(sendbackto.containsKey(pl.getName()) && !(ps.getServerInfo(sendbackto.get(pl.getName()))==null))
		{
			ServerInfo sinf = ps.getServerInfo(sendbackto.get(pl.getName()));
			sendbackto.remove(pl.getName());
			pl.connect(sinf);
		}
	}


	protected static void startTask(final ProxiedPlayer pl)
	{
		guest.add(pl.getName());
		if(Main.gseshlength==0)
		{
			return;
		}
		else
		{
			prelogin.put(pl.getName(), Main.plugin.getProxy().getScheduler().schedule(Main.plugin, new Runnable() {

				@Override
				public void run()
				{
					if(guest.contains(pl.getName()))
					{
						for(int i=0; i<guest.size();i++)
						{
							if(guest.get(i).equals(pl.getName()))
							{
								guest.remove(i);
							}
						}
					}
					pl.disconnect(new TextComponent(Main.nologin_kick));
				}

			}, (long) Main.gseshlength, TimeUnit.SECONDS));
		}
	}


	protected static void kickPlayerIfServerDead(final ProxiedPlayer pl, final ServerInfo sinf)
	{
		try {
			sinf.ping(new Callback<ServerPing>() {
				@Override
				public void done(ServerPing ping, Throwable throwable) {
					if (ping==null)
					{
						pl.disconnect(new TextComponent(Main.error_no_server));
					}
				}
			});
		}
		catch (Exception e)
		{
			pl.disconnect(new TextComponent(Main.error_no_server));
		}
	}

}