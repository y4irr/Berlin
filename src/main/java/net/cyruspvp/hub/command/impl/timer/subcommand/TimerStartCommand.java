package net.cyruspvp.hub.command.impl.timer.subcommand;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.JavaUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.timer.Timer;
import net.cyruspvp.hub.model.timer.TimerManager;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class TimerStartCommand extends BaseCommand {

    private final Berlin plugin;
    private final FileConfig languageFile;
    private final TimerManager timerManager;

    public TimerStartCommand(Berlin plugin) {
        this.plugin = plugin;
        this.languageFile = plugin.getLanguageFile();
        this.timerManager = plugin.getTimerManager();
    }

    @Command(name = "timer.start", aliases = {"customt.start", "ctimer.start", "ct.start"}, permission = "berlin.command.timer.start", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".start", "");

        if (args.length < 3) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " start <timer> <time> <prefix>");
            ChatUtil.sendMessage(sender, "&cExample: /" + label + " start Test 60m Test");
            return;
        }

        String timerName = args[0];

        if (timerManager.existsTimer(timerName)) {
            ChatUtil.sendMessage(sender, languageFile.getString("timer-message.exists")
                    .replace("<timer>", timerName));
            return;
        }

        long duration = JavaUtil.formatLong(args[1]);

        if (duration == -1L) {
            ChatUtil.sendMessage(sender, "&c" + args[1] + " is an invalid duration.");
            return;
        }

        if (duration < 1000L) {
            ChatUtil.sendMessage(sender, "&cTimer must last for at least 20 ticks.");
            return;
        }

        String prefix = StringUtils.join(args, ' ', 2, args.length);

        Timer timer = new Timer(plugin, timerName, System.currentTimeMillis(), System.currentTimeMillis() + duration, prefix);
        timerManager.createTimer(timer);
        ChatUtil.sendMessage(sender, languageFile.getString("timer-message.created")
                .replace("<timer>", timerName));
    }
}
