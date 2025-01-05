package dev.comunidad.net.model.spawn;

import dev.comunidad.net.utilities.BukkitUtil;
import dev.comunidad.net.utilities.Berlin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 14-12-2022
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter
public class SpawnManager {

    private final Berlin plugin;
    private Location location;

    public SpawnManager(Berlin plugin) {
        this.plugin = plugin;
        this.location = BukkitUtil.deserializeLocation(plugin.getConfigFile().getString("spawn-location"));
    }

    public void setLocation(Location location) {
        this.location = location;
        plugin.getConfigFile().getConfiguration().set("spawn-location", BukkitUtil.serializeLocation(location));
        plugin.getConfigFile().save();
    }

    public void toSpawn(Player player) {
        player.teleport(location == null ? player.getWorld().getSpawnLocation() : location);
    }

    public void toSpawn(Entity entity) {
        entity.teleport(location == null ? entity.getWorld().getSpawnLocation() : location);
    }
}
