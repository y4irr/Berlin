package dev.comunidad.net.command.impl.media;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.command.CommandSender;

public class StoreCommand extends BaseCommand {

    private final Berlin plugin;

    public StoreCommand(Berlin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "store")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
            ChatUtil.sendMessage(sender, "&f&oCheck out our store and buy some perks!");
            ChatUtil.sendMessage(sender, "&a&nstore.cyruspvp.net");
            ChatUtil.sendMessage(sender, "&7&m                                                       ");
        }
    }
}