package net.cyruspvp.hub.model.ban.impl;

import net.cyruspvp.hub.model.ban.IBan;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;

import java.util.UUID;

public class AquacoreBan implements IBan {

    @Override
    public boolean isBanned(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data != null && data.getPunishData().isBanned();
    }

    @Override
    public String getBanReason(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        return data == null ? "" : data.getPunishData().getActiveBan().getReason();
    }

    @Override
    public long getBanDuration(UUID playerUUID) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(playerUUID);
        if (data != null && data.getPunishData().getActiveBan() != null) {
            if (data.getPunishData().getActiveBan().isPermanent()) {
                return -1;
            }
            return Long.parseLong(data.getPunishData().getActiveBan().getNiceDuration());
        }
        return 0;
    }
}
