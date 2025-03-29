package net.cyruspvp.hub.utilities.extra;

import lombok.Getter;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.ReflectionUtils;
import net.cyruspvp.hub.utilities.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


@Getter
public abstract class Manager extends Configs {

    private static Method SET_DATA;
    private final Berlin instance;

    public Manager(Berlin instance) {
        this.instance = instance;
        this.instance.getManagers().add(this);
    }

    public void registerListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public void setData(ItemStack item, int data) {
        if (Utils.isModernVer()) {
            if (item.getItemMeta() == null) return;

            Damageable damageable = (Damageable) item.getItemMeta();
            damageable.setDamage(data);
            item.setItemMeta((ItemMeta) damageable);

        } else {
            item.setDurability((short) data);
        }
    }

    public void setData(Block block, int data) {
        if (!Utils.isModernVer()) {
            if (SET_DATA == null) {
                SET_DATA = ReflectionUtils.accessMethod(Block.class, "setData", byte.class);
            }

            try {

                SET_DATA.invoke(block, (byte) data);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getData(ItemStack item) {
        if (Utils.isModernVer()) {
            if (item.getItemMeta() == null) return 0;

            Damageable damageable = (Damageable) item.getItemMeta();
            return damageable.getDamage();

        } else {
            return item.getDurability();
        }
    }

    public void enable() {
    }

    public void disable() {
    }

    public void reload() {
    }
}