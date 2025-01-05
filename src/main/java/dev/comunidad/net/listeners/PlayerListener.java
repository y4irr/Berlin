package dev.comunidad.net.listeners;

import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.PlayerUtil;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.utilities.cosmetics.ParticleUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import static com.cryptomorin.xseries.messages.ActionBar.sendActionBar;


public class PlayerListener implements Listener {

	private final Berlin plugin;

	public PlayerListener(Berlin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		float walkSpeed = (float) plugin.getConfigFile().getDouble("Player-Join.Speed");
		sendActionBar(player, ChatUtil.translate("&f&ki&4❤&f&ki&r &d&l75% OFF &favailable at &2&lstore.cyruspvp.net &f&ki&4❤&f&ki&r"));
		Bukkit.getScheduler().runTaskLater(this.plugin.getPlugin(), () -> {
			player.sendMessage(ChatUtil.translate(new String[]{
					"",
					"&2&lHappy Holidays &4&l❤",
					"&7We are celebrating our first Christmas together,",
					"&7Thank you for all of your support &4&l❤",
					""}));
		}, 20L);
		Bukkit.getScheduler().runTaskLater(this.plugin.getPlugin(), () -> { PlayerUtil.playSound(player, "CAT_PURR");}, 20L);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setGameMode(GameMode.ADVENTURE);
		player.setWalkSpeed(walkSpeed);
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
			if (player.getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(false);
				player.setAllowFlight(true);
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
}