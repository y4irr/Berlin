package dev.comunidad.net.model.ban;

import dev.comunidad.net.model.ban.impl.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@Getter
public class BanManager {

    private final IBan ban;

    public BanManager() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("Volcano") != null) {
            this.ban = new VolcanoBan();
        } else if (pluginManager.getPlugin("Aquacore") != null) {
            this.ban = new AquacoreBan();
        } else {
            this.ban = new DefaultBan();
        }
    }
}

