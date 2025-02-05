package net.cyruspvp.hub.model.rank.impl;

import net.cyruspvp.hub.model.rank.IRank;
import me.zowpy.core.api.CoreAPI;
import me.zowpy.core.api.profile.Profile;

import java.util.UUID;

public class Volcano implements IRank {

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
