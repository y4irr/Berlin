package dev.comunidad.net.command.impl.timer;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;

public class TimerCommand extends BaseCommand {

    @Command(name = "timer", aliases = {"customt", "ctimer", "ct"}, permission = "berlin.command.timer", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        String label = command.getLabel();

        ChatUtil.sendMessage(command.getSender(), new String[]{
                ChatUtil.NORMAL_LINE,
                "&d&lTimer Commands",
                "",
                " &8● &d/" + label + " start <timer> <time> <prefix> &7- &fStart a timer.",
                " &8● &d/" + label + " stop <timer> &7- &fStop a timer.",
                " &8● &d/" + label + " list &7- &fSee all timers.",
                ChatUtil.NORMAL_LINE
        });
    }
}
