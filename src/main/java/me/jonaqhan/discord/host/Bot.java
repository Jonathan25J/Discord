package me.jonaqhan.discord.host;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import me.jonaqhan.discord.Main;
import me.jonaqhan.discord.livechat.Chat;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot implements Listener {
  
	//static ListenerAdapter[] listenerAdapters = new ListenerAdapter[] { new Chat() };

	private Main plugin;
	static JDA jda;


}
