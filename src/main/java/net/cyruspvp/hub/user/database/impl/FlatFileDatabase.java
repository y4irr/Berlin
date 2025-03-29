package net.cyruspvp.hub.user.database.impl;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.user.SocialStuff;
import net.cyruspvp.hub.user.User;
import net.cyruspvp.hub.user.UserManager;
import net.cyruspvp.hub.user.database.Database;
import net.cyruspvp.hub.utilities.file.FileConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlatFileDatabase extends Database {

    private final FileConfig fileConfig;
    private final FileConfiguration configuration;

    public FlatFileDatabase(Berlin plugin, UserManager userManager) {
        super(plugin, userManager);

        this.fileConfig = new FileConfig(Berlin.getPlugin(), "users.yml");
        this.configuration = fileConfig.getConfiguration();
    }

    @Override
    public void load(User user) {
        String path = "users." + user.getUuid().toString() + ".";

        // Load player social stuff if it exists
        if (configuration.contains(path + "socialStuff")) {
            String youtubeLink = configuration.getString(path + "socialStuff.youtube");
            String twitchLink = configuration.getString(path + "socialStuff.twitch");
            String twitterLink = configuration.getString(path + "socialStuff.twitter");
            String discordLink = configuration.getString(path + "socialStuff.discord");

            user.setSocialStuff(new SocialStuff(youtubeLink, twitchLink, twitterLink, discordLink));
        }
    }

    @Override
    public void save(User user) {
        String path = "users." + user.getUuid().toString() + ".";

        configuration.set(path + "name", user.getName());

        // Save player social stuff
        configuration.set(path + "socialStuff.twitter", user.getSocialStuff().getTwitterLink());
        configuration.set(path + "socialStuff.youtube", user.getSocialStuff().getYoutubeLink());
        configuration.set(path + "socialStuff.discord", user.getSocialStuff().getDiscordLink());
        configuration.set(path + "socialStuff.twitch", user.getSocialStuff().getTwitchLink());

        fileConfig.save();
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

        for (String key : configuration.getConfigurationSection("users").getKeys(false)) {
            users.add(getUserFromDB(UUID.fromString(key)));
        }

        return users;
    }

    private UUID findUUID(String name) {
        String path = "users.";

        for (String key : configuration.getConfigurationSection(path).getKeys(false)) {
            if (configuration.getString(path + key + ".name").equalsIgnoreCase(name)) return UUID.fromString(key);
        }

        return null;
    }

    private String findName(UUID uuid) {
        return configuration.getString("users." + uuid.toString() + ".name");
    }
}
