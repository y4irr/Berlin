package dev.comunidad.net.scoreboard.animation;

import dev.comunidad.net.BerlinPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardAnimated {

    @Getter private static String title;
    @Getter private static String footer;

    public static void init() {
        List<String> titles = BerlinPlugin.get().getScoreboardFile().getStringList("title-animation.lines");
        List<String> footers = BerlinPlugin.get().getScoreboardFile().getStringList("footer-animation.lines");

        title = titles.get(0);
        footer = footers.get(0);

        if (BerlinPlugin.get().getScoreboardFile().getBoolean("title-animation.enabled")) {
            AtomicInteger atomicInteger = new AtomicInteger();

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(BerlinPlugin.getPlugin(), () -> {
                if (atomicInteger.get() == titles.size()) atomicInteger.set(0);

                title = titles.get(atomicInteger.getAndIncrement());

            }, 0L, (long) (BerlinPlugin.get().getScoreboardFile().getDouble("title-animation.interval") * 20L));
        }

        if (BerlinPlugin.get().getScoreboardFile().getBoolean("footer-animation.enabled")) {
            AtomicInteger atomicInteger = new AtomicInteger();

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(BerlinPlugin.getPlugin(), () -> {
                if (atomicInteger.get() == footers.size()) atomicInteger.set(0);

                footer = footers.get(atomicInteger.getAndIncrement());

            }, 0L, (long) (BerlinPlugin.get().getScoreboardFile().getDouble("footer-animation.interval") * 20L));
        }
    }

    public static void disable() {
        footer = null;
        title = null;
    }
}