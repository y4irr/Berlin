package net.cyruspvp.hub.tablist;

import org.bukkit.entity.Player;


public interface TablistAdapter {

    String[] getHeader(Player player);

    String[] getFooter(Player player);

    Tablist getInfo(Player player);

}