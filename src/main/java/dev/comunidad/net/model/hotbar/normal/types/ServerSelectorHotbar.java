package dev.comunidad.net.model.hotbar.normal.types;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.hotbar.normal.NormalHotbar;
import dev.comunidad.net.model.selector.menu.ServerSelectorMenu;
import dev.comunidad.net.utilities.PlayerUtil;
import org.bukkit.entity.Player;

public class ServerSelectorHotbar extends NormalHotbar {

    private final Berlin plugin;

    public ServerSelectorHotbar(Berlin plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void onItemInteract(Player player) {
        ServerSelectorMenu serverSelectorMenu = new ServerSelectorMenu(this.plugin);
        serverSelectorMenu.openMenu(player);
        PlayerUtil.playSound(player, "CHEST_OPEN");
    }
}
