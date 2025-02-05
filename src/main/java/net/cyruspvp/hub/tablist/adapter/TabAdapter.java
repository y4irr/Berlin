package net.cyruspvp.hub.tablist.adapter;

import org.bukkit.entity.Player;
import net.cyruspvp.hub.tablist.setup.TabEntry;

import java.util.List;

public interface TabAdapter {

    /**
     * Get the tab header for a player.
     *
     * @param player the player
     * @return string
     */
    String getHeader(Player player);

    /**
     * Get the tab player for a player.
     *
     * @param player the player
     * @return string
     */
    String getFooter(Player player);

    /**
     * Get the tab lines for a player.
     *
     * @param player the player
     * @return list of entries
     */
    List<TabEntry> getLines(Player player);
}
