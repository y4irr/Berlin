package net.cyruspvp.hub.utilities;

import org.bukkit.Bukkit;

public class Logger {

    public static final String LINE_CONSOLE = ChatUtil.translate("&7&m---------------------------------------");

    public static void print(String... message) {
        for (String s : message) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatUtil.translate(s));
        }
    }

    private static String convert(String string) {
        return string.equalsIgnoreCase("enabled") ? "Loaded" : "Saved";
    }
}