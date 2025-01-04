package dev.comunidad.net.model.rank;

import java.util.UUID;

public interface IRank {

    String getRankName(UUID uuid);
    String getPrefix (UUID playerUUID);
}
