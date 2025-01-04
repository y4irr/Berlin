package dev.comunidad.net.model.rank.impl;

import dev.comunidad.net.model.rank.IRank;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;

import java.util.UUID;

public class LuckPerms implements IRank {

    private final net.luckperms.api.LuckPerms luckPerms;

    public LuckPerms() {
        this.luckPerms = Bukkit.getServicesManager().load(net.luckperms.api.LuckPerms.class);
    }

    @Override
    public String getRankName(UUID uuid) {
        User user = getUser(uuid);
        return user == null ? "" : user.getPrimaryGroup();
    }

    @Override
    public String getPrefix(UUID uuid) {
        User user = getUser(uuid);
        if (user == null) {
            return "";
        }

        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? prefix : "";
    }

    private User getUser(UUID uuid) {
        return this.luckPerms.getUserManager().getUser(uuid);
    }
}
