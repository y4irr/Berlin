package dev.comunidad.net.command.impl.queue.subcommands;

import dev.comunidad.net.database.redis.RedisManager;
import dev.comunidad.net.database.redis.RedisMessage;
import dev.comunidad.net.database.redis.RedisPayload;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.queue.Queue;
import dev.comunidad.net.model.queue.QueueManager;
import dev.comunidad.net.utilities.file.FileConfig;
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
