package dev.comunidad.net.model.hotbar.normal.types;

import dev.comunidad.net.model.hotbar.normal.NormalHotbar;
import dev.comunidad.net.model.hotbar.normal.NormalHotbarManager;
import org.bukkit.entity.Player;

public class ShowPlayerHotbar extends NormalHotbar {

    private final NormalHotbarManager normalHotbarManager;

    public ShowPlayerHotbar(String name, NormalHotbarManager normalHotbarManager) {
        super(name);
        this.normalHotbarManager = normalHotbarManager;
    }

    @Override
    public void onItemInteract(Player player) {
        for (Player online : player.getServer().getOnlinePlayers()) {
            player.showPlayer(online);
        }

        NormalHotbar hidePlayerHotbar = normalHotbarManager.getHotbar("hide-players");
        player.getInventory().setItem(hidePlayerHotbar.getItemSlot(), hidePlayerHotbar.getItem(player));
    }
}
