package net.cyruspvp.hub.model.hotbar.normal;

import net.cyruspvp.hub.model.hotbar.normal.types.*;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.file.FileConfig;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NormalHotbarManager {

    private final Berlin plugin;
    private final Map<String, NormalHotbar> hotbarMap;
    private final Map<ItemStack, NormalHotbar> hotbarCache = new HashMap<>();

    public NormalHotbarManager(Berlin plugin) {
        this.plugin = plugin;
        this.hotbarMap = new LinkedHashMap<>();
        this.loadOrRefresh(false);
    }

    public Collection<NormalHotbar> getHotbars() {
        return hotbarMap.values();
    }

    public NormalHotbar getHotbar(String name) {
        return hotbarMap.get(name);
    }

    public NormalHotbar getHotbar(ItemStack item) {
        if (hotbarCache.containsKey(item))
            return hotbarCache.get(item);

        for (NormalHotbar hotbar : getHotbars()) {
            if (hotbar.isSimilar(item))
                return hotbarCache.computeIfAbsent(item, k -> hotbar);
        }
        return null;
    }

    public NormalHotbar registerHotbarFromConfig(NormalHotbar hotbar, ConfigurationSection section) {
        hotbar.setEnabled(section.getBoolean(hotbar.getName() + ".enabled"));
        hotbar.setItem(new ItemBuilder(section.getString(hotbar.getName() + ".item.material"))
                .setName(section.getString(hotbar.getName() + ".item.name"))
                .setLore(section.getStringList(hotbar.getName() + ".item.description"))
                .setData(section.getInt(hotbar.getName() + ".item.data"))
                .setEnchanted(section.getBoolean(hotbar.getName() + ".item.enchanted"))
                .build());
        hotbar.setItemSlot(section.getInt(hotbar.getName() + ".item.slot"));
        hotbar.setSkullOwner(section.getString(hotbar.getName() + ".item.head"));
        return hotbar;
    }

    public void registerHotbars(NormalHotbar... hotbars) {
        for (NormalHotbar hotbar : hotbars) {
            hotbarMap.put(hotbar.getName(), hotbar);
        }
    }

    public void giveHotbar(Player player) {
        player.getInventory().clear();

        for (NormalHotbar hotbar : getHotbars()) {
            if (hotbar.isEnabled()) player.getInventory().setItem(hotbar.getItemSlot(), hotbar.getItem(player));
        }
    }

    public void loadOrRefresh(boolean reload) {
        if (reload) hotbarMap.clear();

        FileConfig hotbarFile = plugin.getHotbarFile();
        ConfigurationSection section = hotbarFile.getConfiguration().getConfigurationSection("normal");

        registerHotbars(
                registerHotbarFromConfig(new EnderButtHotbar("ender-butt", section.getBoolean("ender-butt.mount")), section),
                registerHotbarFromConfig(new ServerSelectorHotbar(plugin, "server-selector"), section),
                registerHotbarFromConfig(new ShowPlayerHotbar("show-players", this), section),
                registerHotbarFromConfig(new HidePlayerHotbar("hide-players", this), section),
                registerHotbarFromConfig(new InformationHotbar("information", this), section),
                registerHotbarFromConfig(new ProfileHotbar(plugin, "profile-beta"), section)
        );
        if (reload) {
            Bukkit.getOnlinePlayers().forEach(this::giveHotbar);
        }
    }
}
