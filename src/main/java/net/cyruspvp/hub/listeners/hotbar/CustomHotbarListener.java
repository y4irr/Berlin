package net.cyruspvp.hub.listeners.hotbar;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.model.hotbar.custom.CustomHotbar;
import net.cyruspvp.hub.model.hotbar.custom.CustomHotbarManager;
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

public class CustomHotbarListener implements Listener {

    private final CustomHotbarManager customHotbarManager;

    public CustomHotbarListener(Berlin plugin) {
        this.customHotbarManager = plugin.getCustomHotbarManager();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        customHotbarManager.giveHotbar(event.getPlayer());
    }

    @EventHandler
    private void onHotbarInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        CustomHotbar hotbar = customHotbarManager.getHotbar(event.getItem());
        if (hotbar == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        for (String command : hotbar.getCommands()) {
            player.performCommand(command);
        }
    }
}
