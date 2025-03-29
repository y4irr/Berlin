package net.cyruspvp.hub.nametags.adapter;

import net.cyruspvp.hub.nametags.Nametag;
import net.cyruspvp.hub.nametags.NametagAdapter;
import net.cyruspvp.hub.nametags.NametagManager;
import net.cyruspvp.hub.nametags.extra.NameVisibility;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.entity.Player;

/**
 * Copyright (c) 2025. Keano
 * Use or redistribution of source or file is
 * only permitted if given explicit permission.
 */
public class AzuriteNametags extends Module<NametagManager> implements NametagAdapter {

    public AzuriteNametags(NametagManager manager) {
        super(manager);
    }

    @Override
    public String getAndUpdate(Player player, Player target) {
        return createTeam(player, target, target.getName(), "", ChatUtil.translate(getInstance().getRankManager().getRank().getSuffix(target.getUniqueId())), NameVisibility.ALWAYS);
    }

    // yes it needs color because 1.16 is stupid
    private String createTeam(Player player, Player target, String name, String prefix, String color, NameVisibility visibility) {
        Nametag nametag = getManager().getNametags().get(player.getUniqueId());
        String formattedPrefix = (prefix.isEmpty() ? "" : prefix);

        if (nametag != null) {
            nametag.getPacket().create(name, color, formattedPrefix, "", true, visibility);
            nametag.getPacket().addToTeam(target, name);
        }

        return formattedPrefix + color;
    }
}