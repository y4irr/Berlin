package net.cyruspvp.hub.utilities.users;

import lombok.Getter;
import lombok.Setter;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;


@Getter
@Setter
public class User extends Module<UserManager> {

    private UUID uniqueID;
    private String name;

    private boolean scoreboard;

    // For deserialization
    public User(UserManager manager, Map<String, Object> map) {
        super(manager);

        this.uniqueID = UUID.fromString((String) map.get("uniqueID"));
        this.name = (String) map.get("name");
        this.scoreboard = Boolean.parseBoolean((String) map.get("scoreboard"));
    }

    public User(UserManager manager, UUID uniqueID, String name) {
        super(manager);

        this.uniqueID = uniqueID;
        this.name = name;

        this.scoreboard = true;
        manager.getUsers().put(uniqueID, this);
        manager.getUuidCache().put(name, uniqueID);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>(); // keep order

        map.put("name", name);
        map.put("uniqueID", uniqueID.toString());
        map.put("scoreboard", String.valueOf(scoreboard));

        return map;
    }

    public String getName() {
        Player player = Bukkit.getPlayer(uniqueID);

        if (player != null) {
            return player.getName(); // Support disguise systems
        }

        return (name == null ? "Null User" : name);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueID);
    }

    public void save() {
//        getInstance().getStorageManager().getStorage().saveUser(this, true);
    }

    public void delete() {
//        getInstance().getStorageManager().getStorage().deleteUser(this);
    }
}