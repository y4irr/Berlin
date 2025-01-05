package dev.comunidad.net.command.impl;

import dev.comunidad.net.BerlinPlugin;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
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
