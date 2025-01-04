package dev.comunidad.net.model.hotbar.normal.types;

import dev.comunidad.net.model.hotbar.normal.NormalHotbar;
import dev.comunidad.net.model.hotbar.normal.NormalHotbarManager;
import org.bukkit.entity.Player;

public class HidePlayerHotbar extends NormalHotbar {

    private final NormalHotbarManager normalHotbarManager;

    public HidePlayerHotbar(String name, NormalHotbarManager normalHotbarManager) {
        super(name);
        this.normalHotbarManager = normalHotbarManager;
    }

    @Override
    public void onItemInteract(Player player) {
        for (Player online : player.getServer().getOnlinePlayers()) {
            player.hidePlayer(online);
        }

        NormalHotbar showPlayerHotbar = normalHotbarManager.getHotbar("show-players");
        player.getInventory().setItem(showPlayerHotbar.getItemSlot(), showPlayerHotbar.getItem(player));
    }
}
