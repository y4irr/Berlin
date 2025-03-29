package net.cyruspvp.hub.utilities;

import com.viaversion.viaversion.api.Via;
import net.cyruspvp.hub.Berlin;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.PluginManager;
import protocolsupport.api.ProtocolSupportAPI;

import java.util.Arrays;

public class Utils {
    private static final String NMS_VER = getNMSVer();

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        } else if (repeat <= 0) {
            return "";
        } else {
            int inputLength = str.length();
            if (repeat != 1 && inputLength != 0) {
                if (inputLength == 1 && repeat <= 8192) {
                    return repeat(str.charAt(0), repeat);
                } else {
                    int outputLength = inputLength * repeat;
                    switch (inputLength) {
                        case 1:
                            return repeat(str.charAt(0), repeat);
                        case 2:
                            char ch0 = str.charAt(0);
                            char ch1 = str.charAt(1);
                            char[] output2 = new char[outputLength];

                            for (int i = repeat * 2 - 2; i >= 0; --i) {
                                output2[i] = ch0;
                                output2[i + 1] = ch1;
                                --i;
                            }

                            return new String(output2);
                        default:
                            StringBuilder buf = new StringBuilder(outputLength);

                            for (int i = 0; i < repeat; ++i) {
                                buf.append(str);
                            }

                            return buf.toString();
                    }
                }
            } else {
                return str;
            }
        }
    }

    private static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return "";
        } else {
            char[] buf = new char[repeat];
            Arrays.fill(buf, ch);
            return new String(buf);
        }
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() <= len ? str : str.substring(0, len);
        }
    }

    public static String getNMSVer() {
        try {

            String packageName = Bukkit.getServer().getClass().getPackage().getName(); // Craft Server
            return packageName.split("\\.")[3].replaceAll("v", ""); // we don't want the v

        } catch (Exception e) {
            return "Latest";
        }
    }

    public static boolean isModernVer() {
        return NMS_VER.equals("1_16_R3") || NMS_VER.equals("1_17_R1") || NMS_VER.equalsIgnoreCase("1_18_R2")
                || NMS_VER.equals("1_19_R3") || NMS_VER.equals("1_20_R1") || NMS_VER.equalsIgnoreCase("latest");
    }

    public static boolean isVer1_7(int protocol) {
        return protocol <= 5;
    }

    public static int getProtocolVersion(Player player) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("ViaVersion") != null) {
            try {

                return Via.getAPI().getPlayerVersion(player.getUniqueId());

            } catch (Exception e) {
                return us.myles.ViaVersion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
            }

        } else if (pluginManager.getPlugin("ProtocolSupport") != null) {
            return ProtocolSupportAPI.getProtocolVersion(player).getId();
        }

        return 100;
    }

    public static boolean verifyPlugin(String plugin, Berlin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        return pm.getPlugin(plugin) != null;
    }

    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}