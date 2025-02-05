package net.cyruspvp.hub.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import net.cyruspvp.hub.BerlinPlugin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.Berlin;
import lombok.Getter;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class Mongo {

    private final String name;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private boolean connected;

    public Mongo(Berlin plugin, String name) {
        this.name = name;
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);

        try {
            MongoClientURI uri = new MongoClientURI(plugin.getDatabaseFile().getString("mongo-uri." + name));

            if (uri.getDatabase() == null) {
                ChatUtil.logger("&cDatabase name is not set in the URI.");
                return;
            }

            this.mongoClient = new MongoClient(uri);
            this.database = mongoClient.getDatabase(uri.getDatabase());
            this.connected = true;
            ChatUtil.logger("&a[" + BerlinPlugin.getPlugin().getName() + "] MongoDB successfully connected to " + name + " database.");
        }
        catch (MongoException | IllegalArgumentException | NullPointerException exception) {
            ChatUtil.logger("&c[" + BerlinPlugin.getPlugin().getName() + "] &cMongoDB failed to connect to " + name + " database.");
        }
    }
}
