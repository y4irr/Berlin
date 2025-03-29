package net.cyruspvp.hub.integrations;

import net.cyruspvp.hub.Berlin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class PlaceholderAPIHook {

    @Getter
    private boolean enabled;

    public void initialize(Berlin plugin) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIExpansion papi = new PlaceholderAPIExpansion(plugin);
            if (!papi.isRegistered()) papi.register();
            enabled = true;
        }
    }
}
