package dev.comunidad.net.listeners;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatManager implements Listener {

    private final Berlin plugin;

    public ChatManager(Berlin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        boolean customChatEnabled = plugin.getConfigFile().getBoolean("custom-chat.enabled");
        if (customChatEnabled) {
            String format = plugin.getConfigFile().getString("custom-chat.format");
            format = format.replace("%player%", player.getName());
            format = format.replace("%message%", message);

            String prefix = plugin.getRankManager().getRank().getPrefix(player.getUniqueId());
            format = format.replace("%rank%", prefix != null ? prefix : "&a");

            event.setFormat(ChatUtil.translate(format));
        } else {
            event.setFormat(ChatUtil.translate("%1$s: %2$s"));
        }
    }

}
