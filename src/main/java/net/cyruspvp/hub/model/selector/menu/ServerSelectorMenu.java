package net.cyruspvp.hub.model.selector.menu;

import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.selector.ServerSelector;
import net.cyruspvp.hub.model.selector.ServerSelectorManager;
import net.cyruspvp.hub.model.selector.menu.button.ServerSelectorButton;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.menu.Button;
import net.cyruspvp.hub.utilities.menu.Menu;
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
