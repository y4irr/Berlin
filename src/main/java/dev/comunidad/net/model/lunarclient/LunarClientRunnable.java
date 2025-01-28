package dev.comunidad.net.model.lunarclient;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class LunarClientRunnable implements Runnable {
    private final Berlin plugin;
    private final NametagModule module;

    public LunarClientRunnable(Berlin plugin) {
        this.plugin = plugin;
        this.module = Apollo.getModuleManager().getModule(NametagModule.class);
    }

    @Override
    public void run() {
        for (ApolloPlayer player : Apollo.getPlayerManager().getPlayers()) {
            for (ApolloPlayer viewer : Apollo.getPlayerManager().getPlayers()) {
                Player bukkitPlayer = (Player) player.getPlayer();

                boolean isBanned = plugin.getBanManager().getBan().isBanned(bukkitPlayer.getUniqueId());

                String configSection = isBanned ? "lunar-client.banned" : "lunar-client.nametag";
                List<Component> components = plugin.getConfigFile().getStringList(configSection).stream().map((line) -> {
                    String replacedLine = ChatUtil.placeholder(bukkitPlayer, line);
                    return LegacyComponentSerializer.legacySection().deserialize(replacedLine);
                }).collect(Collectors.toList());

                Collections.reverse(components);
                module.overrideNametag(viewer, player.getUniqueId(), Nametag.builder().lines(components).build());
            }
        }
    }
}