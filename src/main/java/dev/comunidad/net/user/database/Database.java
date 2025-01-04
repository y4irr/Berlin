package dev.comunidad.net.user.database;

import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.user.User;
import dev.comunidad.net.user.UserManager;
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
