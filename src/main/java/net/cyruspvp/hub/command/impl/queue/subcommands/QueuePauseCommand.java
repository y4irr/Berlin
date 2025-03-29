package net.cyruspvp.hub.command.impl.queue.subcommands;

import net.cyruspvp.hub.database.redis.RedisManager;
import net.cyruspvp.hub.database.redis.RedisMessage;
import net.cyruspvp.hub.database.redis.RedisPayload;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.queue.Queue;
import net.cyruspvp.hub.model.queue.QueueManager;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.command.CommandSender;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueuePauseCommand extends BaseCommand {

    private final FileConfig languageFile;
    private final RedisManager redisManager;
    private final QueueManager queueManager;

    public QueuePauseCommand(Berlin plugin) {
        this.languageFile = plugin.getLanguageFile();
        this.redisManager = plugin.getRedisManager();
        this.queueManager = plugin.getQueueManager();
    }

    @Command(name = "queue.pause", permission = "berlin.command.queue.pause", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&cUsage: /queue pause <queue>");
            return;
        }

        String queueName = args[0];

        if (!queueManager.exists(queueName)) {
            ChatUtil.sendMessage(sender, "&cQueue with name '" + queueName + "' does not exist.");
            return;
        }

        Queue queue = queueManager.getQueue(queueName);
        String json = new RedisMessage(RedisPayload.QUEUE_PAUSE_UPDATE)
                .setParam("queue-name", queueName)
                .setParam("queue-pause", String.valueOf(queue.isPaused()))
                .toJSON();
        redisManager.getRedis().write(json);

        if (queue.isPaused()) {
            ChatUtil.sendMessage(sender, languageFile.getString("queue-message.paused")
                    .replace("<queue>", queueName));
        }
        else {
            ChatUtil.sendMessage(sender, languageFile.getString("queue-message.un-paused")
                    .replace("<queue>", queueName));
        }
    }
}
