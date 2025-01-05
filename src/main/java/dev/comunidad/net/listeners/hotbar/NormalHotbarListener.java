package dev.comunidad.net.listeners.hotbar;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.hotbar.normal.NormalHotbar;
import dev.comunidad.net.model.hotbar.normal.NormalHotbarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Astro
 * Project: Berlin
 * Date: 20-12-2022
 * Twitter: @AstroDev
 * GitHub: https://github.com/AstroDev
 */

public class NormalHotbarListener implements Listener {

    private final NormalHotbarManager normalHotbarManager;

    public NormalHotbarListener(Berlin plugin) {
        this.normalHotbarManager = plugin.getNormalHotbarManager();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        normalHotbarManager.giveHotbar(event.getPlayer());
    }

    @EventHandler
    private void onHotbarInteract(PlayerInteractEvent event) {
        if (event.getItem() == null
                || event.getAction() != Action.RIGHT_CLICK_BLOCK
                && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        NormalHotbar hotbar = normalHotbarManager.getHotbar(event.getItem());
        if (hotbar == null)
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        hotbar.onItemInteract(player);
    }
}
