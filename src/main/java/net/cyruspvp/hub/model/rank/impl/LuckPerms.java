package net.cyruspvp.hub.model.rank.impl;

import net.cyruspvp.hub.model.rank.IRank;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;

import java.util.UUID;

public class LuckPerms implements IRank {

    private final net.luckperms.api.LuckPerms luckPerms;

    public LuckPerms() {
        this.luckPerms = Bukkit.getServicesManager().load(net.luckperms.api.LuckPerms.class);
    }

    @Override
    public String getName(UUID uuid) {
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

    @Override
    public String getSuffix(UUID uuid) {
        User user = getUser(uuid);
        if (user == null) {
            return "";
        }

        String suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix != null ? suffix : "";
    }

    @Override
    public String getColor(UUID uuid) {
        User user = getUser(uuid);
        if (user == null) {
            return "";
        }

        String color = user.getCachedData().getMetaData().getMetaValue("color");
        return color != null ? color : "";
    }

    private User getUser(UUID uuid) {
        return this.luckPerms.getUserManager().getUser(uuid);
    }
}
