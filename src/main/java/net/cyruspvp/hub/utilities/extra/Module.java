package net.cyruspvp.hub.utilities.extra;

import lombok.Getter;
import net.cyruspvp.hub.nametags.Nametag;
import net.cyruspvp.hub.tablist.Tablist;
import net.cyruspvp.hub.utilities.users.User;
import net.cyruspvp.hub.Berlin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

@Getter
public abstract class Module<T extends Manager> extends Configs implements Listener {

    private final Berlin instance;
    private final T manager;

    public Module(T manager) {
        this.instance = manager.getInstance();
        this.manager = manager;

        this.checkListener();
    }

    private void checkListener() {
        // Improve load times
        if (this instanceof User) return;
        if (this instanceof Tablist) return;
        if (this instanceof Nametag) return;

        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                manager.registerListener(this);
                break; // Break the loop, we already know it's a listener now.
            }
        }
    }
}