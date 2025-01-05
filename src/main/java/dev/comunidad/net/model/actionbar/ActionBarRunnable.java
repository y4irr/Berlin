package dev.comunidad.net.model.actionbar;

import dev.comunidad.net.utilities.actionbar.ActionBarAPI;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.Bukkit;

import java.util.List;

public class ActionBarRunnable implements Runnable {

    private final Berlin plugin;
    private final int interval;
    private int index;

    public ActionBarRunnable(Berlin plugin, int interval) {
        this.interval = interval;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            List<String> messages = plugin.getConfigFile().getStringList("action-bar.messages");
            if (index == messages.size()) index = 0;

            ActionBarAPI.sendActionBarToAllPlayers(ChatUtil.translate(messages.get(index)), interval * 20);
            index++;
        }
    }
}
