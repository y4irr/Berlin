package dev.comunidad.net.model.ban.impl;

import dev.comunidad.net.model.ban.IBan;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class DefaultBan implements IBan {

    @Override
    public boolean isBanned(UUID playerUUID) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
        return player.isBanned();
    }

    @Override
    public String getBanReason(UUID playerUUID) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry banEntry = banList.getBanEntry(Bukkit.getOfflinePlayer(playerUUID).getName());

        if (banEntry != null) {
            return banEntry.getReason() != null ? banEntry.getReason() : "No reason specified";
        }
        return "Not banned";
    }

    @Override
    public long getBanDuration(UUID playerUUID) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry banEntry = banList.getBanEntry(Bukkit.getOfflinePlayer(playerUUID).getName());

        if (banEntry != null && banEntry.getExpiration() != null) {
            long expiration = banEntry.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            return expiration - currentTime > 0 ? expiration - currentTime : 0;
        }
        return -1; // -1 means permanent ban or not banned
    }
}
