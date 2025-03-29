package net.cyruspvp.hub.model.rank.impl;

import net.cyruspvp.hub.model.rank.IRank;

import java.util.UUID;

public class Default implements IRank {

    @Override
    public String getName(UUID uuid) {
        return "";
    }
    public String getPrefix(UUID playerUUID) {
        return "";
    }
    public String getSuffix(UUID playerUUID) {
        return "";
    }
    public String getColor(UUID uuid) {
        return "";
    }
}
