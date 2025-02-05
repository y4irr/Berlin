package net.cyruspvp.hub.command.impl.timer.subcommand;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.timer.Timer;
import net.cyruspvp.hub.model.timer.TimerManager;
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
