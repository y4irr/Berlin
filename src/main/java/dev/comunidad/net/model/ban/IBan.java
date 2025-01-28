package dev.comunidad.net.model.ban;

import java.util.UUID;

public interface IBan {

    boolean isBanned(UUID playerUUID);
    String getBanReason(UUID playerUUID);
    long getBanDuration(UUID playerUUID);
}
