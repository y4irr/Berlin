package dev.comunidad.net;

import dev.comunidad.net.utilities.Berlin;
import org.bukkit.plugin.java.JavaPlugin;

public class BerlinPlugin extends JavaPlugin {

    private static dev.comunidad.net.utilities.Berlin instance;

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