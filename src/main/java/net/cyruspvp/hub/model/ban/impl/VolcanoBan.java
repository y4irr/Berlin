package net.cyruspvp.hub.model.ban.impl;

import net.cyruspvp.hub.model.ban.IBan;
import me.zowpy.core.api.CoreAPI;
import me.zowpy.core.api.profile.Profile;

import java.util.UUID;

public class VolcanoBan implements IBan {

    @Override
    public boolean isBanned(UUID playerUUID) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(playerUUID);
        if (profile == null) {
            return false;
        }

        if (profile.getBanPunishment() == null) return false;

        return profile.getBanPunishment().checkActive();
    }

    @Override
    public String getBanReason(UUID playerUUID) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(playerUUID);
        if (profile == null) {
            return "No reason available";
        }
        return profile.getBanPunishment().getReason();
    }

    @Override
    public long getBanDuration(UUID playerUUID) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(playerUUID);
        if (profile == null) {
            return 0;
        }
        return profile.getBanPunishment().getDuration();
    }

    @Override
    public String getExecutor(UUID playerUUID) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(playerUUID);
        if (profile == null) {
            return "Console";
        }
        return String.valueOf(profile.getBanPunishment().getIssuer());
    }
}
