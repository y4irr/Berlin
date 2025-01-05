package dev.comunidad.net.user.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import dev.comunidad.net.user.SocialStuff;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.database.mongo.Mongo;
import dev.comunidad.net.user.User;
import dev.comunidad.net.user.UserManager;
import dev.comunidad.net.user.database.Database;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoDatabase extends Database {

    private MongoCollection<Document> collection;

    public MongoDatabase(Berlin plugin, UserManager userManager, Mongo mongo) {
        super(plugin, userManager);

        if (!mongo.isConnected()) {
            for (int i = 0; i < 3; i++) {
                System.out.println("Error trying register mongo profiles save method, mongo is not connected.");
            }
            return;
        }

        this.collection = mongo.getDatabase().getCollection("profiles");
    }

    @Override
    public void load(User user) {
        Document document = collection.find(new Document("uuid", user.getUuid().toString())).first();

        if (document == null) {
            save(user);
            return;
        }

        Document socialStuff = (Document) document.get("socialStuff");
        if (socialStuff != null) {
            SocialStuff socialStuffData = new SocialStuff(
                    socialStuff.getString("youtubeLink"),
                    socialStuff.getString("twitchLink"),
                    socialStuff.getString("twitterLink"),
                    socialStuff.getString("discordLink")
            );
            user.setSocialStuff(socialStuffData);
        }
    }

    @Override
    public void save(User user) {
        Document document = new Document();

        document.put("uuid", user.getUuid().toString());
        document.put("name", user.getName());

        SocialStuff socialStuff = user.getSocialStuff();
        Document socialStuffDoc = new Document()
                .append("youtubeLink", socialStuff.getYoutubeLink())
                .append("twitchLink", socialStuff.getTwitchLink())
                .append("twitterLink", socialStuff.getTwitterLink())
                .append("discordLink", socialStuff.getDiscordLink());

        document.put("socialStuff", socialStuffDoc);

        collection.insertOne(document, new InsertOneOptions().bypassDocumentValidation(true));
    }

    @Override
    public User getUserFromDB(String name) {
        User user = new User(findUUID(name), name);
        load(user);
        return user;
    }

    @Override
    public User getUserFromDB(UUID uuid) {
        User user = new User(uuid, findName(uuid));
        load(user);
        return user;
    }

    @Override
    public List<User> getUsersFromDB() {
        List<User> users = new ArrayList<>();

        for (Document document : collection.find()) {
            User user = getUserFromDB(document.getString("uuid"));
            users.add(user);
        }

        return users;
    }

    private UUID findUUID(String name) {
        return collection.find(Filters.eq("name", name)).first().get("uuid", UUID.class);
    }

    private String findName(UUID uuid) {
        return collection.find(Filters.eq("uuid", uuid.toString())).first().getString("name");
    }
}
