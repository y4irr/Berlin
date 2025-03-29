package net.cyruspvp.hub.nametags;

import lombok.Getter;
import net.cyruspvp.hub.nametags.packet.NametagPacket;
import net.cyruspvp.hub.utilities.Tasks;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.entity.Player;


@Getter
public class Nametag extends Module<NametagManager> {

    private final Player player;
    private final NametagPacket packet;
    private int protocolVersion;

    public Nametag(NametagManager manager, Player player) {
        super(manager);
        this.player = player;
        this.packet = manager.createPacket(player);
        this.protocolVersion = Utils.getProtocolVersion(player);
        Tasks.executeLater(getManager(), 20L, () -> protocolVersion = Utils.getProtocolVersion(player));
    }

    public void delete() {
        packet.delete();
    }
}