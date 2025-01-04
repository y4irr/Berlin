package dev.comunidad.net.model.selector.menu;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.selector.ServerSelector;
import dev.comunidad.net.model.selector.ServerSelectorManager;
import dev.comunidad.net.model.selector.menu.button.ServerSelectorButton;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.menu.Button;
import dev.comunidad.net.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ServerSelectorMenu extends Menu {

    private final Berlin plugin;
    private final ServerSelectorManager serverSelectorManager;

    public ServerSelectorMenu(Berlin plugin) {
        this.plugin = plugin;
        this.serverSelectorManager = plugin.getServerSelectorManager();
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtil.translate(plugin.getServerSelectorFile().getString("menu.title"));
    }

    @Override
    public int getSize() {
        return 9 * plugin.getServerSelectorFile().getInt("menu.rows");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (ServerSelector serverSelector : serverSelectorManager.getServerSelectors()) {
            buttons.put(serverSelector.getIconSlot(), new ServerSelectorButton(plugin, serverSelector));
        }

        return buttons;
    }
}
