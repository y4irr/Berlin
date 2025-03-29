package net.cyruspvp.hub.clients.type;

import com.cheatbreaker.api.CheatBreakerAPI;
import net.cyruspvp.hub.clients.Client;
import net.cyruspvp.hub.clients.ClientHook;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.entity.Player;
import java.util.List;

public class CheatBreakerClient extends Module<ClientHook> implements Client {

    public CheatBreakerClient(ClientHook manager) {
        super(manager);
    }

    @Override
    public void overrideNametags(Player target, Player viewer, List<String> tag) {
        CheatBreakerAPI.getInstance().overrideNametag(target, tag, viewer);
    }

    @Override
    public void handleJoin(Player player) {
    }
}