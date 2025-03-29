package net.cyruspvp.hub.utilities.file;

import net.cyruspvp.hub.tablist.extra.TablistSkin;
import net.cyruspvp.hub.utilities.extra.Configs;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    public static String SERVER_NOT_LOADED;
    public static String COULD_NOT_LOAD_DATA;
    public static boolean TABLIST_ENABLED;
    public static int TABLIST_PING;

    public static List<String> NAMETAG_BANNED;
    public static List<String> NAMETAG_NORMAL;
    public static List<String> TAB_SKIN_CACHE_LEFT;
    public static List<String> TAB_SKIN_CACHE_MIDDLE;
    public static List<String> TAB_SKIN_CACHE_RIGHT;
    public static List<String> TAB_SKIN_CACHE_FAR_RIGHT;


    public static void load(Configs configs, boolean reload) {
        TABLIST_PING = configs.getTablistConfig().getInt("TABLIST_INFO.PING");
        TABLIST_ENABLED = configs.getTablistConfig().getBoolean("TABLIST_INFO.ENABLED");
        NAMETAG_BANNED = configs.getLunarConfig().getStringList("NAMETAGS.FORMAT.BANNED");
        NAMETAG_NORMAL = configs.getLunarConfig().getStringList("NAMETAGS.FORMAT.NORMAL");
        TAB_SKIN_CACHE_LEFT = configs.getTablistConfig().getStringList("LEFT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        TAB_SKIN_CACHE_MIDDLE = configs.getTablistConfig().getStringList("MIDDLE").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        TAB_SKIN_CACHE_RIGHT = configs.getTablistConfig().getStringList("RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
        TAB_SKIN_CACHE_FAR_RIGHT = configs.getTablistConfig().getStringList("FAR_RIGHT").stream().map(s -> s.split(";")[0]).collect(Collectors.toList());
    }
}