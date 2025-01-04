package dev.comunidad.net.command.impl.media;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.command.CommandSender;

public class TeamSpeakCommand extends BaseCommand {

    private final Berlin plugin;

    public TeamSpeakCommand(Berlin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "ts", aliases = "teamspeak")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
            ChatUtil.sendMessage(sender, "&f&oJoin our teamspeak if you need help!");
            ChatUtil.sendMessage(sender, "&b&nts.cyruspvp.net");
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
        }
    }
}