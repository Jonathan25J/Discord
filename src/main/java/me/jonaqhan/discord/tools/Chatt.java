package me.jonaqhan.discord.tools;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class Chatt {

	public static String tools(String s) {

		final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

		Matcher match = pattern.matcher(s);

		while (match.find()) {

			String color = s.substring(match.start(), match.end());
			s = s.replace(color, ChatColor.of(color) + "");

			match = pattern.matcher(s);

		}

		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
