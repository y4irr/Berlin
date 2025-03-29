package net.cyruspvp.hub.model.rank.impl;

import net.cyruspvp.hub.model.rank.IRank;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;

import java.util.UUID;

public class AquaCore implements IRank {

    @Override
    public String getName(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "" : data.getHighestRank().getName();
    }
    @Override
    public String getPrefix (UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data == null ? "" : data.getHighestRank().getPrefix();
    }
    @Override
    public String getSuffix (UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data == null ? "" : data.getHighestRank().getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "" : String.valueOf(data.getHighestRank().getColor());
    }
}