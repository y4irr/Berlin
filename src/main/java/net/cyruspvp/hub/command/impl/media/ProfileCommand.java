package net.cyruspvp.hub.command.impl.media;

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Hub
 * @author Yair © 2024
 * Date: 13 - nov
 */

import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.profile.ProfileMenu;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.PlayerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ProfileCommand extends BaseCommand {

    private final Berlin plugin;

    @Override
    @Command(name = "profile", aliases = "profilemenu")
    public void onCommand(CommandArgs command) {
        Player player = (Player) command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            PlayerUtil.playSound(player, "NOTE_BASS");
            new ProfileMenu(plugin.getPlugin(), player).openMenu(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatUtil.translate("&cPlayer not found!"));
            return;
        }

        new ProfileMenu(plugin.getPlugin(), target).openMenu(player);
    }
}