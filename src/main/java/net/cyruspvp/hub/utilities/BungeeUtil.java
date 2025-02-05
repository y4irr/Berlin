package net.cyruspvp.hub.utilities;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.cyruspvp.hub.BerlinPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@UtilityClass
public class BungeeUtil {

    public void sendBungeeServer(Berlin plugin, Player player, String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ConnectOther");
            out.writeUTF(player.getName());
            out.writeUTF(server);

            player.sendPluginMessage(BerlinPlugin.getPlugin(), "BungeeCord", out.toByteArray());
        }
        catch (Exception exception) {
            ChatUtil.sendMessage(player, "&cAn error occurred while trying to send you to the server.");
        }
    }
}
