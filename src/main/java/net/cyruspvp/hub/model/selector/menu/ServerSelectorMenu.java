package net.cyruspvp.hub.model.selector.menu;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.selector.ServerSelector;
import net.cyruspvp.hub.model.selector.ServerSelectorManager;
import net.cyruspvp.hub.model.selector.menu.button.ServerSelectorButton;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.menu.Button;
import net.cyruspvp.hub.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class ServerSelectorMenu extends Menu {

    private final Berlin plugin;
    private final ServerSelectorManager serverSelectorManager;
    private final Material fillMaterial;
    private final String fillName;
    private final String fillStyle;
    private final short fillData;
    private final boolean fillGlow;
    private final boolean fillEmptySlots;

    public ServerSelectorMenu(Berlin plugin) {
        this.plugin = plugin;
        this.serverSelectorManager = plugin.getServerSelectorManager();

        FileConfiguration config = plugin.getServerSelectorFile().getConfiguration();
        this.fillEmptySlots = config.getBoolean("menu.fill.enabled");
        this.fillStyle = plugin.getServerSelectorFile().getString("menu.fill.style").toLowerCase();
        this.fillMaterial = Material.getMaterial(config.getString("menu.fill.material"));
        this.fillData = (short) config.getInt("menu.fill.data");
        this.fillName = ChatUtil.translate(config.getString("menu.fill.name"));
        this.fillGlow = config.getBoolean("menu.fill.glow");
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtil.translate(plugin.getServerSelectorFile().getString("menu.title"));
    }

    @Override
    public int getSize() {
        return 9 * plugin.getServerSelectorFile().getInt("menu.rows");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (ServerSelector serverSelector : serverSelectorManager.getServerSelectors()) {
            buttons.put(serverSelector.getIconSlot(), new ServerSelectorButton(plugin, serverSelector));
        }

        if (fillEmptySlots) {
            applyFillPattern(buttons);
        }

        return buttons;
    }

    private void applyFillPattern(Map<Integer, Button> buttons) {
        int size = getSize();
        switch (fillStyle) {
            case "border":
                for (int i = 0; i < size; i++) {
                    if (i < 9 || i >= size - 9 || i % 9 == 0 || (i + 1) % 9 == 0) {
                        buttons.putIfAbsent(i, new FillGlassButton());
                    }
                }
                break;

            case "cross":
                for (int i = 0; i < size; i++) {
                    if ((i % 9 == 4) || (i >= (size / 2) - 4 && i <= (size / 2) + 4)) {
                        buttons.putIfAbsent(i, new FillGlassButton());
                    }
                }
                break;

            case "checker":
                for (int i = 0; i < size; i++) {
                    if ((i / 9) % 2 == (i % 2)) {
                        buttons.putIfAbsent(i, new FillGlassButton());
                    }
                }
                break;

            case "default":
            default:
                for (int i = 0; i < size; i++) {
                    buttons.putIfAbsent(i, new FillGlassButton());
                }
                break;
        }
    }

    private class FillGlassButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemStack item = new ItemStack(fillMaterial, 1, fillData);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(fillName);

                if (fillGlow) {
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                item.setItemMeta(meta);
            }
            return item;
        }
    }
}