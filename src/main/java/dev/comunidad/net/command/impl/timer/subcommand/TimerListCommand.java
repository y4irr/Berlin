package dev.comunidad.net.command.impl.timer.subcommand;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.timer.Timer;
import dev.comunidad.net.model.timer.TimerManager;
import org.bukkit.command.CommandSender;

public class TimerListCommand extends BaseCommand {

    private final TimerManager timerManager;

    public TimerListCommand(Berlin plugin) {
        this.timerManager = plugin.getTimerManager();
    }

    @Command(name = "timer.list", aliases = {"customt.list", "ctimer.list", "ct.list"}, permission = "berlin.command.timer.list", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        ChatUtil.sendMessage(sender, "&7&m------------------------------------");
        ChatUtil.sendMessage(sender, "&d&lActive Timers");
        ChatUtil.sendMessage(sender, "");

        if (timerManager.getTimers().isEmpty()) {
            ChatUtil.sendMessage(sender, "&cNo active Timers.");
        }
        else {
            for (Timer timer : timerManager.getTimers().values()) {
                ChatUtil.sendMessage(sender, " &e&l" + timer.getName() + "&7- &eTimer");
            }
        }

        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
    }
}
