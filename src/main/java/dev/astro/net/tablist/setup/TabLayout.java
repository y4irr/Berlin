package dev.astro.net.tablist.setup;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.astro.net.tablist.TablistHandler;
import dev.astro.net.tablist.adapter.TabAdapter;
import dev.astro.net.tablist.util.PacketUtils;
import dev.astro.net.tablist.util.Skin;
import dev.astro.net.tablist.util.StringUtils;

import java.util.*;

@Log4j2
public class TabLayout {

    public static String[] TAB_NAMES = new String[80];

    private final Map<Integer, TabEntryInfo> entryMapping = new Int2ObjectArrayMap<>();

    /**
     * {@link Integer Mod} is the modification integer
     * used to determine rows/columns from index of the {@link TabEntry}.
     */
    @Getter private final int mod;
    /**
     * {@link Integer Max Entries} is 60 for 1.7 clients and lower
     * whilst for 1.8+ is 80.
     */
    @Getter private final int maxEntries;
    /**
     * The player associated with this {@link TabLayout}
     */
    @Getter private final Player player;
    /**
     * We need to send a player list name update instantly to
     * modern clients on first join, otherwise their tablist shows
     * up with white text "null" until next update is triggered.
     */
    private boolean isFirstJoin = true;

    public TabLayout(Player player) {
        this.mod = PacketUtils.isLegacyClient(player) ? 3 : 4;
        this.maxEntries = PacketUtils.isLegacyClient(player) ? 60 : 80;
        this.player = player;
    }

    /**
     * Create and initialize this {@link TabLayout} client side
     */
    public void create() {
        PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
        ServerManager manager = packetEvents.getServerManager();

        if (manager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> dataList = new ArrayList<>();
            for ( int index = 0; index < maxEntries; index++ ) {
                int x = index % mod;
                int y = index / mod;
                int i = y * mod + x;

                UserProfile gameProfile = this.generateProfile(i);
                TabEntryInfo info = new TabEntryInfo(gameProfile);
                this.entryMapping.put(i, info);

                dataList.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile,
                        true,
                        0,
                        GameMode.SURVIVAL,
                        !PacketUtils.isLegacyClient(player) ? AdventureSerializer.fromLegacyFormat(this.getTeamAt(i)) : null,
                        null));
            }

            WrapperPlayServerPlayerInfoUpdate packetInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, dataList);
            WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, dataList);
            WrapperPlayServerPlayerInfoUpdate gamemode = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE, dataList);

            this.sendPacket(packetInfo);
            if (!PacketUtils.isLegacyClient(player)) {
                WrapperPlayServerPlayerInfoUpdate display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, dataList);
                this.sendPacket(display);
            }
            this.sendPacket(list);
            this.sendPacket(gamemode);
        } else {
            WrapperPlayServerPlayerInfo packetInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER);
            List<WrapperPlayServerPlayerInfo.PlayerData> dataList = packetInfo.getPlayerDataList();

            for ( int index = 0; index < maxEntries; index++ ) {
                int x = index % mod;
                int y = index / mod;
                int i = y * mod + x;

                UserProfile gameProfile = this.generateProfile(i);
                TabEntryInfo info = new TabEntryInfo(gameProfile);
                this.entryMapping.put(i, info);

                dataList.add(new WrapperPlayServerPlayerInfo.PlayerData(
                        !PacketUtils.isLegacyClient(player) ? AdventureSerializer.fromLegacyFormat(this.getTeamAt(i)) : null,
                        gameProfile,
                        GameMode.SURVIVAL,
                        0)
                );
            }

            this.sendPacket(packetInfo);
        }

        // Add everyone to the "Tab" team
        // These aren't really used for 1.17+ except for hiding our own name
        Team bukkitTeam = player.getScoreboard().getTeam("tab");
        if (bukkitTeam == null) {
            bukkitTeam = player.getScoreboard().registerNewTeam("tab");
        }

        Bukkit.getOnlinePlayers().stream().filter(Objects::nonNull).map(Player::getName).forEach(bukkitTeam::addEntry);

        // Add them to their own team so that our own name doesn't show up
        for ( int index = 0; index < maxEntries; index++ ) {
            String displayName = getTeamAt(index);
            String team = "$" + displayName;

            Team scoreboardTeam = player.getScoreboard().getTeam(team);
            if (scoreboardTeam == null) {
                scoreboardTeam = player.getScoreboard().registerNewTeam(team);
                scoreboardTeam.addEntry(displayName);
            }
        }

        for ( Player target : Bukkit.getOnlinePlayers() ) {
            Team team = target.getScoreboard().getTeam("tab");
            if (team == null) continue;

            team.addEntry(player.getName());
        }
    }

    /**
     * Send Header and Footer to the Client but
     * only send it if we aren't on 1.7 and below.
     */
    public void setHeaderAndFooter() {
        if (PacketUtils.isLegacyClient(player)) return;

        TabAdapter tablistAdapter = TablistHandler.getInstance().getAdapter();
        if (tablistAdapter == null) return;

        String header = StringUtils.color(tablistAdapter.getHeader(player));
        String footer = StringUtils.color(tablistAdapter.getFooter(player));

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = new WrapperPlayServerPlayerListHeaderAndFooter(
                AdventureSerializer.fromLegacyFormat(header),
                AdventureSerializer.fromLegacyFormat(footer)
        );

        this.sendPacket(headerAndFooter);
    }

    /**
     * Update the text, skin and ping for the specified Tablist Entry with index
     *
     * @param index {@link Integer Entry index}
     * @param text  {@link String Entry Text}
     * @param ping  {@link Integer Latency}
     * @param skin  {@link Skin Entry Skin}
     */
    public void update(int index, String text, int ping, Skin skin) {
        if (PacketUtils.isLegacyClient(player) && index >= 60) {
            return;
        }

        String[] splitString = StringUtils.split(text);
        text = StringUtils.color(text);

        String prefix = StringUtils.color(splitString[0]);
        String suffix = StringUtils.color(splitString[1]);

        String displayName = getTeamAt(index);
        String team = "$" + displayName;

        TabEntryInfo entry = this.entryMapping.get(index);
        if (entry == null) return;

        boolean changed = false;
        if (!prefix.equals(entry.getPrefix())) {
            entry.setPrefix(prefix);
            changed = true;
        }

        if (!suffix.equals(entry.getSuffix())) {
            entry.setSuffix(suffix);
            changed = true;
        }

        // 1.7 and below support
        if (PacketUtils.isLegacyClient(player)) {
            Scoreboard scoreboard = player.getScoreboard();
            Team bukkitTeam = scoreboard.getTeam(team);
            boolean teamExists = bukkitTeam != null;

            // This is a new entry, make it's team
            if (bukkitTeam == null) {
                bukkitTeam = scoreboard.registerNewTeam(team);
                bukkitTeam.addEntry(displayName);
            }

            if (changed || !teamExists) {
                bukkitTeam.setPrefix(prefix);
                bukkitTeam.setSuffix(suffix);
            }
            this.updatePing(entry, ping);

        // 1.8 to 1.20+ support
        } else {
            // So basically updating the skin automatically causes an update
            // to the display name of the fake player, so updating below is just idiotic.
            boolean updated = this.updateSkin(entry, skin, text);
            this.updatePing(entry, ping);

            if (!updated && (changed || this.isFirstJoin)) {
                this.updateDisplayName(entry, text.length() == 0 ?  this.getTeamAt(index) : text);

                if (this.isFirstJoin) {
                    this.isFirstJoin = false;
                }
            }
        }
    }

    /**
     * Update the {@link TabEntry}'s ping
     *
     * @param info {@link TabEntryInfo info}
     * @param ping {@link Integer ping}
     */
    private void updatePing(TabEntryInfo info, int ping) {
        PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        // Only send if changed
        int lastConnection = info.getPing();
        if (lastConnection == ping) {
            return;
        }
        info.setPing(ping);

        UserProfile gameProfile = info.getProfile();
        PacketWrapper<?> playerInfo;
        if (isClientNew) {
            playerInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY,
                    new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, true, ping, GameMode.SURVIVAL, null, null)
            );
        } else {
            playerInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_LATENCY,
                    new WrapperPlayServerPlayerInfo.PlayerData(null, gameProfile, GameMode.SURVIVAL, ping)
            );
        }

        this.sendPacket(playerInfo);
    }

    /**
     * Update the {@link TabEntry}'s Skin
     *
     * @param info {@link TabEntryInfo info}
     * @param skin {@link Skin skin}
     */
    private boolean updateSkin(TabEntryInfo info, Skin skin, String text) {
        PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
        ServerManager serverManager = packetEvents.getServerManager();
        boolean isServerNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        if (skin == null) {
            skin = Skin.DEFAULT_SKIN;
        }

        // Only send if changed
        Skin lastSkin = info.getSkin();
        if (skin.equals(lastSkin)) {
            return false;
        }

        info.setSkin(skin);

        UserProfile userProfile = info.getProfile();
        TextureProperty textureProperty = new TextureProperty("textures", skin.getValue(), skin.getSignature());
        userProfile.setTextureProperties(Collections.singletonList(textureProperty));

        int ping = info.getPing();

        PacketWrapper<?> playerInfoRemove = null;

        if (isServerNew) {
            playerInfoRemove = new WrapperPlayServerPlayerInfoRemove(userProfile.getUUID());
        } else {
            playerInfoRemove = new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
                    new WrapperPlayServerPlayerInfo.PlayerData(null, userProfile, GameMode.SURVIVAL, ping)
            );
        }

        if (isServerNew) {
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                    userProfile,
                    true,
                    ping,
                    GameMode.SURVIVAL,
                    !PacketUtils.isLegacyClient(player) ? AdventureSerializer.fromLegacyFormat(text) : null,
                    null
            );

            WrapperPlayServerPlayerInfoUpdate add = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, data);
            WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, data);

            if (PacketUtils.isModernClient(player)) {
                this.sendPacket(playerInfoRemove);
            }

            this.sendPacket(add);
            this.sendPacket(list);

            if (!PacketUtils.isLegacyClient(player)) {
                WrapperPlayServerPlayerInfoUpdate display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, data);
                this.sendPacket(display);
            }
        } else {
            WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                    !PacketUtils.isLegacyClient(player) ? AdventureSerializer.fromLegacyFormat(text) : null,
                    userProfile,
                    GameMode.SURVIVAL,
                    ping
            );

            PacketWrapper<?> playerInfoAdd = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, data);
            if (PacketUtils.isModernClient(player)) {
                this.sendPacket(playerInfoRemove);
            }
            this.sendPacket(playerInfoAdd);
        }

        return true;
    }

    /**
     * Send a {@link PacketWrapper Packet} to this Layout's Player.
     * This method is null safe as opposed to {@link PacketUtils#sendPacket(Player, PacketWrapper)}.
     *
     * @param packetWrapper {@link PacketWrapper packet}
     */
    private void sendPacket(PacketWrapper<?> packetWrapper) {
        if (player == null) return; // Null Safety

        PacketUtils.sendPacket(player, packetWrapper);
    }

    /**
     * Generate a {@link UserProfile} for the specified index.
     *
     * @param index {@link Integer Index}
     * @return      {@link UserProfile Profile}
     */
    private UserProfile generateProfile(int index) {
        Skin defaultSkin = Skin.DEFAULT_SKIN;

        UserProfile gameProfile = new UserProfile(UUID.randomUUID(), getTeamAt(index));
        TextureProperty textureProperty = new TextureProperty("textures", defaultSkin.getValue(), defaultSkin.getSignature());
        gameProfile.setTextureProperties(Collections.singletonList(textureProperty));

        return gameProfile;
    }

    /**
     * The normal way to make a tablist is to use {@link Team}s.<br>
     * <br>
     * In modern versions (1.16+), this is broken due to whitespace trimming.
     * We instead use PlayerList name or Display name of the {@link Player}
     * to display the tablist.<br>
     * <br>
     * It's also beneficial to use this for 1.8 because then 48 is the limit.
     *
     * @param entry {@link TabEntryInfo entry}
     */
    private void updateDisplayName(TabEntryInfo entry, String text) {
        PacketEventsAPI<?> packetEvents = PacketEvents.getAPI();
        ServerManager manager = packetEvents.getServerManager();

        UserProfile profile = entry.getProfile();
        PacketWrapper<?> display;

        if (manager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                    profile,
                    true,
                    0,
                    null,
                    AdventureSerializer.fromLegacyFormat(text),
                    null
            );
            display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, data);
        } else {
            WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                    AdventureSerializer.fromLegacyFormat(text),
                    profile,
                    null,
                    0
            );
            display = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_DISPLAY_NAME, data);
        }
        this.sendPacket(display);
    }

    public String getTeamAt(int index) {
        return TAB_NAMES[index];
    }

    // Credits: Scifi
    static {
        for ( int i = 0; i < TAB_NAMES.length; i++ ) {
            int x = i % 4;
            int y = i / 4;
            String name = "§0§" + x
                    + (y > 9 ? "§" + String.valueOf(y).toCharArray()[0]
                    + "§" + String.valueOf(y).toCharArray()[1] :
                    "§0§" + String.valueOf(y).toCharArray()[0])
                    + ChatColor.RESET;
            TAB_NAMES[i] = name;
        }

    }
}
