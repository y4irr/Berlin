package net.cyruspvp.hub.nametags.task;

import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.nametags.NametagManager;
import net.cyruspvp.hub.utilities.versions.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings("ALL")
public class NametagTask extends Module<NametagManager> implements Runnable {

    public NametagTask(NametagManager manager) {
        super(manager);
    }

    @Override
    public void run() {
        try {
            Version version = getInstance().getVersionManager().getVersion();
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                getManager().handleUpdate(viewer, viewer);

                for (Player target : version.getTrackedPlayers(viewer)) {
                    if (viewer == target) continue;
                    getManager().handleUpdate(viewer, target);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}