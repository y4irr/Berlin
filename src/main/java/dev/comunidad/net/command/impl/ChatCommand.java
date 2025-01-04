package dev.comunidad.net.command.impl;

import dev.comunidad.net.command.BaseCommand;
import dev.comunidad.net.command.Command;
import dev.comunidad.net.command.CommandArgs;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatCommand extends BaseCommand implements Listener {

    private final @NotNull Berlin plugin;

    private boolean chatMuted;
    private long cooldownTime = 10000;

    private final @NotNull Map<UUID, Long> cooldownMap = new HashMap<>();

    public ChatCommand(@NotNull Berlin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin.getPlugin());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (chatMuted) {
            if (player.hasPermission("chat.admin")) {
                return;
            }
            event.setCancelled(true);
            player.sendMessage(ChatUtil.translate("&cChat is currently muted."));
            return;
        }

        if (cooldownMap.containsKey(playerUUID)) {
            long lastMessageTime = cooldownMap.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMessageTime < cooldownTime) {
                event.setCancelled(true);
                long timeRemaining = (cooldownTime - (currentTime - lastMessageTime)) / 1000;
                player.sendMessage(ChatUtil.translate("&cYou have to wait " + timeRemaining + "s before sending another message."));
                return;
            }
        }
        cooldownMap.put(playerUUID, System.currentTimeMillis());
    }

    @Command(
            name = "chat",
            permission = "chat.admin",
            inGameOnly = false
    )
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&7&m----------------------------");
            ChatUtil.sendMessage(sender, "&d&lChat Management");
            ChatUtil.sendMessage(sender, "");
            ChatUtil.sendMessage(sender, "&8 ● &d/chat mute &7- &fMute the chat");
            ChatUtil.sendMessage(sender, "&8 ● &d/chat unmute &7- &fUnmute the chat");
            ChatUtil.sendMessage(sender, "&8 ● &d/chat cooldown &7- &fAdjust the cooldown of chat messages");
            ChatUtil.sendMessage(sender, "&7&m----------------------------");
        } else {
            if (args[0].equalsIgnoreCase("mute")) {
                chatMuted = true;

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String prefix = plugin.getRankManager().getRank().getPrefix(player.getUniqueId());
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                    Bukkit.broadcastMessage(ChatUtil.translate("&cChat has been muted by " + prefix + player.getName() + "&c."));
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                } else {
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                    Bukkit.broadcastMessage(ChatUtil.translate("&cChat has been muted by Console"));
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                }

                ChatUtil.sendMessage(sender, "&aChat has been muted succesfully.");
            } else if (args[0].equalsIgnoreCase("unmute")) {
                chatMuted = false;

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String prefix = plugin.getRankManager().getRank().getPrefix(player.getUniqueId());
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                    Bukkit.broadcastMessage(ChatUtil.translate("&aChat has been unmuted by " + prefix + player.getName() + "&a."));
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                } else {
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                    Bukkit.broadcastMessage(ChatUtil.translate("&aChat has been unmuted by console."));
                    Bukkit.broadcastMessage(ChatUtil.translate(" "));
                }

                ChatUtil.sendMessage(sender, "&aChat has been unmuted succesfully.");
            } else if (args[0].equalsIgnoreCase("cooldown")) {
                if (args.length < 2) {
                    ChatUtil.sendMessage(sender, "&cYou have to specify the time cooldown.");
                    return;
                }
                try {
                    long newCooldown = Long.parseLong(args[1]) * 1000;
                    cooldownTime = newCooldown;
                    ChatUtil.sendMessage(sender, "&aThe cooldown between messages is now &d" + args[1] + "s&a.");
                } catch (NumberFormatException e) {
                    ChatUtil.sendMessage(sender, "&cThe cooldown time has to be a valid number.");
                }
            }
        }
    }
}