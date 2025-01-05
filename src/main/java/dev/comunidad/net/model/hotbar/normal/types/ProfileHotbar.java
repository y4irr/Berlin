package dev.comunidad.net.model.hotbar.normal.types;

import dev.comunidad.net.model.hotbar.normal.NormalHotbar;
import dev.comunidad.net.profile.ProfileMenu;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.utilities.PlayerUtil;
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