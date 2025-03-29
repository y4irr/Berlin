package net.cyruspvp.hub.command.impl;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.users.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
            ChatUtil.sendMessage(sender, "&d&lBerlin Hub Plugin &7" + Berlin.getPlugin().getDescription().getVersion());
            ChatUtil.sendMessage(sender, "");
            ChatUtil.sendMessage(sender, "&7&oThis plugin was made by " + Berlin.getPlugin().getDescription().getAuthors());
            ChatUtil.sendMessage(sender, "&7&oOriginally forked Comet");
            ChatUtil.sendMessage(sender, "&7&m----------------------------");
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                this.plugin.onReload();
                ChatUtil.sendMessage(sender, "&aHubCore has been reloaded.");
            }

            if (args[0].equalsIgnoreCase("deleteusers")) {
                List<User> toDelete = new ArrayList<>(Berlin.getPlugin().getUserManager2().getUsers().values());
                for (User user : toDelete) {
                    user.delete();
                }

                Berlin.getPlugin().getUserManager2().getUsers().clear();
                Berlin.getPlugin().getUserManager2().getUuidCache().clear();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    User user = new User(Berlin.getPlugin().getUserManager2(), player.getUniqueId(), player.getName());
                    user.save();
                }

                ChatUtil.sendMessage(sender, "&fYou have deleted &d" + toDelete.size() + " &fusers.");
            }

        }
    }
}
