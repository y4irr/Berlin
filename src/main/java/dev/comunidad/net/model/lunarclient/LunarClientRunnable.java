package dev.comunidad.net.model.lunarclient;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.Berlin;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class LunarClientRunnable implements Runnable {
    private final Berlin plugin;
    private final NametagModule module;

    public LunarClientRunnable(Berlin plugin) {
        this.plugin = plugin;
        this.module = (NametagModule) Apollo.getModuleManager().getModule(NametagModule.class);
    }

    public void run() {
        Iterator var1 = Apollo.getPlayerManager().getPlayers().iterator();

        while(var1.hasNext()) {
            ApolloPlayer player = (ApolloPlayer)var1.next();
            Iterator var3 = Apollo.getPlayerManager().getPlayers().iterator();

            while(var3.hasNext()) {
                ApolloPlayer viewer = (ApolloPlayer)var3.next();
                List<Component> components = (List)this.plugin.getConfigFile().getStringList("lunar-client.nametag").stream().map((line) -> {
                    String replacedLine = ChatUtil.placeholder((Player)player.getPlayer(), line);
                    return LegacyComponentSerializer.legacySection().deserialize(replacedLine);
                }).collect(Collectors.toList());
                Collections.reverse(components);
                this.module.overrideNametag(viewer, player.getUniqueId(), Nametag.builder().lines(components).build());
            }
        }

    }
}

