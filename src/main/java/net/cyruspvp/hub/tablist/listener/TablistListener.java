package net.cyruspvp.hub.tablist.listener;

import net.cyruspvp.hub.tablist.Tablist;
import net.cyruspvp.hub.tablist.TablistManager;
import net.cyruspvp.hub.tablist.extra.TablistSkin;
import net.cyruspvp.hub.utilities.Tasks;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class TablistListener extends Module<TablistManager> {

    public TablistListener(TablistManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // call last
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Tasks.executeLater(getManager(), 20L, () -> new Tablist(getManager(), player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        // Remove them from the map to help ram usage.
        getManager().getTablists().remove(player.getUniqueId());
        TablistSkin.SKIN_CACHE.remove(player.getUniqueId());
    }
}