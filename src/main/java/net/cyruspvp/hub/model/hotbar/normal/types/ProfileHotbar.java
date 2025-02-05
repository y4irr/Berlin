package net.cyruspvp.hub.model.hotbar.normal.types;

import net.cyruspvp.hub.model.hotbar.normal.NormalHotbar;
import net.cyruspvp.hub.profile.ProfileMenu;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.utilities.PlayerUtil;
import org.bukkit.entity.Player;

public class ProfileHotbar extends NormalHotbar {

    private final Berlin plugin;

    public ProfileHotbar(Berlin plugin, String name) {
        super(name);

        this.plugin = plugin;
    }

    @Override
    public void onItemInteract(Player player) {
        new ProfileMenu(plugin.getPlugin(), player).openMenu(player);
        PlayerUtil.playSound(player, "NOTE_BASS");
    }
}