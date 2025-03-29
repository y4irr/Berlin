package net.cyruspvp.hub.clients;

import org.bukkit.entity.Player;
import java.util.List;


public interface Client {

    void overrideNametags(Player target, Player viewer, List<String> tag);

    void handleJoin(Player player);
}