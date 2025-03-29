package net.cyruspvp.hub.model.selector.menu.button;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.selector.ServerSelector;
import net.cyruspvp.hub.model.selector.menu.SubServerSelectorMenu;
import net.cyruspvp.hub.utilities.BungeeUtil;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import net.cyruspvp.hub.utilities.menu.Button;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ServerSelectorButton extends Button {

    private final Berlin plugin;
    private final ServerSelector serverSelector;

    public ServerSelectorButton(Berlin plugin, ServerSelector serverSelector) {
        this.plugin = plugin;
        this.serverSelector = serverSelector;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = serverSelector.getIcon().clone();
        return new ItemBuilder(itemStack)
                .setName(ChatUtil.placeholder(player, itemStack.getItemMeta().getDisplayName()))
                .setLore(ChatUtil.placeholder(player, itemStack.getItemMeta().getLore()))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!serverSelector.getSubServerSelectors().isEmpty()) {
            SubServerSelectorMenu subServerSelectorMenu = new SubServerSelectorMenu(plugin, serverSelector);
            String soundName = plugin.getConfigFile().getString("SELECTOR-CLICK-SOUND");

            Sound Sound = null;
            try {
                Sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException | NullPointerException e) {
                plugin.getLogger().warning("Invalid Sound! You are using " + soundName);
            }

            if (Sound != null) {
                Sound finalSound = Sound;
                player.playSound(player.getLocation(), finalSound, 1.0f, 1.0f);
            }
            subServerSelectorMenu.openMenu(player);
            return;
        }

        String server = serverSelector.getServer();

        if (!server.isEmpty()) BungeeUtil.sendBungeeServer(plugin, player, server);
        String soundName = plugin.getConfigFile().getString("SUBSELECTOR-CLICK-SOUND");

        Sound Sound = null;
        try {
            Sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException | NullPointerException e) {
            plugin.getLogger().warning("Invalid Sound! You are using " + soundName);
        }

        if (Sound != null) {
            Sound finalSound = Sound;
            player.playSound(player.getLocation(), finalSound, 1.0f, 1.0f);
        }
        serverSelector.executeCommands(player);
    }
}
