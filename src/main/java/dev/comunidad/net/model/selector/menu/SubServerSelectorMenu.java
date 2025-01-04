package dev.comunidad.net.model.selector.menu;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.model.selector.ServerSelector;
import dev.comunidad.net.model.selector.SubServerSelector;
import dev.comunidad.net.model.selector.menu.button.SubServerSelectorButton;
import dev.comunidad.net.utilities.ChatUtil;
import dev.comunidad.net.utilities.menu.Button;
import dev.comunidad.net.utilities.menu.Menu;
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
