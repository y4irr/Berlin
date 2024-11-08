package dev.astro.net.tablist.adapter.impl;

import dev.astro.net.model.hcfcore.HCFCoreManager;
import dev.astro.net.model.server.IServerData;
import dev.astro.net.model.server.ServerDataManager;
import dev.astro.net.tablist.adapter.TabAdapter;
import dev.astro.net.tablist.setup.TabEntry;
import dev.astro.net.tablist.skin.custom.CustomSkin;
import dev.astro.net.tablist.util.Skin;
import dev.astro.net.utilities.ChatUtil;
import dev.astro.net.utilities.Comet;
import dev.astro.net.utilities.file.FileConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomTablistProvider implements TabAdapter {

    private final List<CustomSkin> customSkins = new ArrayList<>();
    private final FileConfig tabFile;
    private final HCFCoreManager hcfCoreManager;
    private final ServerDataManager serverDataManager;

    public CustomTablistProvider(Comet plugin) {
        this.tabFile = plugin.getTablistFile();
        this.hcfCoreManager = plugin.getHcfCoreManager();
        this.serverDataManager = plugin.getServerDataManager();
        loadCustomSkins(plugin.getTablistHeadsFile().getConfiguration());
    }

    private void loadCustomSkins(FileConfiguration config) {
        ConfigurationSection skinsSection = config.getConfigurationSection("heads");
        if (skinsSection != null) {
            skinsSection.getKeys(false).forEach(key -> {
                ConfigurationSection skinSection = skinsSection.getConfigurationSection(key);
                if (skinSection != null) {
                    customSkins.add(new CustomSkin(
                            key,
                            skinSection.getString("value", ""),
                            skinSection.getString("signature", "")
                    ));
                }
            });
        }
    }

    private Skin getCustomSkin(String name) {
        return customSkins.stream()
                .filter(skin -> skin.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private String replace(Player player, String text) {
        if (text == null || text.isEmpty()) return "";

        IServerData serverData = serverDataManager.getServerData();

        if (text.contains("-status>")) {
            String leftText = text.split("<")[0];
            String serverName = text.replace(leftText, "").replace("<", "").replace("-status>", "");
            String placeholderPath = "<" + serverName + "-status>";

            text = text.replace(placeholderPath,
                    serverData.isOffline(serverName) ? "&cOffline" :
                            serverData.isWhitelisted(serverName) ? "&eWhitelisted" :
                                    serverData.isMaintenance(serverName) ? "&cMaintenance" : "&aOnline");
        }

        text = text.replace("<player-name>", player.getName());

        if (hcfCoreManager.getHcf() != null && hcfCoreManager.getHcf().isConnected()) {
            text = text.replace("<hcfcore-hcf-kills>", String.valueOf(hcfCoreManager.getHcf().getKills(player.getUniqueId())))
                    .replace("<hcfcore-hcf-deaths>", String.valueOf(hcfCoreManager.getHcf().getDeaths(player.getUniqueId())))
                    .replace("<hcfcore-hcf-lives>", String.valueOf(hcfCoreManager.getHcf().getLives(player.getUniqueId())))
                    .replace("<hcfcore-hcf-deathban>", hcfCoreManager.getHcf().getDeathban(player.getUniqueId()));
        }

        if (hcfCoreManager.getKitmap() != null && hcfCoreManager.getKitmap().isConnected()) {
            text = text.replace("<hcfcore-kitmap-kills>", String.valueOf(hcfCoreManager.getKitmap().getKills(player.getUniqueId())))
                    .replace("<hcfcore-kitmap-deaths>", String.valueOf(hcfCoreManager.getKitmap().getDeaths(player.getUniqueId())))
                    .replace("<hcfcore-kitmap-killstreak>", String.valueOf(hcfCoreManager.getKitmap().getKillStreak(player.getUniqueId())));
        }

        return ChatUtil.placeholder(player, text);
    }

    @Override
    public String getHeader(Player player) {
        return replace(player, tabFile.getString("tablist.header"));
    }

    @Override
    public String getFooter(Player player) {
        return replace(player, tabFile.getString("tablist.footer"));
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> entries = new ObjectArrayList<>();
        addTabEntries(entries, "left", 0, player);
        addTabEntries(entries, "middle", 1, player);
        addTabEntries(entries, "right", 2, player);
        addTabEntries(entries, "far_right", 3, player);

        customSkins.add(new CustomSkin("player", Skin.getPlayer(player).getValue(), Skin.getPlayer(player).getSignature()));

        return entries;
    }

    private void addTabEntries(List<TabEntry> entries, String section, int column, Player player) {
        ConfigurationSection tabSection = tabFile.getConfiguration().getConfigurationSection("tablist.lines." + section);
        if (tabSection != null) {
            tabSection.getKeys(false).forEach(tabRow -> {
                int row = Integer.parseInt(tabRow) - 1;
                String text = tabFile.getString("tablist.lines." + section + "." + tabRow + ".text");
                String skin = tabFile.getString("tablist.lines." + section + "." + tabRow + ".head");
                entries.add(new TabEntry(column, row, replace(player, ChatUtil.translate(text)))
                        .setPing(-1)
                        .setSkin(getCustomSkin(skin)));
            });
        }
    }
}
