package me.jonaqhan.discord.livechat;

import java.util.Collection;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jonaqhan.discord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Chat extends ListenerAdapter implements Listener, AudioManager {
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

	@SuppressWarnings("static-access")
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

		String content = "**" + p + "** " + message;
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

		String content = "**" + p + "** " + message;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.leave"));
		Channel.sendMessage(embed.build()).queue();
	}

	@EventHandler
	public void Death(PlayerDeathEvent e) { 

		String p = e.getEntity().getName();
		String message = e.getDeathMessage().replace("§e", "").replace(p, "**" + p + "**");
		TextChannel Channel = jda.getTextChannelById(plugin.getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;
		
		String content = ":skull_crossbones: " + message;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.death"));
		Channel.sendMessage(embed.build()).queue();
		
		
	}
	
	@EventHandler
	public void Advancement(PlayerAdvancementDoneEvent e) { 

		String p = "**" +e.getPlayer().getName() + "**";
		String message = plugin.getConfig().getString("Messages.advancement");
		String[] advancement = e.getAdvancement().getKey().toString().replace("_", " ").split("/");
		
		TextChannel Channel = jda.getTextChannelById(plugin.getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;
	
		String content = ":tada: " + p + " " + message + "** " + advancement[1] + "**";
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(plugin.getConfig().getInt("Colors.advancement"));
		Channel.sendMessage(embed.build()).queue();
		
		
		
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) { // discord stuurt bericht naar minecraft chat
	if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage())
			return;

		if (event.getChannel() != jda.getTextChannelById(plugin.getConfig().getString("Channel-id")))
			return;

		String message = event.getMessage().getContentRaw();
		User user = event.getAuthor();
		int Color = event.getMember().getColorRaw();

		String hex = Integer.toHexString(Color);

		String username = "#" + hex + user.getName();

		try {

			Bukkit.broadcastMessage(CColor.tools(username) + "#" + user.getDiscriminator() + ": §f" + message);
		} catch (Exception e) {
		}

	}

	public void openAudioConnection(VoiceChannel channel) {

		
	}

	public void closeAudioConnection() {
		
	}

	public void setSpeakingMode(Collection<SpeakingMode> mode) {
		
	}

	public EnumSet<SpeakingMode> getSpeakingMode() {
		return null;
	}

	public void setSpeakingDelay(int millis) {
		
	}

	public JDA getJDA() {
		return null;
	}

	public Guild getGuild() {
		return null;
	}

	public boolean isAttemptingToConnect() {
		return false;
	}

	public VoiceChannel getQueuedAudioConnection() {
		return null;
	}

	public VoiceChannel getConnectedChannel() {
		return null;
	}

	public boolean isConnected() {
		return false;
	}

	public void setConnectTimeout(long timeout) {
		
	}

	public long getConnectTimeout() {
		return 0;
	}

	public void setSendingHandler(AudioSendHandler handler) {
		
	}

	public AudioSendHandler getSendingHandler() {
		return null;
	}

	public void setReceivingHandler(AudioReceiveHandler handler) {
		
	}

	public AudioReceiveHandler getReceivingHandler() {
		return null;
	}

	public void setConnectionListener(ConnectionListener listener) {
		
	}

	public ConnectionListener getConnectionListener() {
		return null;
	}

	public ConnectionStatus getConnectionStatus() {
		return null;
	}

	public void setAutoReconnect(boolean shouldReconnect) {
		
	}

	public boolean isAutoReconnect() {
		return false;
	}

	public void setSelfMuted(boolean muted) {
		
	}

	public boolean isSelfMuted() {
		return false;
	}

	public void setSelfDeafened(boolean deafened) {
	}

	public boolean isSelfDeafened() {
		return false;
	}
}