package net.cyruspvp.hub.tablist;

import lombok.Getter;
import net.cyruspvp.hub.utilities.file.Config;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.tablist.extra.TablistEntry;
import net.cyruspvp.hub.tablist.extra.TablistSkin;
import net.cyruspvp.hub.tablist.packet.TablistPacket;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.extra.Pair;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Getter
public class Tablist extends Module<TablistManager> {

    private final Map<Pair<Integer, Integer>, TablistEntry> entries;
    private final Map<Pair<Integer, Integer>, TablistSkin> oldSkinData;
    private final TablistPacket packet;

    public Tablist(TablistManager manager, Player player) {
        super(manager);
        this.entries = new ConcurrentHashMap<>();
        this.oldSkinData = new ConcurrentHashMap<>();
        this.packet = manager.createPacket(player);
        manager.getTablists().put(player.getUniqueId(), this);
    }

    public void update() {
        // Store old skin data
        for (Map.Entry<Pair<Integer, Integer>, TablistEntry> entry : entries.entrySet()) {
            TablistEntry tablistEntry = entry.getValue();

            if (tablistEntry.getSkin() != null) {
                oldSkinData.put(entry.getKey(), tablistEntry.getSkin());
            }
        }

        packet.update(); // Updating will get the entries again.
    }

    public TablistEntry getEntry(int col, int row) {
        TablistEntry entry = entries.get(new Pair<>(col, row));

        if (entry == null) {
            TablistEntry tablistEntry = new TablistEntry("", getManager().getDefaultSkins().get(col, row), Config.TABLIST_PING);
            entries.put(new Pair<>(col, row), tablistEntry);
            return tablistEntry;
        }

        return entry;
    }

    public TablistSkin getOldSkin(int col, int row) {
        return oldSkinData.get(new Pair<>(col, row));
    }

    public void add(int col, int row, String text) {
        this.add(col, row, text, Config.TABLIST_PING);
    }

    public void add(int col, int row, String text, int ping) {
        this.add(col, row, null, text, ping);
    }

    public void add(int col, int row, TablistSkin skin, String text) {
        this.add(col, row, skin, text, Config.TABLIST_PING);
    }

    public void add(int col, int row, TablistSkin skin, String text, int ping) {
        if (skin == null) skin = getManager().getDefaultSkins().get(col, row);
        text = ChatUtil.translate(getInstance().getPlaceholderHook().replace(packet.getPlayer(), text));
        entries.put(new Pair<>(col, row), new TablistEntry(text, skin, ping));
    }
}