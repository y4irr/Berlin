package dev.comunidad.net.model.rank.impl;

import dev.comunidad.net.model.rank.IRank;
import services.plasma.helium.api.HeliumAPI;

import java.util.UUID;

public class Helium implements IRank {

    @Override
    public String getRankName(UUID uuid) {
        return HeliumAPI.INSTANCE.getRankName(uuid);
    }

    @Override
    public String getPrefix(UUID playerUUID) {
        return HeliumAPI.INSTANCE.getRankPrefix(playerUUID);
    }
}
