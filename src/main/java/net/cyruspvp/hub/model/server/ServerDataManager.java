package net.cyruspvp.hub.model.server;

import net.cyruspvp.hub.model.server.impl.AquaCoreServerData;
import net.cyruspvp.hub.model.server.impl.DefaultServerData;
import net.cyruspvp.hub.model.server.impl.HeliumServerData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@Getter
public class ServerDataManager {

    private final IServerData serverData;

    public ServerDataManager() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("AquaCore") != null) {
            this.serverData = new AquaCoreServerData();
        }
        else if (pluginManager.getPlugin("Helium") != null) {
            this.serverData = new HeliumServerData();
        }
        else {
            this.serverData = new DefaultServerData();
        }
    }
}
