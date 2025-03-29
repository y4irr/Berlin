package net.cyruspvp.hub.database.redis;

import com.google.gson.Gson;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.model.queue.Queue;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 23-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class RedisPublisher extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        RedisMessage redisMessage = new Gson().fromJson(message, RedisMessage.class);

        if (Objects.requireNonNull(redisMessage.getPayload()) == RedisPayload.QUEUE_PAUSE_UPDATE) {
            String queueName = redisMessage.getParam("queue-name");

            if (!Berlin.get().getQueueManager().exists(queueName)) return;

            Queue queue = Berlin.get().getQueueManager().getQueue(queueName);
            queue.setPaused(!Boolean.parseBoolean(redisMessage.getParam("queue-pause")));
        } else {
            ChatUtil.logger("&c[Redis] The message was received, but there was no response");
        }
    }
}
