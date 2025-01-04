package dev.comunidad.net.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public class User {

    private final UUID uuid;
    private final String name;
    private SocialStuff socialStuff;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.socialStuff = new SocialStuff(
                "https://youtube.com/c/yourChannel",
                "https://twitch.tv/yourChannel",
                "https://x.com/yourProfile",
                "@yourDiscordId"
        );

    }

    public void load(UserManager userManager) {
        userManager.getDatabase().load(this);
    }

    public void save(UserManager userManager) {
        userManager.getDatabase().save(this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
