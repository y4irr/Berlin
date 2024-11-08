package dev.astro.net.scoreboard.animation;

import dev.astro.net.CometPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardAnimated {

    @Getter private static String title;
    @Getter private static String footer;

    public static void init() {
        List<String> titles = CometPlugin.get().getScoreboardFile().getStringList("title-animation.lines");
        List<String> footers = CometPlugin.get().getScoreboardFile().getStringList("footer-animation.lines");

        title = titles.isEmpty() ? "" : titles.get(0);
        footer = footers.isEmpty() ? "" : footers.get(0);

        if (CometPlugin.get().getScoreboardFile().getBoolean("title-animation.enabled")) {
            startAnimation(titles, "title-animation.interval", newTitle -> title = newTitle);
        }

        if (CometPlugin.get().getScoreboardFile().getBoolean("footer-animation.enabled")) {
            startAnimation(footers, "footer-animation.interval", newFooter -> footer = newFooter);
        }
    }

    private static void startAnimation(List<String> lines, String configPath, java.util.function.Consumer<String> updater) {
        if (lines.isEmpty()) return;

        AtomicInteger index = new AtomicInteger();
        long intervalTicks = (long) (CometPlugin.get().getScoreboardFile().getDouble(configPath) * 20L);

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(CometPlugin.getPlugin(), () -> {
            if (index.get() >= lines.size()) index.set(0);
            updater.accept(lines.get(index.getAndIncrement()));
        }, 0L, intervalTicks);
    }

    public static void disable() {
        title = null;
        footer = null;
    }
}
