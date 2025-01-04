package dev.comunidad.net.command.impl.queue.subcommands;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.queue.Queue;
import dev.comunidad.net.model.queue.QueueManager;
import org.bukkit.command.CommandSender;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueueListCommand extends BaseCommand {

    private final QueueManager queueManager;

    public QueueListCommand(Berlin plugin) {
        this.queueManager = plugin.getQueueManager();
    }

    @Command(name = "queue.list", permission = "berlin.command.queue.list", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        ChatUtil.sendMessage(sender, "&7&m--------->&r &6&lQueue List &7&m<---------");

        if (queueManager.getQueues().isEmpty()) {
            ChatUtil.sendMessage(sender, "&cThere are no queues.");
        }
        else {
            for (Queue queue : queueManager.getQueues().values()) {
                ChatUtil.sendMessage(sender, " &7▶ &e" + queue.getServer() + " Queue");
            }
        }

        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
    }
}
