package net.cyruspvp.hub.model.rank;

import lombok.Getter;
import net.cyruspvp.hub.model.rank.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@Getter
public class RankManager {

    private final IRank rank;

    public RankManager() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("AquaCore") != null) {
            this.rank = new AquaCore();
        } else if (pluginManager.getPlugin("LuckPerms") != null) {
            this.rank = new LuckPerms();
        } else if (pluginManager.getPlugin("Helium") != null) {
            this.rank = new Helium();
        } else if (pluginManager.getPlugin("Volcano") != null) {
            this.rank = new Volcano();
        } else if (pluginManager.getPlugin("Kup") != null) {
            this.rank = new Kup();
        } else {
            this.rank = new Default();
        }
    }
}