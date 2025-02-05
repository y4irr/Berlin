package net.cyruspvp.hub;

import net.cyruspvp.hub.utilities.Berlin;
import org.bukkit.plugin.java.JavaPlugin;

public class BerlinPlugin extends JavaPlugin {

    private static Berlin instance;

    @Override
    public void onLoad() {
        instance = new Berlin(this);
        instance.onLoad();
    }

    @Override
    public void onEnable() {
        instance.onEnable();
    }

    @Override
    public void onDisable() {
        instance.onDisable();
    }

    public static Berlin get() {
        return instance;
    }

    public static BerlinPlugin getPlugin() {
        return BerlinPlugin.getPlugin(BerlinPlugin.class);
    }
}