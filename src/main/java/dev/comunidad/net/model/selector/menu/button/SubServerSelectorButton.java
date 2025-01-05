package dev.comunidad.net.model.selector.menu.button;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.selector.SubServerSelector;
import dev.comunidad.net.utilities.BungeeUtil;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.item.ItemBuilder;
import dev.comunidad.net.utilities.menu.Button;
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
