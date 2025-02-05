package net.cyruspvp.hub.model.hotbar.normal.types;

import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.hotbar.normal.NormalHotbar;
import net.cyruspvp.hub.model.selector.menu.ServerSelectorMenu;
import net.cyruspvp.hub.utilities.PlayerUtil;
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
