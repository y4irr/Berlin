package net.cyruspvp.hub.listeners;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.queue.Queue;
import net.cyruspvp.hub.model.queue.QueueManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueueListener implements Listener {

    private final QueueManager queueManager;

    public QueueListener(Berlin plugin) {
        this.queueManager = plugin.getQueueManager();
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Queue queue = queueManager.getQueueByPlayer(player);
        if (queue != null) queueManager.removeQueuePlayer(queue, player.getUniqueId());
    }
}
