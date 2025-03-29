package net.cyruspvp.hub.tablist.task;

import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.tablist.Tablist;
import net.cyruspvp.hub.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class TablistTask extends Module<TablistManager> implements Runnable {

    public TablistTask(TablistManager manager) {
        super(manager);
    }

    @Override
    public void run() {
        try {

            getManager().getTitle().tick();

            for (Player player : Bukkit.getOnlinePlayers()) {
                Tablist tablist = getManager().getTablists().get(player.getUniqueId());

                if (tablist != null) {
                    tablist.update();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}