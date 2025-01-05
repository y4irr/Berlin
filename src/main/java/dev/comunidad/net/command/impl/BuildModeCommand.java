package dev.comunidad.net.command.impl;

import dev.comunidad.net.BerlinPlugin;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.utilities.file.FileConfig;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BuildModeCommand extends BaseCommand {

    private final Berlin plugin;
    private final FileConfig languageFile;

    public BuildModeCommand(Berlin plugin) {
        this.plugin = plugin;
        this.languageFile = plugin.getLanguageFile();
    }

    @Command(name = "buildmode", aliases = {"build", "bm"}, permission = "berlin.command.buildmode")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.hasMetadata("Berlin-build-mode")) {
            player.removeMetadata("Berlin-build-mode", BerlinPlugin.getPlugin());
            ChatUtil.sendMessage(player, languageFile.getString("build-mode-message.disabled"));
        }
        else {
            player.setMetadata("Berlin-build-mode", new FixedMetadataValue(BerlinPlugin.getPlugin(), true));
            ChatUtil.sendMessage(player, languageFile.getString("build-mode-message.enabled"));
        }
    }
}

