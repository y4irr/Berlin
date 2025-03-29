package net.cyruspvp.hub.model.rank.impl;

import net.cyruspvp.hub.model.rank.IRank;
import services.plasma.helium.api.HeliumAPI;

import java.util.UUID;

public class Helium implements IRank {

    @Override
    public String getName(UUID uuid) {
        return HeliumAPI.INSTANCE.getRankName(uuid);
    }

    @Override
    public String getPrefix(UUID playerUUID) {
        return HeliumAPI.INSTANCE.getRankPrefix(playerUUID);
    }

    @Override
    public String getSuffix(UUID playerUUID) {
        return HeliumAPI.INSTANCE.getRankSuffix(playerUUID);
    }

    @Override
    public String getColor(UUID uuid) {
        return HeliumAPI.INSTANCE.getRankColor(uuid);
    }
}
