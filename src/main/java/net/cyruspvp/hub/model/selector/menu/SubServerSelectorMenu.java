package net.cyruspvp.hub.model.selector.menu;

import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.selector.ServerSelector;
import net.cyruspvp.hub.model.selector.SubServerSelector;
import net.cyruspvp.hub.model.selector.menu.button.SubServerSelectorButton;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.menu.Button;
import net.cyruspvp.hub.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SubServerSelectorMenu extends Menu {

    private final Berlin plugin;
    private final ServerSelector serverSelector;

    public SubServerSelectorMenu(Berlin plugin, ServerSelector serverSelector) {
        this.plugin = plugin;
        this.serverSelector = serverSelector;
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtil.translate(serverSelector.getMenuTitle()
                .replace("<server>", serverSelector.getName()));
    }

    @Override
    public int getSize() {
        return 9 * serverSelector.getMenuRows();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (SubServerSelector subServerSelector : serverSelector.getSubServerSelectors()) {
            buttons.put(subServerSelector.getIconSlot(), new SubServerSelectorButton(plugin, subServerSelector));
        }

        return buttons;
    }
}
