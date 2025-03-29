package net.cyruspvp.hub.utilities.versions.type;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.cyruspvp.hub.nametags.Nametag;
import net.cyruspvp.hub.nametags.extra.NameVisibility;
import net.cyruspvp.hub.tablist.extra.TablistSkin;
import net.cyruspvp.hub.utilities.ReflectionUtils;
import net.cyruspvp.hub.utilities.extra.Module;
import net.cyruspvp.hub.utilities.file.Config;
import net.cyruspvp.hub.utilities.versions.Version;
import net.cyruspvp.hub.utilities.versions.VersionManager;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@SuppressWarnings("all")
public class Version1_16_R3 extends Module<VersionManager> implements Version {

    private static final Field ENTITY_ID = ReflectionUtils.accessField(PacketPlayOutEntityEquipment.class, "a");
    private static final Field SLOT = ReflectionUtils.accessField(PacketPlayOutEntityEquipment.class, "b");
    private static final Field ACTION = ReflectionUtils.accessField(PacketPlayOutPlayerInfo.class, "a");
    private static final Field DATA = ReflectionUtils.accessField(PacketPlayOutPlayerInfo.class, "b");

    private static final Method method = ReflectionUtils.accessMethod(
            CraftWorld.class, "spawnParticle", Particle.class, Location.class, int.class, Object.class
    );

    public Version1_16_R3(VersionManager manager) {
        super(manager);
    }

    @Override
    public CommandMap getCommandMap() {
        try {

            CraftServer server = (CraftServer) Bukkit.getServer();
            Method method = server.getClass().getMethod("getCommandMap");
            return (CommandMap) method.invoke(server);

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<Player> getTrackedPlayers(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PlayerChunkMap.EntityTracker tracker = entityPlayer.tracker;

        if (tracker != null) {
            Set<Player> players = new HashSet<>();

            for (EntityPlayer trackedPlayer : new ArrayList<>(tracker.trackedPlayers)) {
                players.add(trackedPlayer.getBukkitEntity());
            }

            return players;
        }

        return Collections.emptySet();
    }

    @Override
    public int getPing(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        return craftPlayer.getHandle().ping;
    }

    @Override
    public ItemStack getItemInHand(Player player) {
        try {

            Method method = player.getInventory().getClass().getMethod("getItemInMainHand");
            return (ItemStack) method.invoke(player.getInventory());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ItemStack addGlow(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void setItemInHand(Player player, ItemStack item) {
        try {

            Method method = player.getInventory().getClass().getMethod("setItemInMainHand", ItemStack.class);
            method.invoke(player.getInventory(), item);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playEffect(Location location, String name, Object data) {
        try {

            Particle particle = Particle.valueOf(name);
            method.invoke(location.getWorld(), particle, location, 1, data);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Particle " + name + " does not exist.");

        } catch (InvocationTargetException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTPSColored() {
        double tps = MinecraftServer.getServer().recentTps[0];
        String color = (tps > 18 ? "§a" : tps > 16 ? "§e" : "§c");
        String asterisk = (tps > 20 ? "*" : "");
        return color + asterisk + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    @Override
    public void hideArmor(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        if (entityPlayer.tracker != null) {
            List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> items = new ArrayList<>();

            items.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(null)));
            items.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(null)));
            items.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(null)));
            items.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(null)));

            entityPlayer.tracker.broadcast(new PacketPlayOutEntityEquipment(player.getEntityId(), items));
        }
    }

    @Override
    public void showArmor(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        if (entityPlayer.tracker != null) {
            org.bukkit.inventory.PlayerInventory inventory = player.getInventory();
            List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> items = new ArrayList<>();

            items.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(inventory.getBoots())));
            items.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(inventory.getLeggings())));
            items.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(inventory.getChestplate())));
            items.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(inventory.getHelmet())));

            entityPlayer.tracker.broadcast(new PacketPlayOutEntityEquipment(player.getEntityId(), items));
        }
    }

    @Override
    public void handleNettyListener(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        ChannelPipeline pipeline = entityPlayer.playerConnection.networkManager.channel.pipeline();

        if (pipeline.get("packet_handler") == null) {
            player.kickPlayer(Config.COULD_NOT_LOAD_DATA);
            return;
        }

        pipeline.addBefore("packet_handler", "Azurite", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof PacketPlayOutEntityEquipment) {
                    PacketPlayOutEntityEquipment packet = (PacketPlayOutEntityEquipment) msg;
                    int id = (int) ENTITY_ID.get(packet);

                } else if (msg instanceof PacketPlayOutPlayerInfo) {
                    PacketPlayOutPlayerInfo packet = (PacketPlayOutPlayerInfo) msg;
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction action = (PacketPlayOutPlayerInfo.EnumPlayerInfoAction) ACTION.get(packet);
                    Nametag nametag = getInstance().getNametagManager().getNametags().get(player.getUniqueId());

                    if (action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER && nametag != null) {
                        List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) DATA.get(packet);

                        for (PacketPlayOutPlayerInfo.PlayerInfoData info : data) {
                            Player targetPlayer = Bukkit.getPlayer(info.a().getId());

                            if (targetPlayer == null) continue;

                            // Make tablist sort instantly
                            nametag.getPacket().create("tablist", "", "", "", false, NameVisibility.ALWAYS);
                            nametag.getPacket().addToTeam(targetPlayer, "tablist");
                        }
                    }
                }

                super.write(ctx, msg, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof PacketPlayInBlockDig) {
                    PacketPlayInBlockDig packet = (PacketPlayInBlockDig) msg;
                    PacketPlayInBlockDig.EnumPlayerDigType type = packet.d();

                }

                super.channelRead(ctx, msg);
            }
        });
    }

    @Override
    public void sendToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void damageItemDefault(Player player, ItemStack hand) {
        if (hand != null) {
            net.minecraft.server.v1_16_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(hand);
            nmsCopy.damage(1, ((CraftPlayer) player).getHandle(), (entityPlayer -> entityPlayer.broadcastItemBreak(EnumHand.MAIN_HAND)));
            setItemInHand(player, CraftItemStack.asBukkitCopy(nmsCopy));
        }
    }

    @Override
    public void clearArrows(Player player) {
        ((CraftPlayer) player).getHandle().setArrowCount(0);
    }

    @Override
    public List<ItemStack> getBlockDrops(Player bukkitPlayer, org.bukkit.block.Block bukkitBlock, ItemStack item) {
        List<ItemStack> drops = new LinkedList<>();

        EntityPlayer entityPlayer = ((CraftPlayer) bukkitPlayer).getHandle();
        BlockPosition blockPosition = new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        WorldServer worldServer = entityPlayer.playerInteractManager.world;
        IBlockData blockData = worldServer.getType(blockPosition);
        net.minecraft.server.v1_16_R3.ItemStack itemStack = entityPlayer.getItemInMainHand();
        itemStack = itemStack.isEmpty() ? net.minecraft.server.v1_16_R3.ItemStack.b : itemStack.cloneItemStack();
        TileEntity tileEntity = worldServer.getTileEntity(blockPosition);

        net.minecraft.server.v1_16_R3.Block.getDrops(blockData, worldServer, blockPosition, tileEntity, entityPlayer, itemStack).forEach(nmsItem ->
                drops.add(CraftItemStack.asCraftMirror(nmsItem)));

        return drops;
    }

    @Override
    public TablistSkin getSkinData(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getProfile();

        if (profile.getProperties().get("textures").size() == 0) {
            return null;
        }

        Property property = profile.getProperties().get("textures").iterator().next();
        return new TablistSkin(property.getValue(), property.getSignature());
    }
}