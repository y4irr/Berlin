package net.cyruspvp.hub.tablist.extra;

import lombok.Getter;
import lombok.Setter;
import net.cyruspvp.hub.tablist.Tablist;
import net.cyruspvp.hub.utilities.users.User;
import org.bukkit.entity.Player;

import java.util.List;


@Getter
@Setter
public class TablistData {

    private Tablist info;
    private User user;
    private String line;
    private Player player;

    public TablistData(Tablist info, User user, Player player) {
        this.info = info;
        this.user = user;
        this.player = player;
        this.line = null;
    }
}