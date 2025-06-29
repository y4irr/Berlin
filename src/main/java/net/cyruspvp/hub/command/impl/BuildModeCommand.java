package net.cyruspvp.hub.command.impl;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BuildModeCommand extends BaseCommand {

    private final Berlin instance;
    private final FileConfig languageFile;

    public BuildModeCommand(Berlin plugin) {
        this.instance = plugin;
        this.languageFile = plugin.getLanguageFile();
    }

    @Command(name = "buildmode", aliases = {"build", "bm"}, permission = "berlin.command.buildmode")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.hasMetadata("Berlin-build-mode")) {
            player.removeMetadata("Berlin-build-mode", Berlin.getPlugin());
            ChatUtil.sendMessage(player, languageFile.getString("build-mode-message.disabled"));
        }
        else {
            player.setMetadata("Berlin-build-mode", new FixedMetadataValue(Berlin.getPlugin(), true));
            ChatUtil.sendMessage(player, languageFile.getString("build-mode-message.enabled"));
        }
    }
}

