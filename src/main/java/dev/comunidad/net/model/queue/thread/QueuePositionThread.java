package dev.comunidad.net.model.queue.thread;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.queue.Queue;
import dev.comunidad.net.model.queue.QueueManager;
import org.bukkit.entity.Player;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueuePositionThread extends Thread {

    private final Berlin plugin;
    private final QueueManager queueManager;

    public QueuePositionThread(Berlin plugin) {
        this.plugin = plugin;
        this.queueManager = plugin.getQueueManager();
    }

    @Override
    public void run() {
        while (true) {
            for (Queue queue : queueManager.getQueues().values()) {
                if (!queue.isPaused() && !queue.getPlayers().isEmpty()) {
                    Player player = queue.getPlayers().element().getPlayer();

                    if (player != null && player.isOnline()) queueManager.sendToServer(player, queue);
                }
            }

            try {
                Thread.sleep(1000L * plugin.getQueueFile().getLong("queue.position-thread"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
