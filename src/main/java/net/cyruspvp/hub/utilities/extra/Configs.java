package net.cyruspvp.hub.utilities.extra;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.file.Config;
import net.cyruspvp.hub.utilities.file.ConfigYML;


public class Configs {

    private static ConfigYML LUNAR_CONFIG;
    private static ConfigYML TABLIST_CONFIG;

    public void load(Berlin instance) {
        LUNAR_CONFIG = new ConfigYML(instance, "lunar");
        TABLIST_CONFIG = new ConfigYML(instance, "tablist");

//        new ItemUtils(this);
        Config.load(this, false);
    }

    public ConfigYML getTablistConfig() {
        return TABLIST_CONFIG;
    }

    public ConfigYML getLunarConfig() {
        return LUNAR_CONFIG;
    }
}