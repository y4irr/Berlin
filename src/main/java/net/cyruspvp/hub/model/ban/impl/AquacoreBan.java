package net.cyruspvp.hub.model.ban.impl;

import net.cyruspvp.hub.model.ban.IBan;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AquacoreBan implements IBan {

    @Override
    public boolean isBanned(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data != null && data.isBanned();
    }

    @Override
    public String getBanReason(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data == null ? "" : data.getActiveBan().getReason();
    }

    @Override
    public long getBanDuration(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        if (data != null && data.getActiveBan() != null) {
            if (data.getActiveBan().isPermanent()) {
                return -1;
            }
            return Long.parseLong(data.getActiveBan().getNiceDuration());
        }
        return 0;
    }

    @Override
    public String getExecutor(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data == null ? "": data.getActiveBan().getAddedBy();
    }
}
