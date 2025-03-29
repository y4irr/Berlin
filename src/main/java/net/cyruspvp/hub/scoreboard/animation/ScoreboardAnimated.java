package net.cyruspvp.hub.scoreboard.animation;

import net.cyruspvp.hub.Berlin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardAnimated {

    @Getter private static String title;
    @Getter private static String footer;

    public static void init() {
        List<String> titles = Berlin.get().getScoreboardFile().getStringList("title-animation.lines");
        List<String> footers = Berlin.get().getScoreboardFile().getStringList("footer-animation.lines");

        title = titles.get(0);
        footer = footers.get(0);

        if (Berlin.get().getScoreboardFile().getBoolean("title-animation.enabled")) {
            AtomicInteger atomicInteger = new AtomicInteger();

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Berlin.getPlugin(), () -> {
                if (atomicInteger.get() == titles.size()) atomicInteger.set(0);

                title = titles.get(atomicInteger.getAndIncrement());

            }, 0L, (long) (Berlin.get().getScoreboardFile().getDouble("title-animation.interval") * 20L));
        }

        if (Berlin.get().getScoreboardFile().getBoolean("footer-animation.enabled")) {
            AtomicInteger atomicInteger = new AtomicInteger();

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Berlin.getPlugin(), () -> {
                if (atomicInteger.get() == footers.size()) atomicInteger.set(0);

                footer = footers.get(atomicInteger.getAndIncrement());

            }, 0L, (long) (Berlin.get().getScoreboardFile().getDouble("footer-animation.interval") * 20L));
        }
    }

    public static void disable() {
        footer = null;
        title = null;
    }
}