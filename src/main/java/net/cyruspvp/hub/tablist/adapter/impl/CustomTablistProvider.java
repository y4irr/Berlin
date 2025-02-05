package net.cyruspvp.hub.tablist.adapter.impl;

import net.cyruspvp.hub.model.hcfcore.HCFCoreManager;
import net.cyruspvp.hub.model.server.IServerData;
import net.cyruspvp.hub.model.server.ServerDataManager;
import net.cyruspvp.hub.tablist.adapter.TabAdapter;
import net.cyruspvp.hub.tablist.setup.TabEntry;
import net.cyruspvp.hub.tablist.skin.custom.CustomSkin;
import net.cyruspvp.hub.tablist.util.Skin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.utilities.file.FileConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomTablistProvider implements TabAdapter {

    private final List<CustomSkin> customSkins;

    private final FileConfig tabFile;
    private final HCFCoreManager hcfCoreManager;
    private final ServerDataManager serverDataManager;

    public CustomTablistProvider(Berlin plugin) {
        this.tabFile = plugin.getTablistFile();
        this.hcfCoreManager = plugin.getHcfCoreManager();
        this.serverDataManager = plugin.getServerDataManager();

        this.customSkins = new ArrayList<>();

        this.loadCustomSkins(plugin.getTablistHeadsFile().getConfiguration());
    }

    private void loadCustomSkins(FileConfiguration config) {
        if (config.contains("heads")) {
            ConfigurationSection skinsSection = config.getConfigurationSection("heads");
            for (String key : skinsSection.getKeys(false)) {
                ConfigurationSection skinSection = skinsSection.getConfigurationSection(key);
                String value = skinSection.getString("value");
                String signature = skinSection.getString("signature");
                this.customSkins.add(new CustomSkin(key, value, signature));
            }
        }
    }

    private Skin getCustomSkin(Player player, String name) {
        return this.customSkins.stream()
                .filter(skin -> skin.getName().equalsIgnoreCase(name.replace("%player%", player.getName())))
                .findFirst()
                .orElse(null);
    }

    protected String replace(Player player, String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (text.contains("-status>")) {
            String leftText = text.split("<")[0];
            String serverName = text
                    .replace(leftText, "")
                    .replace("<", "")
                    .replace("-status>", "");

            IServerData serverData = serverDataManager.getServerData();
            String placeholderPath = "<" + serverName + "-status>";

            if (serverData.isOffline(serverName)) return text
                    .replace(placeholderPath, "&cOffline");
            else if (serverData.isWhitelisted(serverName)) return text
                    .replace(placeholderPath, "&eWhitelist");
            else if (serverData.isMaintenance(serverName)) return text
                    .replace(placeholderPath, "&cMaintenance");
            else return text.replace(placeholderPath, "&aOnline");
        }

        if (text.contains("<player-name>")) {
            text = text.replace("<player-name>", player.getName());
        }

        if (hcfCoreManager.getHcf() != null && hcfCoreManager.getHcf().isConnected()) {
            text = text
                    .replace("<hcfcore-hcf-kills>", String.valueOf(hcfCoreManager.getHcf().getKills(player.getUniqueId())))
                    .replace("<hcfcore-hcf-deaths>", String.valueOf(hcfCoreManager.getHcf().getDeaths(player.getUniqueId())))
                    .replace("<hcfcore-hcf-lives>", String.valueOf(hcfCoreManager.getHcf().getLives(player.getUniqueId())))
                    .replace("<hcfcore-hcf-deathban>", hcfCoreManager.getHcf().getDeathban(player.getUniqueId()));
        }
        if (hcfCoreManager.getKitmap() != null && hcfCoreManager.getKitmap().isConnected()) {
            text = text
                    .replace("<hcfcore-kitmap-kills>", String.valueOf(hcfCoreManager.getKitmap().getKills(player.getUniqueId())))
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

        tabFile.getConfiguration().getConfigurationSection("tablist.lines.left").getKeys(false)
                .forEach(tabRow -> {
                    int row = Integer.parseInt(tabRow) - 1;
                    String text = tabFile.getString("tablist.lines.left." + tabRow + ".text");
                    String skin = tabFile.getString("tablist.lines.left." + tabRow + ".head");
                    entries.add(new TabEntry(0, row, replace(player, ChatUtil.translate(text))).setPing(-1)
                            .setSkin(getCustomSkin(player, skin)));
                });

        tabFile.getConfiguration().getConfigurationSection("tablist.lines.middle").getKeys(false)
                .forEach(tabRow -> {
                    int row = Integer.parseInt(tabRow) - 1;
                    String text = tabFile.getString("tablist.lines.middle." + tabRow + ".text");
                    String skin = tabFile.getString("tablist.lines.middle." + tabRow + ".head");
                    entries.add(new TabEntry(1, row, replace(player, ChatUtil.translate(text))).setPing(-1)
                            .setSkin(getCustomSkin(player, skin)));
                });

        tabFile.getConfiguration().getConfigurationSection("tablist.lines.right").getKeys(false)
                .forEach(tabRow -> {
                    int row = Integer.parseInt(tabRow) - 1;
                    String text = tabFile.getString("tablist.lines.right." + tabRow + ".text");
                    String skin = tabFile.getString("tablist.lines.right." + tabRow + ".head");
                    entries.add(new TabEntry(2, row, replace(player, ChatUtil.translate(text))).setPing(-1)
                            .setSkin(getCustomSkin(player, skin)));
                });

        tabFile.getConfiguration().getConfigurationSection("tablist.lines.far_right").getKeys(false)
                .forEach(tabRow -> {
                    int row = Integer.parseInt(tabRow) - 1;
                    String text = tabFile.getString("tablist.lines.far_right." + tabRow + ".text");
                    String skin = tabFile.getString("tablist.lines.far_right." + tabRow + ".head");
                    entries.add(new TabEntry(3, row, replace(player, ChatUtil.translate(text))).setPing(-1)
                            .setSkin(getCustomSkin(player, skin)));
                });

        this.customSkins.add(new CustomSkin(player.getName(), Skin.getPlayer(player).getValue(), Skin.getPlayer(player).getSignature()));

        return entries;
    }
}