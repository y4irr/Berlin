package dev.comunidad.net.command.impl.queue;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class QueueCommand extends BaseCommand {

    @Command(name = "queue", permission = "berlin.command.queue", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        ChatUtil.sendMessage(command.getSender(), new String[]{
                ChatUtil.NORMAL_LINE,
                "&6&lQueue Commands",
                "",
                " &f<> &7= &fRequired &7| &f[] &7= &fOptional",
                "",
                " &7▶ &e/queue join <queue> &7- &fJoin a queue.",
                " &7▶ &e/queue leave &7- &fLeave a queue.",
                " &7▶ &e/queue pause <queue> &7- &fPause a queue.",
                " &7▶ &e/queue list &7- &fSee all queues.",
                ChatUtil.NORMAL_LINE
        });
    }
}
