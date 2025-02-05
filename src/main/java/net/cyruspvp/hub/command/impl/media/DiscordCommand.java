package net.cyruspvp.hub.command.impl.media;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.utilities.Berlin;
import org.bukkit.command.CommandSender;

public class DiscordCommand extends BaseCommand {

    private final Berlin plugin;

    public DiscordCommand(Berlin plugin) {
        this.plugin = plugin;
    }

    @Command(
            name = "discord",
            aliases = "dc" + "community",
            permission = "",
            inGameOnly = false
    )
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
            ChatUtil.sendMessage(sender, "&f&oJoin our discord if you need help!");
            ChatUtil.sendMessage(sender, "&9&ndiscord.cyruspvp.net");
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
        } else {
            if (args.length == 1) {
                ChatUtil.sendMessage(sender, "&7&m                                                       ");
                ChatUtil.sendMessage(sender, "&f&oJoin our discord if you need help!");
                ChatUtil.sendMessage(sender, "&9&ndiscord.cyruspvp.net");
                ChatUtil.sendMessage(sender, "&7&m                                                       ");
            }
        }
    }
}