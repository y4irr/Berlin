package net.cyruspvp.hub.placeholder.type;

import me.clip.placeholderapi.PlaceholderAPI;
import net.cyruspvp.hub.placeholder.Placeholder;
import net.cyruspvp.hub.placeholder.PlaceholderHook;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;


public class PlaceholderAPIHook extends Module<PlaceholderHook> implements Placeholder {
    public PlaceholderAPIHook(PlaceholderHook manager) {
        super(manager);
        Bukkit.getPluginManager().registerEvents(this, getInstance()); // Registra el listener
    }

    @Override
    public String replace(Player player, String string) {
        if (player == null || string == null) return "";
        return PlaceholderAPI.setPlaceholders(player, string);
    }

    @Override
    public List<String> replace(Player player, List<String> list) {
        if (player == null || list == null) return Collections.emptyList();
        return PlaceholderAPI.setPlaceholders(player, list);
    }

}