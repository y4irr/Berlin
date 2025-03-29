package net.cyruspvp.hub.tablist.packet;

import lombok.Getter;
import net.cyruspvp.hub.nametags.Nametag;
import net.cyruspvp.hub.nametags.extra.NameVisibility;
import net.cyruspvp.hub.tablist.packet.type.TablistPacketV1_8_R3;
import net.cyruspvp.hub.tablist.TablistManager;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.utilities.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
public abstract class TablistPacket extends Module<TablistManager> {

    protected final Player player;

    public TablistPacket(TablistManager manager, Player player) {
        super(manager);
        this.player = player;
    }

    public String getName(int col, int row) {
        char[] chars = String.valueOf(row).toCharArray();
        boolean addAlphabet = (this instanceof TablistPacketV1_8_R3 && Utils.getProtocolVersion(player) > 47);
        return "§" + col + (row >= 10 ? (addAlphabet ? "§a" : "") + "§" + chars[0] + "§" + chars[1] : "§" + chars[0]);
    }

    public int calcSlot(int col, int row) {
        return row + (col == 0 ? 0 : col == 1 ? 20 : col == 2 ? 40 : 60);
    }

    public void handleTeams(Player fake, String text, int slot) {
        // My nametag api uses packets so that is better than the bukkit api.
        Nametag nametag = getInstance().getNametagManager().getNametags().get(player.getUniqueId());

        // Just in case
        if (nametag == null) return;

        // Tablist sorting from 0
        String team = "00000000000000" + (slot >= 10 ? slot : "0" + slot);

        // 1.16+ There is no limit.
        if (Utils.isModernVer()) {
            nametag.getPacket().create(team, "", text, "", false, NameVisibility.ALWAYS);
            nametag.getPacket().addToTeam(fake, team);
            return;
        }

        if (text.length() < 17) {
            nametag.getPacket().create(team, "", text, "", false, NameVisibility.ALWAYS);

        } else {
            final String left = text.substring(0, 16);
            final String right = text.substring(16);

            if (left.endsWith("§")) {
                nametag.getPacket().create(team, "",
                        left.substring(0, left.toCharArray().length - 1),
                        Utils.left(ChatColor.getLastColors(left) + "§" + right, 16),
                        false, NameVisibility.ALWAYS);

            } else {
                nametag.getPacket().create(team, "",
                        left,
                        Utils.left(ChatColor.getLastColors(left) + right, 16),
                        false, NameVisibility.ALWAYS);
            }
        }

        nametag.getPacket().addToTeam(fake, team);
    }

    public abstract void update();
}