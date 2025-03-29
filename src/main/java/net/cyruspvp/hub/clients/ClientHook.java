package net.cyruspvp.hub.clients;

import lombok.Getter;
import net.cyruspvp.hub.clients.type.CheatBreakerClient;
import net.cyruspvp.hub.clients.type.LunarClient;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Manager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


@Getter
public class ClientHook extends Manager implements Client {

    private final List<Client> clients;

    public ClientHook(Berlin instance) {
        super(instance);
        this.clients = new ArrayList<>();
        this.load();
    }

    private void load() {
        if (Utils.verifyPlugin("Apollo-Bukkit", getInstance())) {
            clients.add(new LunarClient(this));
        }

        if (Utils.verifyPlugin("CheatBreakerAPI", getInstance())) {
            clients.add(new CheatBreakerClient(this));
        }
    }

    @Override
    public void overrideNametags(Player target, Player viewer, List<String> tag) {
        for (Client client : clients) {
            client.overrideNametags(target, viewer, tag);
        }
    }

    @Override
    public void handleJoin(Player player) {
        for (Client client : clients) {
            client.handleJoin(player);
        }
    }
}