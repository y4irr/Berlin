package net.cyruspvp.hub.tablist.adapter;

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.tablist.Tablist;
import net.cyruspvp.hub.tablist.TablistAdapter;
import net.cyruspvp.hub.tablist.TablistManager;
import net.cyruspvp.hub.tablist.extra.TablistData;
import net.cyruspvp.hub.tablist.extra.TablistPlaceholder;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.utilities.extra.Triple;
import net.cyruspvp.hub.utilities.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AzuriteTablist extends Module<TablistManager> implements TablistAdapter {

    private final String[] header;
    private final String[] footer;

    private final List<String> leftTablist;
    private final List<String> middleTablist;
    private final List<String> rightTablist;
    private final List<String> farRightTablist;

    private final Triple<Integer, Integer, List<TablistPlaceholder>> placeholders;

    public AzuriteTablist(TablistManager manager) {
        super(manager);

        this.header = getTablistConfig().getStringList("TABLIST_INFO.HEADER").toArray(new String[0]);
        this.footer = getTablistConfig().getStringList("TABLIST_INFO.FOOTER").toArray(new String[0]);

        this.leftTablist = getTablistConfig().getStringList("LEFT");
        this.middleTablist = getTablistConfig().getStringList("MIDDLE");
        this.rightTablist = getTablistConfig().getStringList("RIGHT");
        this.farRightTablist = getTablistConfig().getStringList("FAR_RIGHT");

        this.placeholders = new Triple<>();
        this.load();
    }

    private void load() {
        // This will split the skins to the text itself.
        for (int row = 0; row < 20; row++) {
            String[] left = leftTablist.get(row).split(";");
            leftTablist.set(row, (left.length == 1 ? "" : left[1]));
            cachePlaceholders(0, row, (left.length == 1 ? "" : left[1]));

            String[] middle = middleTablist.get(row).split(";");
            middleTablist.set(row, (middle.length == 1 ? "" : middle[1]));
            cachePlaceholders(1, row, (middle.length == 1 ? "" : middle[1]));

            String[] right = rightTablist.get(row).split(";");
            rightTablist.set(row, (right.length == 1 ? "" : right[1]));
            cachePlaceholders(2, row, (right.length == 1 ? "" : right[1]));

            String[] farRight = farRightTablist.get(row).split(";");
            farRightTablist.set(row, (farRight.length == 1 ? "" : farRight[1]));
            cachePlaceholders(3, row, (farRight.length == 1 ? "" : farRight[1]));
        }
    }

    private void cachePlaceholders(int col, int row, String string) {
        List<TablistPlaceholder> list = new ArrayList<>();

        if (string.contains("%online%")) {
            list.add((data, line, add) -> {
                line = line.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));
                if (add) data.getInfo().add(col, row, line);
                return line;
            });
        }

        if (string.contains("%max-online%")) {
            list.add((data, line, add) -> {
                line = line.replace("%max-online%", String.valueOf(Bukkit.getMaxPlayers()));
                if (add) data.getInfo().add(col, row, line);
                return line;
            });
        }

        if (string.contains("%title%")) {
            list.add((data, line, add) -> {
                line = line.replace("%title%", getManager().getTitle().getCurrent());
                if (add) data.getInfo().add(col, row, line);
                return line;
            });
        }

        if (!list.isEmpty()) {
            placeholders.put(col, row, list);
        }
    }

    @Override
    public String[] getHeader(Player player) {
        String[] finalHeader = header.clone();

        // Need to do some replacing
        for (int i = 0; i < finalHeader.length; i++) {
            String string = finalHeader[i];
            finalHeader[i] = getInstance().getPlaceholderHook().replace(player, string)
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("%max-online%", String.valueOf(Bukkit.getMaxPlayers()))
                    .replace("%title%", getManager().getTitle().getCurrent());
        }

        return finalHeader;
    }

    @Override
    public String[] getFooter(Player player) {
        String[] finalFooter = footer.clone();

        // Need to do some replacing
        for (int i = 0; i < finalFooter.length; i++) {
            String string = finalFooter[i];
            finalFooter[i] = getInstance().getPlaceholderHook().replace(player, string)
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("%max-online%", String.valueOf(Bukkit.getMaxPlayers()))
                    .replace("%title%", getManager().getTitle().getCurrent());
        }

        return finalFooter;
    }

    @Override
    public Tablist getInfo(Player player) {
        Tablist info = getManager().getTablists().get(player.getUniqueId());
        User user = getInstance().getUserManager2().getByUUID(player.getUniqueId());
        TablistData data = new TablistData(info, user, player);

        for (int row = 0; row < 20; row++) {
            String left = leftTablist.get(row);
            String middle = middleTablist.get(row);
            String right = rightTablist.get(row);
            String farRight = farRightTablist.get(row);

            List<TablistPlaceholder> leftPlaceholders = placeholders.get(0, row);
            List<TablistPlaceholder> middlePlaceholders = placeholders.get(1, row);
            List<TablistPlaceholder> rightPlaceholders = placeholders.get(2, row);
            List<TablistPlaceholder> farRightPlaceholders = placeholders.get(3, row);

            this.addEntry(0, row, left, leftPlaceholders, data);
            this.addEntry(1, row, middle, middlePlaceholders, data);
            this.addEntry(2, row, right, rightPlaceholders, data);
            this.addEntry(3, row, farRight, farRightPlaceholders, data);
        }

        return info;
    }

    private void addEntry(int col, int row, String line, List<TablistPlaceholder> placeholders, TablistData data) {
        if (line.isEmpty()) return;

        boolean add = false;

        if (placeholders != null) {
            for (int i = 0; i < placeholders.size(); i++) {
                TablistPlaceholder placeholder = placeholders.get(i);

                if (i == placeholders.size() - 1) {
                    add = true;
                }

                line = placeholder.replaceOrAdd(data, line, add);
            }
        }

        if (!add) {
            data.getInfo().add(col, row, line);
        }
    }
}