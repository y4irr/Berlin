package net.cyruspvp.hub.tablist.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * <p>
 * This Project is property of Refine Development.<br>
 * Copyright © 2023, All Rights Reserved.<br>
 * Redistribution of this Project is not allowed.<br>
 * </p>
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 11/30/2023
 */

@UtilityClass
public class StringUtils {

    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final int MINOR_VERSION = Integer.parseInt(VERSION.split("_")[1]);
    private static final Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");


    // Thnx scifi, love you <3

    //TODO: Last colors in scoreboard team suffix not working properly
    public String[] split(String text) {
        if (text.length() <= 16) {
            return new String[] { text, "" };
        }

        String prefix = text.substring(0, 16);
        String suffix;

        if (prefix.charAt(15) == ChatColor.COLOR_CHAR || prefix.charAt(15) == '&') {
            prefix = prefix.substring(0, 15);
            suffix = text.substring(15);
        } else if (prefix.charAt(14) == ChatColor.COLOR_CHAR || prefix.charAt(14) == '&') {
            prefix = prefix.substring(0, 14);
            suffix = text.substring(14);
        } else {
            suffix = StringUtils.getLastColors(prefix) + text.substring(16);
        }

        return new String[] { prefix, suffix };
    }

    /**
     * Get last colors from a string in form of {@link Color}
     *
     * @param input {@link String} The string from which we extract color
     * @return      {@link Color}
     */
    public net.md_5.bungee.api.ChatColor getLastColors(String input) {
        String prefixColor = ChatColor.getLastColors(color(input));

        if (prefixColor.isEmpty()) return null;

        net.md_5.bungee.api.ChatColor color;

        // Hex Color Support

        // Obviously in older versions, hex color does not exist, so we just parse it normally
        ChatColor bukkitColor = ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
        if (bukkitColor == null) {
            return null;
        }
        color = bukkitColor.asBungee();

        return color;
    }


    /**
     * Translate '&' based color codes into bukkit ones
     *
     * @param text {@link String} Input Text
     * @return     {@link String} Output Text (with HexColor Support)
     */
    public String color(String text) {
        if (text == null) return "";

        text = ChatColor.translateAlternateColorCodes('&', text);
        return text;
    }
}
