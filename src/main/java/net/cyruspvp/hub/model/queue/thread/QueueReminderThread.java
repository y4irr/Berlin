package net.cyruspvp.hub.model.queue.thread;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.queue.Queue;
import net.cyruspvp.hub.model.queue.QueueManager;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueueReminderThread extends Thread {

    private final Berlin plugin;
    private final FileConfig languageFile;
    private final QueueManager queueManager;

    public QueueReminderThread(Berlin plugin) {
        this.plugin = plugin;
        this.languageFile = plugin.getLanguageFile();
        this.queueManager = plugin.getQueueManager();
    }

    @Override
    public void run() {
        while (true) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Queue queue = queueManager.getQueueByPlayer(player);

                if (queue != null) {
                    for (String message : languageFile.getStringList("queue-message.position")) {
                        ChatUtil.sendMessage(player, message
                                .replace("<queue>", queue.getServer())
                                .replace("<queue-player-position>", String.valueOf(queueManager.getQueuePlayerPosition(queue, player.getUniqueId())))
                                .replace("<queue-player-size>", String.valueOf(queue.getPlayers().size())));
                    }
                }
            }

            try {
                Thread.sleep(1000L * plugin.getQueueFile().getLong("queue.position-message-thread"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
