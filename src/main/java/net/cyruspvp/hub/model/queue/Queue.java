package net.cyruspvp.hub.model.queue;

import net.cyruspvp.hub.model.queue.player.QueuePlayer;
import net.cyruspvp.hub.model.queue.player.QueuePlayerComparator;
import lombok.Getter;
import lombok.Setter;

import java.util.PriorityQueue;

/**
 * Created by Risas
 * Project: Berlin
 * Date: 19-01-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter @Setter
public class Queue {

    private String server;
    private boolean paused;
    private PriorityQueue<QueuePlayer> players;

    public Queue() {
        this.players = new PriorityQueue<>(new QueuePlayerComparator());
    }
}
