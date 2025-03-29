package net.cyruspvp.hub.utilities.versions;

import net.cyruspvp.hub.tablist.extra.TablistSkin;
import net.cyruspvp.hub.utilities.Logger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;


public interface Version {

    CommandMap getCommandMap();

    Set<Player> getTrackedPlayers(Player player);

    ItemStack getItemInHand(Player player);

    ItemStack addGlow(ItemStack itemStack);

    String getTPSColored();

    int getPing(Player player);

    void setItemInHand(Player player, ItemStack item);

    void playEffect(Location location, String effect, Object data);

    void hideArmor(Player player);

    void showArmor(Player player);

    void handleNettyListener(Player player);

    void sendToServer(Player player, String server);

    void damageItemDefault(Player player, ItemStack hand);

    void clearArrows(Player player);

    List<ItemStack> getBlockDrops(Player player, Block bl, ItemStack hand);

    TablistSkin getSkinData(Player player);
}