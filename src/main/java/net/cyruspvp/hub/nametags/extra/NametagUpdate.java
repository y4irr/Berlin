package net.cyruspvp.hub.nametags.extra;

import lombok.Getter;
import org.bukkit.entity.Player;


@Getter
public class NametagUpdate {

    private final Player viewer;
    private final Player target;

    public NametagUpdate(Player viewer, Player target) {
        this.viewer = viewer;
        this.target = target;
    }
}