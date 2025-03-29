package net.cyruspvp.hub.command.impl.timer.subcommand;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.timer.Timer;
import net.cyruspvp.hub.model.timer.TimerManager;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.command.CommandSender;

public class TimerStopCommand extends BaseCommand {

    private final FileConfig languageFile;
    private final TimerManager timerManager;

    public TimerStopCommand(Berlin plugin) {
        this.languageFile = plugin.getLanguageFile();
        this.timerManager = plugin.getTimerManager();
    }

    @Command(name = "timer.stop", aliases = {"customt.stop", "ctimer.stop", "ct.stop"}, permission = "berlin.command.timer.stop", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".stop", "");

        if (args.length < 1) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " stop <timer>");
            ChatUtil.sendMessage(sender, "&cExample: /" + label + " stop Test");
            return;
        }

        String timerName = args[0];

        if (!timerManager.existsTimer(timerName)) {
            ChatUtil.sendMessage(sender, languageFile.getString("timer-message.not-active")
                    .replace("<timer>", timerName));
            return;
        }

        Timer timer = timerManager.getTimer(timerName);
        timer.setRunning(false);
        ChatUtil.sendMessage(sender, languageFile.getString("timer-message.stopped")
                .replace("<timer>", timerName));
    }
}
