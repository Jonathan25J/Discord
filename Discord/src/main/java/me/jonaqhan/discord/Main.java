package me.jonaqhan.discord;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.jonaqhan.discord.livechat.Chat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
public class Main extends JavaPlugin {

	static JDA jda;

	public void onEnable() {
		saveDefaultConfig();
		new Chat(this);
		TextChannel Channel = Chat.jda.getTextChannelById(getConfig().getString("Channel-id"));
		if ((TextChannel) Channel == null)
			return;

		String text = getConfig().getString("Messages.server_on");
		String content = ":white_check_mark: " + text;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(content);
		embed.setColor(getConfig().getInt("Colors.server_on"));
		Channel.sendMessage(embed.build()).queue();

	}

	public void onDisable() {
		 HandlerList.unregisterAll(this);

			TextChannel Channel = Chat.jda.getTextChannelById(getConfig().getString("Channel-id"));
			if ((TextChannel) Channel == null)
				return;

			String text = getConfig().getString("Messages.server_off");
			String content = ":x: " + text;
			EmbedBuilder embed = new EmbedBuilder();
			embed.setDescription(content);
			embed.setColor(getConfig().getInt("Colors.server_off"));
			
			Channel.sendMessage(embed.build()).complete();
			
			
			try {
		    Chat.jda.cancelRequests();
			Chat.jda.shutdownNow();
			} catch(Exception e) {}

		

	}

}
