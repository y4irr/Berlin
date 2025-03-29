package net.cyruspvp.hub.listeners;

import me.zowpy.core.api.CoreAPI;
import net.cyruspvp.hub.model.rank.RankManager;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.PlayerUtil;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.cosmetics.ParticleUtils;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

//import static net.cyruspvp.hub.utilities.actionbar.ActionBarAPI.sendActionBar;

public class PlayerListener implements Listener {

	private final Berlin plugin;
	private final RankManager rankManager;

	public PlayerListener(Berlin plugin) {
		this.plugin = plugin;
		this.rankManager = plugin.getRankManager();
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		float walkSpeed = (float) plugin.getConfigFile().getDouble("Player-Join.Speed");
		String soundName = plugin.getConfigFile().getString("Player-Join.Sound");

		Sound joinSound = null;
		try {
			joinSound = Sound.valueOf(soundName);
		} catch (IllegalArgumentException | NullPointerException e) {
			plugin.getLogger().warning("Invalid Sound! You are using " + soundName);
		}

		if (joinSound != null) {
			Sound finalJoinSound = joinSound;
			Bukkit.getScheduler().runTaskLater(Berlin.getPlugin(), () -> player.playSound(player.getLocation(), finalJoinSound, 1.0f, 1.0f), 20L);
		}

		player.setWalkSpeed(walkSpeed);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0.0f);
		player.setLevel(0);
		player.setGameMode(GameMode.ADVENTURE);
		giveArmor(player);
	}


	@EventHandler
	private void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(!event.getPlayer().hasMetadata("Berlin-build-mode"));
	}

	@EventHandler
	private void onPlayerItemPickup(PlayerPickupItemEvent event) {
		event.setCancelled(!event.getPlayer().hasMetadata("Berlin-build-mode"));
	}

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		event.setCancelled(!event.getWhoClicked().hasMetadata("Berlin-build-mode"));
	}

	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.getConfigFile().getBoolean("double-jump.enabled")) {
			if (player.getGameMode() == GameMode.CREATIVE) {
				player.setAllowFlight(true);
				return;
			}

			if (!player.getAllowFlight()) {
				player.setAllowFlight(true);
			}
		}
	}

	@EventHandler
	private void onPlayerDoubleJump(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.getConfigFile().getBoolean("double-jump.enabled")) {
			if (player.getGameMode() == GameMode.CREATIVE || player.isFlying()) {
				return;
			}

			event.setCancelled(true);
			player.setVelocity(player.getLocation().getDirection().multiply(plugin.getConfigFile().getDouble("double-jump.velocity")).setY(1));
			PlayerUtil.playSound(player, plugin.getConfigFile().getString("double-jump.sound"));
		}
		createAuraEffect(player);
	}

	private void createAuraEffect(Player player) {
		Location location = player.getLocation();
		String effectName = plugin.getConfigFile().getString("double-jump.effect");
		EnumParticle particleffect;

		try {
			particleffect = EnumParticle.valueOf(effectName.toUpperCase());
		} catch (IllegalArgumentException e) {
			particleffect = EnumParticle.FLAME;
			ChatUtil.logger("Invalid particle effect '" + effectName + "' in config.yml. Using FLAME as default.");
		}

		for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 8) {
			double x = Math.cos(angle) * 1;
			double z = Math.sin(angle) * 1;

			Location particleLocation = location.clone().add(x, 0.5, z);
			ParticleUtils.spawnParticle(
					player,
					particleffect,
					particleLocation.getX(),
					particleLocation.getY(),
					particleLocation.getZ(),
					0.1F, 0.1F, 0.1F,
					0.05F,
					5
			);
		}
	}

	@EventHandler
	private void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		event.setDroppedExp(0);
		event.getDrops().clear();
	}

	private void giveArmor(Player player) {
		String player1 = rankManager.getRank().getName(player.getUniqueId());

		ItemStack chest = new ItemBuilder(Material.LEATHER_CHESTPLATE)
				.setName(ChatUtil.translate(rankManager.getRank().getColor(player.getUniqueId()) + rankManager.getRank().getName(player.getUniqueId()) + " &fArmor"))
				.addEnchantment(Enchantment.DURABILITY, 1)
				.setLeatherArmorColor(colorByRank(player, player1))
				.build();

		ItemStack legs = new ItemBuilder(Material.LEATHER_LEGGINGS)
				.setName(ChatUtil.translate(rankManager.getRank().getColor(player.getUniqueId()) + rankManager.getRank().getName(player.getUniqueId()) + " &fArmor"))
				.addEnchantment(Enchantment.DURABILITY, 1)
				.setLeatherArmorColor(colorByRank(player, player1))
				.build();

		ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
				.setName(ChatUtil.translate(rankManager.getRank().getColor(player.getUniqueId()) + rankManager.getRank().getName(player.getUniqueId()) + " &fArmor"))
				.addEnchantment(Enchantment.DURABILITY, 1)
				.setLeatherArmorColor(colorByRank(player, player1))
				.build();

		player.getInventory().setChestplate(chest);
		player.getInventory().setLeggings(legs);
		player.getInventory().setBoots(boots);
		player.updateInventory();
	}

	private Color colorByRank(Player player, String player1) {
		String color = rankManager.getRank().getColor(player.getUniqueId());

		if (color.equals(ChatColor.RED.toString())) {
			return Color.RED;
		} else if (color.equals(ChatColor.DARK_RED.toString())) {
			return Color.MAROON;
		} else if (color.equals(ChatColor.BLUE.toString())) {
			return Color.BLUE;
		} else if (color.equals(ChatColor.DARK_BLUE.toString())) {
			return Color.NAVY;
		} else if (color.equals(ChatColor.GRAY.toString())) {
			return Color.SILVER;
		} else if (color.equals(ChatColor.DARK_GRAY.toString())) {
			return Color.GRAY;
		} else if (color.equals(ChatColor.WHITE.toString())) {
			return Color.WHITE;
		} else if (color.equals(ChatColor.YELLOW.toString())) {
			return Color.YELLOW;
		} else if (color.equals(ChatColor.GREEN.toString())) {
			return Color.LIME;
		} else if (color.equals(ChatColor.DARK_GREEN.toString())) {
			return Color.GREEN;
		} else if (color.equals(ChatColor.GOLD.toString())) {
			return Color.ORANGE;
		} else if (color.equals(ChatColor.LIGHT_PURPLE.toString())) {
			return Color.FUCHSIA;
		} else if (color.equals(ChatColor.DARK_PURPLE.toString())) {
			return Color.PURPLE;
		} else if (color.equals(ChatColor.AQUA.toString())) {
			return Color.AQUA;
		} else if (color.equals(ChatColor.DARK_AQUA.toString())) {
			return Color.TEAL;
		} else if (color.equals(ChatColor.BLACK.toString())) {
			return Color.BLACK;
		}
		return Color.TEAL;
	}
}