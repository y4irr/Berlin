package net.cyruspvp.hub.model.server;

public interface IServerData {

    boolean isOffline(String server);
    boolean isWhitelisted(String server);
    boolean isMaintenance(String server);
}
