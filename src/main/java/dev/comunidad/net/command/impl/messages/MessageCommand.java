package dev.comunidad.net.command.impl.messages;

import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageCommand extends BaseCommand {

    private final Berlin plugin;

    public MessageCommand(Berlin plugin) {
        this.plugin = plugin;

    }
    @Command(
            name = "msg",
            aliases = {"message", "pm", "whisper"}
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            ChatUtil.sendMessage(sender, "&cUsage: /msg <player> <message>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            ChatUtil.sendMessage(sender, "&cThe player " + args[0] + " is not online.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        String prefixSender = plugin.getRankManager().getRank().getPrefix(sender.getUniqueId());
        String prefixTarget = plugin.getRankManager().getRank().getPrefix(target.getUniqueId());

        String formattedMessageToTarget = ChatUtil.translate("&7(From " + prefixSender + sender.getName() + "&7): " + message.toString().trim());
        String formattedMessageToSender = ChatUtil.translate("&7(To " + prefixTarget + target.getName() + "&7): " + message.toString().trim());

        sender.sendMessage(formattedMessageToSender);
        target.sendMessage(formattedMessageToTarget);

        ReplyCommand.lastMessaged.put(sender.getUniqueId(), target.getUniqueId());
        ReplyCommand.lastMessaged.put(target.getUniqueId(), sender.getUniqueId());
        }
}