package net.cyruspvp.hub.model.hotbar.normal.types;

import net.cyruspvp.hub.model.hotbar.normal.NormalHotbar;
import net.cyruspvp.hub.model.hotbar.normal.NormalHotbarManager;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.PlayerUtil;
import org.bukkit.entity.Player;

public class InformationHotbar extends NormalHotbar {

    private final NormalHotbarManager normalHotbarManager;

    public InformationHotbar(String name, NormalHotbarManager normalHotbarManager) {
        super(name);
        this.normalHotbarManager = normalHotbarManager;
    }

    @Override
    public void onItemInteract(Player player) {
        player.sendMessage(ChatUtil.translate(new String[]{
                "&7&m------------------------",
                "&d&lServer Information",
                "",
                "&aStore&7: &fstore.cyruspvp.net",
                "&9Discord&7: &fdiscord.cyruspvp.net",
                "&bTeamSpeak&7: &fts.cyruspvp.net",
                "&7&m------------------------"
        }));
        PlayerUtil.playSound(player, "CLICK");
        NormalHotbar InformationHotbar = normalHotbarManager.getHotbar("information");
        player.getInventory().setItem(InformationHotbar.getItemSlot(), InformationHotbar.getItem(player));
    }
}