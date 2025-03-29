package net.cyruspvp.hub.placeholder.type;

import net.cyruspvp.hub.placeholder.Placeholder;
import net.cyruspvp.hub.placeholder.PlaceholderHook;
import net.cyruspvp.hub.utilities.extra.Module;
import org.bukkit.entity.Player;

import java.util.List;


public class NonePlaceholderHook extends Module<PlaceholderHook> implements Placeholder {

    public NonePlaceholderHook(PlaceholderHook manager) {
        super(manager);
    }

    @Override
    public String replace(Player player, String string) {
        return string;
    }

    @Override
    public List<String> replace(Player player, List<String> list) {
        return list;
    }
}