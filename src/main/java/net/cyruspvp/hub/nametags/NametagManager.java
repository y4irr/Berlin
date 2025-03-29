package net.cyruspvp.hub.nametags;

import lombok.Getter;
import lombok.SneakyThrows;
import net.cyruspvp.hub.nametags.adapter.AzuriteNametags;
import net.cyruspvp.hub.nametags.listener.NametagListener;
import net.cyruspvp.hub.nametags.packet.NametagPacket;
import net.cyruspvp.hub.nametags.task.NametagTask;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.Tasks;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Manager;
import net.cyruspvp.hub.utilities.extra.NameThreadFactory;
import net.cyruspvp.hub.utilities.file.Config;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class NametagManager extends Manager {

    private final Map<UUID, Nametag> nametags;
    private final AzuriteNametags adapter;
    private final ScheduledExecutorService executor;

    private final Map<UUID, Long> frozenStartTimes = new ConcurrentHashMap<>();

    public NametagManager(Berlin instance) {
        super(instance);

        this.nametags = new ConcurrentHashMap<>();
        this.adapter = new AzuriteNametags(this);
        this.executor = Executors.newScheduledThreadPool(1, new NameThreadFactory("Azurite - NametagThread"));
        this.executor.scheduleAtFixedRate(new NametagTask(this), 0L, 300L, TimeUnit.MILLISECONDS);

        new NametagListener(this);
    }

    @Override
    public void disable() {
        for (Nametag nametag : nametags.values()) {
            nametag.delete();
        }
        executor.shutdownNow();
    }

    public void handleUpdate(Player viewer, Player target) {
        if (viewer == null || target == null) return; // Possibly?

        getAdapter().getAndUpdate(viewer, target);
        updateLunarTags(viewer, target);
    }

    public void updateLunarTags(Player viewer, Player target) {
        if (getInstance().getClientHook() == null || getInstance().getClientHook().getClients().isEmpty()) return;
        if (!nametags.containsKey(viewer.getUniqueId())) return;
        String name = target.getName();

        List<String> format = new ArrayList<>();

        if (getInstance().getBanManager().getBan().isBanned(target.getUniqueId())) {
            for (String s : Config.NAMETAG_BANNED) {
                format.add(s
                        .replace("%name%", name)
                        .replace("%rank-name%", ChatUtil.translate(getInstance().getRankManager().getRank().getName(target.getUniqueId())))
                        .replace("%rank-prefix%", ChatUtil.translate(getInstance().getRankManager().getRank().getPrefix(target.getUniqueId())))
                        .replace("%rank-suffix%", ChatUtil.translate(getInstance().getRankManager().getRank().getSuffix(target.getUniqueId())))
                        .replace("%rank-color%", ChatUtil.translate(getInstance().getRankManager().getRank().getColor(target.getUniqueId())))
                );
            }
        } else {
            for (String s : Config.NAMETAG_NORMAL) {
                format.add(s
                        .replace("%name%", name)
                        .replace("%rank-name%", ChatUtil.translate(getInstance().getRankManager().getRank().getName(target.getUniqueId())))
                        .replace("%rank-prefix%", ChatUtil.translate(getInstance().getRankManager().getRank().getPrefix(target.getUniqueId())))
                        .replace("%rank-suffix%", ChatUtil.translate(getInstance().getRankManager().getRank().getSuffix(target.getUniqueId())))
                        .replace("%rank-color%", ChatUtil.translate(getInstance().getRankManager().getRank().getColor(target.getUniqueId())))
                );
            }
        }

        handleLunar(target, viewer, format);
    }

    private void handleLunar(Player target, Player viewer, List<String> format) {
        // you can't send the packet asynchronously in modern versions
        if (Utils.isModernVer()) {
            Tasks.execute(this, () -> getInstance().getClientHook().overrideNametags(target, viewer, format));
            return;
        }

        getInstance().getClientHook().overrideNametags(target, viewer, format);
    }

    @SneakyThrows
    public NametagPacket createPacket(Player player) {
        String path = "net.cyruspvp.hub.nametags.packet.type.NametagPacketV" + Utils.getNMSVer();
        return (NametagPacket) Class.forName(path).getConstructor(NametagManager.class, Player.class).newInstance(this, player);
    }
}