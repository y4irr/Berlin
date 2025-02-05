package net.cyruspvp.hub.model.ban;

import lombok.Getter;
import net.cyruspvp.hub.model.ban.impl.AquacoreBan;
import net.cyruspvp.hub.model.ban.impl.DefaultBan;
import net.cyruspvp.hub.model.ban.impl.VolcanoBan;
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

