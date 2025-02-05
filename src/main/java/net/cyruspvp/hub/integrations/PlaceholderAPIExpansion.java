package net.cyruspvp.hub.integrations;

import net.cyruspvp.hub.BerlinPlugin;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.queue.Queue;
import net.cyruspvp.hub.model.queue.QueueManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final Berlin plugin;
    private final QueueManager queueManager;

    public PlaceholderAPIExpansion(Berlin plugin) {
        this.plugin = plugin;
        this.queueManager = plugin.getQueueManager();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return BerlinPlugin.getPlugin().getDescription().getAuthors().toString()
                .replace("[", "").replace("]", "");
    }

    @Override
    public @NotNull String getIdentifier() {
        return "Berlin";
    }

    @Override
    public @NotNull String getVersion() {
        return BerlinPlugin.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        for (Queue queue : queueManager.getQueues().values()) {
            if (identifier.equalsIgnoreCase("queue_" + queue.getServer() + "_name")) {
                return queue.getServer();
            }
            if (identifier.equalsIgnoreCase("queue_" + queue.getServer() + "_size")) {
                return String.valueOf(queue.getPlayers().size());
            }
        }

        if (identifier.equals("queue_player_name")) {
            Queue queue = queueManager.getQueueByPlayer(player);
            return queue == null ? "None" : queue.getServer();
        }
        if (identifier.equals("queue_player_position")) {
            Queue queue = queueManager.getQueueByPlayer(player);
            return queue == null ? "0" : String.valueOf(queueManager.getQueuePlayerPosition(queue, player.getUniqueId()));
        }
        if (identifier.equals("queue_player_size")) {
            Queue queue = queueManager.getQueueByPlayer(player);
            return queue == null ? "0" : String.valueOf(queue.getPlayers().size());
        }
        return null;
    }
}
