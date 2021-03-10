package me.jonaqhan.discord.livechat;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jonaqhan.discord.Main;
import me.jonaqhan.discord.tools.Chatt;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Chat extends ListenerAdapter implements Listener {
	public static Main plugin;
	public static JDA jda;
	static GatewayIntent[] gatewayIntents = new GatewayIntent[] {};

	public void Build() {
		JDABuilder jdaBuilder = JDABuilder.createDefault(plugin.getConfig().getString("Token"));
		jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, gatewayIntents);
		// jdaBuilder.addEventListeners(listenerAdapters);
		try {
			jda = jdaBuilder.build();
			jda.awaitReady();
			jda.addEventListener(this);
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Chat(Main plugin) {
		this.plugin = plugin;
		Build();
		Bukkit.getPluginManager().registerEvents(this, plugin);

	}

	@EventHandler
	public void chatEvent(AsyncPlayerChatEvent e) { // speler stuurt bericht naar discord
		String message = e.getMessage();

		TextChannel Channel = jda.getTextChannelById(plugin.getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;

		String content = "**" + e.getPlayer().getName() + ":** " + message;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.chat"));
		Channel.sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent e) { 
		String p = e.getPlayer().getName();
		String message = plugin.getConfig().getString("Messages.join");
		TextChannel Channel = jda.getTextChannelById(plugin.getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;

		String content = "**" +p + "** " +  message;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.join"));
		Channel.sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void Leave(PlayerQuitEvent e) { 
		String p = e.getPlayer().getName();
		String message = plugin.getConfig().getString("Messages.leave");
		TextChannel Channel = jda.getTextChannelById(plugin.getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;

		String content = "**" + p + "** "+ message;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.leave"));
		Channel.sendMessage(embed.build()).queue();
	}


	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) { // discord stuurt bericht naar minecraft chat
		if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage())
			return;
		
		if (event.getChannel() != jda.getTextChannelById(plugin.getConfig().getString("Channel-id"))) return;

		String message = event.getMessage().getContentRaw();
		User user = event.getAuthor();
		int Color = event.getMember().getColorRaw();

		String hex = Integer.toHexString(Color);

		String username = "#" + hex + user.getName();

		Bukkit.broadcastMessage(Chatt.tools(username) + "#" + user.getDiscriminator() + ": §f" + message);

	}
}