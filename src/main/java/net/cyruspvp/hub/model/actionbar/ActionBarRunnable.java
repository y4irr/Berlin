package net.cyruspvp.hub.model.actionbar;

import net.cyruspvp.hub.utilities.actionbar.ActionBarAPI;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.Berlin;
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
