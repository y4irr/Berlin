package net.cyruspvp.hub.utilities.users;

import lombok.Getter;
import lombok.Setter;
import net.cyruspvp.hub.user.UserListener;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.extra.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
public class UserManager extends Manager {

    private final Map<UUID, User> users;
    private final Map<String, UUID> uuidCache;

    public UserManager(Berlin instance) {
        super(instance);

        this.users = new ConcurrentHashMap<>();
        this.uuidCache = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        new UserListener(this.getInstance());
    }

    public User getByUUID(UUID uuid) {
        return users.get(uuid);
    }

    public User getByName(String name) {
        Player player = Bukkit.getPlayer(name);

        if (player != null) {
            return getByUUID(player.getUniqueId()); // Soporte para sistemas de disguise
        }

        UUID uuid = uuidCache.get(name);
        return (uuid != null ? users.get(uuid) : null);
    }
}
