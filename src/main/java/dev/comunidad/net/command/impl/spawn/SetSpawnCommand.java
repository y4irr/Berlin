package dev.comunidad.net.command.impl.spawn;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.spawn.SpawnManager;
import dev.comunidad.net.utilities.file.FileConfig;
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
