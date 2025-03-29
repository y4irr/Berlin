package net.cyruspvp.hub.placeholder;

import net.cyruspvp.hub.placeholder.type.NonePlaceholderHook;
import net.cyruspvp.hub.placeholder.type.PlaceholderAPIHook;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Manager;
import org.bukkit.entity.Player;

import java.util.List;


public class PlaceholderHook extends Manager implements Placeholder {

    private Placeholder placeholder;

    public PlaceholderHook(Berlin instance) {
        super(instance);
        this.load();
    }

    private void load() {
        if (Utils.verifyPlugin("PlaceholderAPI", getInstance())) {
            placeholder = new PlaceholderAPIHook(this);

        } else {
            placeholder = new NonePlaceholderHook(this);
        }
    }

    @Override
    public String replace(Player player, String string) {
        return placeholder.replace(player, string);
    }

    @Override
    public List<String> replace(Player player, List<String> list) {
        return placeholder.replace(player, list);
    }
}