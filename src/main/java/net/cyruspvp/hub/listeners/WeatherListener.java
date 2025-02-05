package net.cyruspvp.hub.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {
    public WeatherListener() {
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent e) {
        World world = e.getWorld();
        if (!e.toWeatherState()) {
            e.setCancelled(false);
        }

        if (!world.hasStorm()) {
            world.setStorm(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setThundering(false);
        }

    }
}
