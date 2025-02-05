package net.cyruspvp.hub.model.rank;

import java.util.UUID;

public interface IRank {

    String getRankName(UUID uuid);
    String getPrefix (UUID playerUUID);
    String getSuffix (UUID playerUUID);
}
