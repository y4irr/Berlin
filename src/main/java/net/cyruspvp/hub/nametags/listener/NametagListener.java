package net.cyruspvp.hub.nametags.listener;

import net.cyruspvp.hub.nametags.NametagManager;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.nametags.Nametag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class NametagListener extends Module<NametagManager> {

    public NametagListener(NametagManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST) // call first
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        getManager().getNametags().put(player.getUniqueId(), new Nametag(getManager(), player));
    }

    @EventHandler(priority = EventPriority.HIGHEST) // call last
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Nametag nametag = getManager().getNametags().remove(player.getUniqueId());
        if (nametag != null) nametag.delete();
    }
}