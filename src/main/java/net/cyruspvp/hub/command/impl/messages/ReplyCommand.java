package net.cyruspvp.hub.command.impl.messages;

import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplyCommand extends BaseCommand {

    private final Berlin plugin;
    public static Map<UUID, UUID> lastMessaged = new HashMap<>();

    public ReplyCommand(Berlin plugin) {
        this.plugin = plugin;

    }
    @Command(
            name = "reply",
            aliases = {"r"}
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (!lastMessaged.containsKey(sender.getUniqueId())) {
            ChatUtil.sendMessage(sender, "&cYou have no one to reply to.");
            return;
        }

        if (args.length < 1) {
            ChatUtil.sendMessage(sender, "&cUsage: /reply <message>");
            return;
        }

        UUID targetUUID = lastMessaged.get(sender.getUniqueId());
        Player target = Bukkit.getPlayer(targetUUID);

        if (target == null || !target.isOnline()) {
            ChatUtil.sendMessage(sender, "&cThe player you are trying to reply to is not online.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        String prefixSender = plugin.getRankManager().getRank().getPrefix(sender.getUniqueId());
        String prefixTarget = plugin.getRankManager().getRank().getPrefix(target.getUniqueId());

        String formattedMessageToTarget = ChatUtil.translate("&7(From " + prefixSender + sender.getName() + "&7): " + message.toString().trim());
        String formattedMessageToSender = ChatUtil.translate("&7(To " + prefixTarget + target.getName() + "&7): " + message.toString().trim());

        sender.sendMessage(formattedMessageToSender);
        target.sendMessage(formattedMessageToTarget);

        lastMessaged.put(sender.getUniqueId(), targetUUID);
        lastMessaged.put(targetUUID, sender.getUniqueId());
    }
}