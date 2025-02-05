package net.cyruspvp.hub.command.impl.spawn;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.command.BaseCommand;
import net.cyruspvp.hub.command.Command;
import net.cyruspvp.hub.command.CommandArgs;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.spawn.SpawnManager;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends BaseCommand {

	private final FileConfig languageFile;
	private final SpawnManager spawnManager;

	public SetSpawnCommand(Berlin plugin) {
		this.languageFile = plugin.getLanguageFile();
		this.spawnManager = plugin.getSpawnManager();
	}

	@Command(name = "setspawn", permission = "berlin.command.setspawn")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		spawnManager.setLocation(player.getLocation());
		ChatUtil.sendMessage(player, languageFile.getString("spawn-message.set"));
	}
}
