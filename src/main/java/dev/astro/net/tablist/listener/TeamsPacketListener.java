package dev.astro.net.tablist.listener;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import dev.astro.net.tablist.TablistHandler;
import dev.astro.net.tablist.util.PacketUtils;

import java.util.EnumSet;

/**
 * <p>
 * This class is essentially used to fix plugins adding/removing
 * entities via the player info packet for various reasons like Disguise or
 * Skin change. This intercepts those packets and makes them compatible with our tab.
 * </p>
 * <br>
 * <p>
 * This Project is property of Refine Development.<br>
 * Copyright © 2023, All Rights Reserved.<br>
 * Redistribution of this Project is not allowed.<br>
 * </p>
 *
 * @author DevScifi/DevDrizzy
 * @version TablistAPI
 * @since 10/15/2023
 */

@RequiredArgsConstructor
public class TeamsPacketListener extends PacketListenerAbstract {

    private final PacketEventsAPI<?> packetEvents;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO &&
                event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE) return;

        Player player = (Player) event.getPlayer();
        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE && isClientNew) {
            handlePlayerInfoUpdate(new WrapperPlayServerPlayerInfoUpdate(event), player);
        } else {
            handlePlayerInfo(new WrapperPlayServerPlayerInfo(event), player);
        }
    }

    private void handlePlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate infoUpdate, Player player) {
        if (!infoUpdate.getActions().contains(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER)) return;

        infoUpdate.getEntries().stream()
                .map(WrapperPlayServerPlayerInfoUpdate.PlayerInfo::getGameProfile)
                .forEach(userProfile -> {
                    if (userProfile != null) preventGlitch(player, userProfile);
                });
    }

    private void handlePlayerInfo(WrapperPlayServerPlayerInfo infoPacket, Player player) {
        if (infoPacket.getAction() != WrapperPlayServerPlayerInfo.Action.ADD_PLAYER) return;

        infoPacket.getPlayerDataList().stream()
                .map(WrapperPlayServerPlayerInfo.PlayerData::getUserProfile)
                .forEach(userProfile -> {
                    if (userProfile != null) preventGlitch(player, userProfile);
                });
    }

    /**
     * Prevents our tablist from glitching out and breaking
     *
     * @param player      {@link Player} Player
     * @param userProfile {@link UserProfile} Profile
     */

    private void preventGlitch(Player player, UserProfile userProfile) {
        Player onlinePlayer = Bukkit.getPlayer(userProfile.getUUID());
        if (onlinePlayer == null) return;

        if (PacketUtils.isLegacyClient(player)) {
            handleLegacyClient(player, userProfile);
        } else {
            addPlayerToTabTeam(player, onlinePlayer.getName());
        }
    }

    private void handleLegacyClient(Player player, UserProfile userProfile) {
        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        PacketWrapper<?> removePacket = isClientNew ?
                new WrapperPlayServerPlayerInfoRemove(userProfile.getUUID()) :
                new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
                        new WrapperPlayServerPlayerInfo.PlayerData(null, userProfile, GameMode.SURVIVAL, -1));

        Bukkit.getScheduler().runTask(TablistHandler.getInstance().getPlugin(), () -> PacketUtils.sendPacket(player, removePacket));
    }

    private void addPlayerToTabTeam(Player player, String playerName) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("tab");

        if (team == null) {
            team = scoreboard.registerNewTeam("tab");
            Team finalTeam = team;
            Bukkit.getOnlinePlayers().forEach(p -> finalTeam.addEntry(p.getName()));
        }

        team.addEntry(playerName);
    }
}
