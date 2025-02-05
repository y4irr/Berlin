package net.cyruspvp.hub.command.impl;

import net.cyruspvp.hub.BerlinPlugin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.utilities.Berlin;
import org.bukkit.command.CommandSender;

public class BerlinCommand extends BaseCommand {

    private final Berlin plugin;

    public BerlinCommand(Berlin plugin) {
        this.plugin = plugin;
    }

    @Command(
            name = "Berlin",
            permission = "",
            inGameOnly = false
    )
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&7&m----------------------------");
            ChatUtil.sendMessage(sender, "&d&lBerlin Hub Plugin &7" + BerlinPlugin.getPlugin().getDescription().getVersion());
            ChatUtil.sendMessage(sender, "");
            ChatUtil.sendMessage(sender, "&7&oThis plugin was made by " + BerlinPlugin.getPlugin().getDescription().getAuthors());
            ChatUtil.sendMessage(sender, "&7&oOriginally forked Comet");
            ChatUtil.sendMessage(sender, "&7&m----------------------------");
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                this.plugin.onReload();
                ChatUtil.sendMessage(sender, "&aHubCore has been reloaded.");
            }

        }
    }
}
