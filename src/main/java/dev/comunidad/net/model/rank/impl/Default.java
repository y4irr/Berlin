package dev.comunidad.net.model.rank.impl;

import dev.comunidad.net.model.rank.IRank;

import java.util.UUID;

public class Default implements IRank {

    @Override
    public String getRankName(UUID uuid) {
        return "";
    }
    public String getPrefix(UUID playerUUID) {
        return "";
    }
}
