package dev.comunidad.net.command.impl.timer.subcommand;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.timer.Timer;
import dev.comunidad.net.model.timer.TimerManager;
import dev.comunidad.net.utilities.file.FileConfig;
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
