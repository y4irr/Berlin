package dev.comunidad.net.model.rank.impl;

import dev.comunidad.net.model.rank.IRank;
import me.zowpy.core.api.CoreAPI;
import me.zowpy.core.api.profile.Profile;

import java.util.UUID;

public class Kup implements IRank {

    @Override
    public String getRankName(UUID uuid) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(uuid);
        return profile.getDisplayRank().getDisplayName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(uuid);
        return profile.getDisplayRank().getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        Profile profile = CoreAPI.getInstance().getProfileManager().getByUUID(uuid);
        return profile.getDisplayRank().getSuffix();
    }
}