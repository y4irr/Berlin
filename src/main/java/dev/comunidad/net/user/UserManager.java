package dev.comunidad.net.user;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.database.mongo.Mongo;
import dev.comunidad.net.user.database.Database;
import dev.comunidad.net.user.database.impl.FlatFileDatabase;
import dev.comunidad.net.user.database.impl.MongoDatabase;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserManager {
    private final Berlin plugin;
    private final Database database;
    private final Map<UUID, User> users;

    public UserManager(Berlin plugin, @Nullable Mongo mongo) {
        this.plugin = plugin;
        this.users = new HashMap<>();

        if (mongo != null)
            this.database = new MongoDatabase(plugin, this, mongo);
        else
            this.database = new FlatFileDatabase(plugin, this);
    }

    public User getUser(UUID uuid) {
        return users.getOrDefault(uuid, null);
    }

    public void createUser(UUID uuid, String name) {
        User user = new User(uuid, name);
        user.load(this);
        users.put(uuid, user);
    }

    public void onDisable() {
        for (User user : users.values()) {
            user.save(this);
        }
    }
}
