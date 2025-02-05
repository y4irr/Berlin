package net.cyruspvp.hub.database.redis;

import net.cyruspvp.hub.BerlinPlugin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.Berlin;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter
public class Redis {

    private JedisPool jedisPool;
    private RedisPublisher redisPublisher;
    private boolean connected;

    public Redis(Berlin plugin) {
        try {
            this.jedisPool = new JedisPool("localhost", 6379);

            Jedis jedis = jedisPool.getResource();

            String password = plugin.getDatabaseFile().getString("redis.password");
            if (!password.isEmpty()) jedis.auth(password);

            this.redisPublisher = new RedisPublisher();

            (new Thread(() -> jedis.subscribe(redisPublisher, "Berlin"))).start();
            jedis.connect();

            connected = true;
            ChatUtil.logger("&a[" + BerlinPlugin.getPlugin().getName() + "] Redis successfully connected.");
        }
        catch (Exception exception) {
            ChatUtil.logger("&c[" + BerlinPlugin.getPlugin().getName() + "] &cRedis failed to connect.");
        }
    }

    public void disconnect() {
        jedisPool.destroy();
        redisPublisher.unsubscribe();
    }

    public void write(String json) {
        Jedis jedis = jedisPool.getResource();
        jedis.publish("Berlin", json);
    }
}
