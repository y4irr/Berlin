package net.cyruspvp.hub.model.custommessages;

import net.cyruspvp.hub.utilities.Berlin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class CustomMessagesListener implements Listener {
    private final Berlin plugin;

    public CustomMessagesListener(Berlin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        List<String> messages = plugin.getConfigFile().getStringList("welcome.message");
        if (messages.isEmpty()) {
            return;
        }

        for (String message : messages) {
            player.sendMessage((message.replace("%player%", player.getName())));
        }

        String title = plugin.getConfigFile().getString("Title.Line1");
        String subtitle = plugin.getConfigFile().getString("Title.Line2");

        title = title.replace("%player%", player.getName());
        subtitle = subtitle.replace("%player%", player.getName());

        player.sendTitle(title, subtitle);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}