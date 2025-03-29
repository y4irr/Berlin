package net.cyruspvp.hub.database.redis;

import net.cyruspvp.hub.Berlin;
import lombok.Getter;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter
public class RedisManager {

    private final Redis redis;

    public RedisManager(Berlin plugin) {
        this.redis = new Redis(plugin);
    }

    public void onDisable() {
        if (redis.isConnected()) redis.disconnect();
    }
}
