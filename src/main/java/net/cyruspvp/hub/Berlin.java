package net.cyruspvp.hub;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import net.cyruspvp.hub.clients.ClientHook;
import net.cyruspvp.hub.command.CommandManager;
import net.cyruspvp.hub.command.impl.BuildModeCommand;
import net.cyruspvp.hub.command.impl.ChatCommand;
import net.cyruspvp.hub.command.impl.BerlinCommand;
import net.cyruspvp.hub.command.impl.media.DiscordCommand;
import net.cyruspvp.hub.command.impl.media.ProfileCommand;
import net.cyruspvp.hub.command.impl.media.StoreCommand;
import net.cyruspvp.hub.command.impl.media.TeamSpeakCommand;
import net.cyruspvp.hub.command.impl.messages.MessageCommand;
import net.cyruspvp.hub.command.impl.messages.ReplyCommand;
import net.cyruspvp.hub.command.impl.queue.QueueCommand;
import net.cyruspvp.hub.command.impl.queue.subcommands.QueueJoinCommand;
import net.cyruspvp.hub.command.impl.queue.subcommands.QueueLeaveCommand;
import net.cyruspvp.hub.command.impl.queue.subcommands.QueueListCommand;
import net.cyruspvp.hub.command.impl.queue.subcommands.QueuePauseCommand;
import net.cyruspvp.hub.command.impl.spawn.SetSpawnCommand;
import net.cyruspvp.hub.command.impl.spawn.SpawnCommand;
import net.cyruspvp.hub.command.impl.timer.TimerCommand;
import net.cyruspvp.hub.command.impl.timer.subcommand.TimerListCommand;
import net.cyruspvp.hub.command.impl.timer.subcommand.TimerStartCommand;
import net.cyruspvp.hub.command.impl.timer.subcommand.TimerStopCommand;
import net.cyruspvp.hub.database.mongo.MongoManager;
import net.cyruspvp.hub.database.redis.RedisManager;
import net.cyruspvp.hub.integrations.PlaceholderAPIHook;
import net.cyruspvp.hub.listeners.*;
import net.cyruspvp.hub.model.ban.BanManager;
import net.cyruspvp.hub.model.custommessages.CustomMessagesListener;
import net.cyruspvp.hub.listeners.hotbar.CustomHotbarListener;
import net.cyruspvp.hub.listeners.hotbar.NormalHotbarListener;
import net.cyruspvp.hub.model.actionbar.ActionBarRunnable;
import net.cyruspvp.hub.model.hcfcore.HCFCoreManager;
import net.cyruspvp.hub.model.hotbar.custom.CustomHotbarManager;
import net.cyruspvp.hub.model.hotbar.normal.NormalHotbarManager;
import net.cyruspvp.hub.model.queue.QueueManager;
import net.cyruspvp.hub.model.queue.rank.QueueRankManager;
import net.cyruspvp.hub.model.queue.thread.QueuePositionThread;
import net.cyruspvp.hub.model.queue.thread.QueueReminderThread;
import net.cyruspvp.hub.model.rank.RankManager;
import net.cyruspvp.hub.model.selector.ServerSelectorManager;
import net.cyruspvp.hub.model.server.ServerDataManager;
import net.cyruspvp.hub.model.spawn.SpawnManager;
import net.cyruspvp.hub.model.timer.TimerManager;
import net.cyruspvp.hub.nametags.NametagManager;
import net.cyruspvp.hub.placeholder.PlaceholderHook;
import net.cyruspvp.hub.scoreboard.Assemble;
import net.cyruspvp.hub.scoreboard.AssembleStyle;
import net.cyruspvp.hub.scoreboard.AssembleProvider;
import net.cyruspvp.hub.scoreboard.animation.ScoreboardAnimated;
import net.cyruspvp.hub.tablist.TablistManager;
import net.cyruspvp.hub.user.UserListener;
import net.cyruspvp.hub.user.UserManager;
import net.cyruspvp.hub.utilities.Logger;
import net.cyruspvp.hub.utilities.cosmetics.ParticleUtils;
import net.cyruspvp.hub.utilities.extra.Configs;
import net.cyruspvp.hub.utilities.extra.Manager;
import net.cyruspvp.hub.utilities.file.Config;
import net.cyruspvp.hub.utilities.file.ConfigYML;
import net.cyruspvp.hub.utilities.file.FileConfig;
import net.cyruspvp.hub.utilities.menu.ButtonListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import lombok.Setter;
import net.cyruspvp.hub.utilities.versions.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Berlin extends JavaPlugin {

    private FileConfig configFile,

            languageFile,
            hotbarFile,
            scoreboardFile,
            tablistFile,
            databaseFile,
            queueFile,
            lunarFile,
            serverSelectorFile;

    private boolean loaded = true;

    public static Berlin instance;
    private VersionManager versionManager;
    private List<Manager> managers;
    private PlaceholderHook placeholderHook;
    private ClientHook clientHook;
    private NametagManager nametagManager;
    private Configs configsObject;
    private List<ConfigYML> configs;
    private CommandManager commandManager;
    private RankManager rankManager;
    private BanManager banManager;
    private UserManager userManager;
    private net.cyruspvp.hub.utilities.users.UserManager userManager2;
    private SpawnManager spawnManager;
    private NormalHotbarManager normalHotbarManager;
    private CustomHotbarManager customHotbarManager;
    private ServerSelectorManager serverSelectorManager;
    private TimerManager timerManager;
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private HCFCoreManager hcfCoreManager;
    private QueueRankManager queueRankManager;
    private QueueManager queueManager;
    private ServerDataManager serverDataManager;
    private PacketEventsAPI<?> packetEventsAPI;

    private Assemble assemble;

    public static Berlin get() {
        return instance;
    }
    public static Berlin getPlugin() {
        return Berlin.getPlugin(Berlin.class);
    }

    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Berlin.getPlugin()));

        this.packetEventsAPI = PacketEvents.getAPI();
        this.packetEventsAPI.getSettings().bStats(false).checkForUpdates(false);

        this.packetEventsAPI.load();
    }

    public void onEnable() {
        instance = this;
        Logger.print(
                Logger.LINE_CONSOLE,
                "      &7✪ &6&lBerlin &7(&fHubCore&7) &7✪",
                "",
                " &fAuthors&7: &6" + this.getDescription().getAuthors(),
                " &fPlugin Version&7: &6" + this.getDescription().getVersion(),
                Logger.LINE_CONSOLE
        );
        this.configs = new ArrayList<>();
        this.managers = new ArrayList<>();

        (this.configsObject = new Configs()).load(this);

        // azurite shit
        this.placeholderHook = new PlaceholderHook(this);
        this.clientHook = new ClientHook(this);
        this.userManager2 = new net.cyruspvp.hub.utilities.users.UserManager(this);

        this.configFile = new FileConfig(this, "config.yml");
        this.languageFile = new FileConfig(this, "language.yml");
        this.hotbarFile = new FileConfig(this, "hotbar.yml");
        this.scoreboardFile = new FileConfig(this, "scoreboard.yml");
        this.databaseFile = new FileConfig(this, "database.yml");
        this.queueFile = new FileConfig(this, "queue.yml");
        this.tablistFile = new FileConfig(this, "tablist.yml");
        this.serverSelectorFile = new FileConfig(this, "selector/server-selector.yml");
        this.lunarFile = new FileConfig(this, "lunar.yml");
        this.mongoManager = new MongoManager(this);
        this.versionManager = new VersionManager(this);
        this.nametagManager = new NametagManager(this);
        this.redisManager = new RedisManager(this);
        this.commandManager = new CommandManager(this);
        this.rankManager = new RankManager();
        this.userManager = new UserManager(this, mongoManager.getMongo("Berlin"));
        this.serverDataManager = new ServerDataManager();
        this.spawnManager = new SpawnManager(this);
        this.normalHotbarManager = new NormalHotbarManager(this);
        this.customHotbarManager = new CustomHotbarManager(this);
        this.serverSelectorManager = new ServerSelectorManager(this);
        this.timerManager = new TimerManager();
        this.queueRankManager = new QueueRankManager(this);
        this.queueManager = new QueueManager(this);
        this.hcfCoreManager = new HCFCoreManager(this);
        this.banManager = new BanManager();

        if (scoreboardFile.getBoolean("scoreboard.enabled")) {
            this.assemble = new Assemble(this, new AssembleProvider(this));
            this.assemble.setAssembleStyle(AssembleStyle.MODERN);
            this.assemble.setTicks(2);

            ScoreboardAnimated.init();
        }
        PlaceholderAPIHook.initialize(this);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ButtonListener(), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new ChatManager(this), this);
        pluginManager.registerEvents(new WorldListener(), this);
        pluginManager.registerEvents(new ParticleUtils(), this);
        pluginManager.registerEvents(new SpawnListener(this), this);
        pluginManager.registerEvents(new WorldListener(), this);
        pluginManager.registerEvents(new NormalHotbarListener(this), this);
        pluginManager.registerEvents(new CustomHotbarListener(this), this);
        pluginManager.registerEvents(new QueueListener(this), this);
        pluginManager.registerEvents(new UserListener(this), this);
        pluginManager.registerEvents(new CustomMessagesListener(this), this);
        commandManager.registerCommands(new BerlinCommand(this));
        commandManager.registerCommands(new ChatCommand(this));
        commandManager.registerCommands(new MessageCommand(this));
        commandManager.registerCommands(new ReplyCommand(this));
        commandManager.registerCommands(new DiscordCommand(this));
        commandManager.registerCommands(new StoreCommand(this));
        commandManager.registerCommands(new TeamSpeakCommand(this));
        commandManager.registerCommands(new BuildModeCommand(this));
        commandManager.registerCommands(new SetSpawnCommand(this));
        commandManager.registerCommands(new SpawnCommand(this));
        commandManager.registerCommands(new TimerCommand());
        commandManager.registerCommands(new TimerStartCommand(this));
        commandManager.registerCommands(new TimerStopCommand(this));
        commandManager.registerCommands(new TimerListCommand(this));
        commandManager.registerCommands(new QueueCommand());
        commandManager.registerCommands(new QueueJoinCommand(this));
        commandManager.registerCommands(new QueueLeaveCommand(this));
        commandManager.registerCommands(new QueuePauseCommand(this));
        commandManager.registerCommands(new QueueListCommand(this));
        commandManager.registerCommands(new ProfileCommand(this));

        if (Config.TABLIST_ENABLED) new TablistManager(this);

        this.managers.forEach(Manager::enable);
        this.loaded = true;

        if (Berlin.get().getConfigFile().getBoolean("action-bar.enabled")) {
            int interval = Berlin.get().getConfigFile().getInt("action-bar.interval");
            Bukkit.getScheduler().runTaskTimer(Berlin.getPlugin(), new ActionBarRunnable(Berlin.get(), interval), interval * 20L, interval * 20L);
        }

        QueueReminderThread reminderThread = new QueueReminderThread(Berlin.get());
        reminderThread.start();

        QueuePositionThread positionThread = new QueuePositionThread(Berlin.get());
        positionThread.start();

        Bukkit.getMessenger().registerOutgoingPluginChannel(Berlin.getPlugin(), "BungeeCord");
    }

    public void onDisable() {
        this.managers.forEach(Manager::disable);

        if (userManager != null) this.userManager.onDisable();
        if (redisManager != null) this.redisManager.onDisable();
        if (assemble != null) ScoreboardAnimated.disable();
    }

    public void onReload() {
        for (ConfigYML config : configs) {
            config.reload();
            config.reloadCache();
        }

        this.configFile.reload();
        this.hotbarFile.reload();
        this.scoreboardFile.reload();
        this.tablistFile.reload();
        this.lunarFile.reload();
        this.databaseFile.reload();
        this.queueFile.reload();
        this.serverSelectorFile.reload();
        this.normalHotbarManager.loadOrRefresh(true);
        this.customHotbarManager.loadOrRefresh(true);
        this.serverSelectorManager.loadOrRefresh();
        this.queueRankManager.loadOrRefresh();
        this.queueManager.loadOrRefresh();

        for (Manager manage : getManagers()) {
            manage.reload();
        }

        Config.load(getConfigsObject(), true);
    }
}