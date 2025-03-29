package net.cyruspvp.hub.utilities.versions;

import lombok.Getter;
import lombok.SneakyThrows;
import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.utilities.Logger;
import net.cyruspvp.hub.utilities.Utils;
import net.cyruspvp.hub.utilities.extra.Manager;
import org.bukkit.Bukkit;


public class VersionManager extends Manager {

    @Getter
    private final Version version;

    public VersionManager(Berlin instance) {
        super(instance);
        this.version = setVersion();
    }

    @SneakyThrows
    public Version setVersion() {
        String path = "net.cyruspvp.hub.utilities.versions.type.Version" + Utils.getNMSVer();

        try {

            return (Version) Class.forName(path).getConstructor(VersionManager.class).newInstance(this);

        } catch (ClassNotFoundException e) {
            Bukkit.getServer().shutdown();
            Logger.print(
                    Logger.LINE_CONSOLE,
                    "- &dAzurite HCF",
                    "- &cThis version is not supported.",
                    Logger.LINE_CONSOLE
            );
            return null;
        }
    }
}