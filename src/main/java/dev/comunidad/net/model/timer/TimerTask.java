package dev.comunidad.net.model.timer;

import dev.comunidad.net.BerlinPlugin;
import dev.comunidad.net.utilities.Berlin;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class TimerTask implements Runnable {

    private final Berlin plugin;
    private final Timer timer;
    private final int id;

    public TimerTask(Berlin plugin, Timer timer) {
        this.plugin = plugin;
        this.timer = timer;
        this.id = Bukkit.getScheduler().runTaskTimerAsynchronously(BerlinPlugin.getPlugin(), this, 0L, 20L).getTaskId();
    }

    @Override
    public void run() {
        if (timer == null) {
            this.cancel();
            return;
        }
        if (timer.getEndMillis() < System.currentTimeMillis()) {
            this.cancel();
            return;
        }
        if (!timer.isRunning()) {
            this.cancel();
        }
    }

    private void cancel() {
        plugin.getTimerManager().deleteTimer(timer);
        Bukkit.getScheduler().cancelTask(id);
    }
}
