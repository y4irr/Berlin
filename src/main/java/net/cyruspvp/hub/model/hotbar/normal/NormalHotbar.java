package net.cyruspvp.hub.model.hotbar.normal;

import net.cyruspvp.hub.utilities.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 14-12-2022
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter @Setter
public abstract class NormalHotbar {

    private final String name;
    private boolean enabled;
    private ItemStack item;
    private int itemSlot;
    private String skullOwner;

    protected NormalHotbar(String name) {
        this.name = name;
    }

    public boolean isSimilar(ItemStack toCheck) {
        return (toCheck != null)
                && (toCheck.getType() != Material.AIR)
                && (toCheck.hasItemMeta())
                && (toCheck.getItemMeta().getDisplayName() != null)
                && toCheck.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName());
    }

    public ItemStack getItem(Player player) {
        return new ItemBuilder(item)
                .setSkullOwner(skullOwner.replace("%player%", player.getName()))
                .build();
    }

    public abstract void onItemInteract(Player player);
}
