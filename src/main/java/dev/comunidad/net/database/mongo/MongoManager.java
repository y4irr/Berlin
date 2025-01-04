package dev.comunidad.net.database.mongo;

import dev.comunidad.net.utilities.Berlin;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MongoManager {

    private final Berlin plugin;
    private final Map<String, Mongo> mongoMap;

    public MongoManager(Berlin plugin) {
        this.plugin = plugin;
        this.mongoMap = new HashMap<>();
        this.mongoMap.put("hcf", new Mongo(plugin, "hcf"));
        this.mongoMap.put("kitmap", new Mongo(plugin, "kitmap"));

        if (plugin.getConfigFile().getString("save_method").equalsIgnoreCase("MONGO")) {
            this.mongoMap.put("Berlin", new Mongo(plugin, "Berlin"));
        }
    }

    public Mongo getMongo(String name) {
        return mongoMap.get(name);
    }
}
