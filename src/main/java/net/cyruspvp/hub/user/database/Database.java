package net.cyruspvp.hub.user.database;

import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.user.User;
import net.cyruspvp.hub.user.UserManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class Database {

    public final Berlin plugin;
    public final UserManager userManager;

    public abstract void load(User user);

    public abstract void save(User user);

    public abstract User getUserFromDB(String name);

    public abstract User getUserFromDB(UUID uuid);

    public abstract List<User> getUsersFromDB();
}
