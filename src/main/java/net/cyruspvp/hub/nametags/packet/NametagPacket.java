package net.cyruspvp.hub.nametags.packet;

import lombok.Getter;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.nametags.NametagManager;
import net.cyruspvp.hub.nametags.extra.NameVisibility;
import org.bukkit.entity.Player;


@Getter
public abstract class NametagPacket extends Module<NametagManager> {

    protected final Player player;

    public NametagPacket(NametagManager manager, Player player) {
        super(manager);
        this.player = player;
    }

    public abstract void create(String name, String color, String prefix, String suffix, boolean friendlyInvis, NameVisibility visibility);

    public abstract void addToTeam(Player target, String team);

    public abstract void delete();
}