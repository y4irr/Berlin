//package net.cyruspvp.hub.utilities.users.menu;
//
//import net.cyruspvp.hub.modules.framework.menu.Menu;
//import net.cyruspvp.hub.modules.framework.menu.MenuManager;
//import net.cyruspvp.hub.modules.framework.menu.button.Button;
//import net.cyruspvp.hub.utilities.users.User;
//import net.cyruspvp.hub.utilities.users.settings.UserSetting;
//import net.cyruspvp.hub.utilities.ItemBuilder;
//import net.cyruspvp.hub.utilities.ItemUtils;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class SettingsMenu extends Menu {
//
//    public SettingsMenu(MenuManager manager, Player player) {
//        super(
//                manager,
//                player,
//                manager.getLanguageConfig().getString("SETTINGS_COMMAND.TITLE"),
//                manager.getLanguageConfig().getInt("SETTINGS_COMMAND.SIZE"),
//                false
//        );
//    }
//
//    @Override
//    public Map<Integer, Button> getButtons(Player player) {
//        Map<Integer, Button> buttons = new HashMap<>();
//        User user = getInstance().getUserManager().getByUUID(player.getUniqueId());
//
//
//        for (String key : getLanguageConfig().getConfigurationSection("SETTINGS_COMMAND.ITEMS").getKeys(false)) {
//            buttons.put(getLanguageConfig().getInt("SETTINGS_COMMAND.ITEMS." + key + ".SLOT"), new Button() {
//                private final UserSetting setting = getSetting(key);
//
//                @Override
//                public void onClick(InventoryClickEvent e) {
//                    e.setCancelled(true);
//
//                    if (setting != null) {
//                        if (setting.toString().equals("SCOREBOARD")) {
//                            user.setScoreboard(!user.isScoreboard());
//                        }
//
//                        update(); // Update the menu
//                    }
//                }
//
//                @Override
//                public ItemStack getItemStack() {
//                    ItemBuilder builder = new ItemBuilder(ItemUtils.getMat(getLanguageConfig().getString("SETTINGS_COMMAND.ITEMS." + key + ".MATERIAL")))
//                            .setName(getLanguageConfig().getString("SETTINGS_COMMAND.ITEMS." + key + ".NAME"))
//                            .data(getManager(), getLanguageConfig().getInt("SETTINGS_COMMAND.ITEMS." + key + ".DATA"));
//
//                    if (setting != null) {
//                        builder.setLore(getLanguageConfig().getStringList("SETTINGS_COMMAND.ITEMS." + key +
//                                ".LORE_" + convert(user, setting.toString()))); // basically handles ENABLED or DISABLED
//                    } else {
//                        builder.setLore(getLanguageConfig().getStringList("SETTINGS_COMMAND.ITEMS." + key + ".LORE"));
//                    }
//
//                    return builder.toItemStack();
//                }
//            });
//        }
//
//        return buttons;
//    }
//
//    private String convert(User user, String setting) {
//        if (setting.equals("SCOREBOARD")) {
//            return convertBoolean(user.isScoreboard());
//        }
//
//        return "";
//    }
//
//    private UserSetting getSetting(String name) {
//        try {
//
//            return UserSetting.valueOf(name);
//
//        } catch (IllegalArgumentException e) {
//            return null;
//        }
//    }
//
//    private String convertBoolean(boolean bool) {
//        return (bool ? "ENABLED" : "DISABLED");
//    }
//}