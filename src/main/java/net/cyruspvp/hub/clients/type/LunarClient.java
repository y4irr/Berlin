package net.cyruspvp.hub.clients.type;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.border.BorderModule;
import com.lunarclient.apollo.module.combat.CombatModule;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.module.waypoint.WaypointModule;
import net.cyruspvp.hub.clients.Client;
import net.cyruspvp.hub.clients.ClientHook;
import net.cyruspvp.hub.utilities.extra.Module;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class LunarClient extends Module<ClientHook> implements Client {

    private final NametagModule nametagModule;
    private final CombatModule combatModule;

    public LunarClient(ClientHook manager) {
        super(manager);
        this.nametagModule = Apollo.getModuleManager().getModule(NametagModule.class);
        this.combatModule = Apollo.getModuleManager().getModule(CombatModule.class);
    }

    @Override
    public void overrideNametags(Player target, Player viewer, List<String> tag) {
        Apollo.getPlayerManager().getPlayer(viewer.getUniqueId()).ifPresent(apolloPlayer -> {
            List<Component> components = tag.stream().map(Component::text).collect(Collectors.toList());
            Collections.reverse(components);
            nametagModule.overrideNametag(apolloPlayer, target.getUniqueId(), Nametag.builder().lines(components).build());
        });
    }

    @Override
    public void handleJoin(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> combatModule.getOptions()
                .set(apolloPlayer, CombatModule.DISABLE_MISS_PENALTY, getLunarConfig().getBoolean("LUNAR_API.FIX_1_8_HIT_DELAY"))
        );
    }
}