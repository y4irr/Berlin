package net.cyruspvp.hub.model.hotbar.normal.types;

import net.cyruspvp.hub.Berlin;
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
        for (String line : Berlin.getPlugin().getHotbarFile().getStringList("normal.information.message")) {
            player.sendMessage(ChatUtil.translate(line));
        }
        PlayerUtil.playSound(player, "CLICK");
        NormalHotbar InformationHotbar = normalHotbarManager.getHotbar("information");
        player.getInventory().setItem(InformationHotbar.getItemSlot(), InformationHotbar.getItem(player));
    }
}