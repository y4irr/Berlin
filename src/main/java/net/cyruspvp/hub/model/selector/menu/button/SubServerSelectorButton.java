package net.cyruspvp.hub.model.selector.menu.button;

import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.selector.SubServerSelector;
import net.cyruspvp.hub.utilities.BungeeUtil;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import net.cyruspvp.hub.utilities.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SubServerSelectorButton extends Button {

    private final Berlin plugin;
    private final SubServerSelector subServerSelector;

    public SubServerSelectorButton(Berlin plugin, SubServerSelector subServerSelector) {
        this.plugin = plugin;
        this.subServerSelector = subServerSelector;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = subServerSelector.getIcon().clone();
        return new ItemBuilder(itemStack)
                .setName(ChatUtil.placeholder(player, itemStack.getItemMeta().getDisplayName()))
                .setLore(ChatUtil.placeholder(player, itemStack.getItemMeta().getLore()))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        String subServer = subServerSelector.getSubServer();

        if (!subServer.isEmpty()) BungeeUtil.sendBungeeServer(plugin, player, subServer);

        subServerSelector.executeCommands(player);
    }
}
