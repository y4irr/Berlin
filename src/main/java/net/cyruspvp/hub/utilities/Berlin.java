package net.cyruspvp.hub.utilities;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import net.cyruspvp.hub.BerlinPlugin;
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
import dev.comunidad.net.listeners.*;
import net.cyruspvp.hub.listeners.*;
import net.cyruspvp.hub.model.ban.BanManager;
import net.cyruspvp.hub.model.custommessages.CustomMessagesListener;
import net.cyruspvp.hub.listeners.hotbar.CustomHotbarListener;
import net.cyruspvp.hub.listeners.hotbar.NormalHotbarListener;
import net.cyruspvp.hub.model.actionbar.ActionBarRunnable;
import net.cyruspvp.hub.model.hcfcore.HCFCoreManager;
import net.cyruspvp.hub.model.hotbar.custom.CustomHotbarManager;
import net.cyruspvp.hub.model.hotbar.normal.NormalHotbarManager;
import net.cyruspvp.hub.model.lunarclient.LunarClientRunnable;
import net.cyruspvp.hub.model.queue.QueueManager;
import net.cyruspvp.hub.model.queue.rank.QueueRankManager;
import net.cyruspvp.hub.model.queue.thread.QueuePositionThread;
import net.cyruspvp.hub.model.queue.thread.QueueReminderThread;
import net.cyruspvp.hub.model.rank.RankManager;
import net.cyruspvp.hub.model.selector.ServerSelectorManager;
import net.cyruspvp.hub.model.server.ServerDataManager;
import net.cyruspvp.hub.model.spawn.SpawnManager;
import net.cyruspvp.hub.model.timer.TimerManager;
import net.cyruspvp.hub.scoreboard.Assemble;
import net.cyruspvp.hub.scoreboard.AssembleStyle;
import net.cyruspvp.hub.scoreboard.AssembleProvider;
import net.cyruspvp.hub.scoreboard.animation.ScoreboardAnimated;
import net.cyruspvp.hub.tablist.TablistHandler;
import net.cyruspvp.hub.tablist.adapter.impl.CustomTablistProvider;
import net.cyruspvp.hub.tablist.listener.TeamsPacketListener;
import net.cyruspvp.hub.user.UserListener;
import net.cyruspvp.hub.user.UserManager;
import net.cyruspvp.hub.utilities.cosmetics.ParticleUtils;
import net.cyruspvp.hub.utilities.file.FileConfig;
import net.cyruspvp.hub.utilities.menu.ButtonListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import java.util.Arrays;

@Getter @Setter
public class Berlin {

    private FileConfig configFile,

            languageFile,
            hotbarFile,
            scoreboardFile,
            tablistFile,
            tablistHeadsFile,
            databaseFile,
            queueFile,
            serverSelectorFile;

    private CommandManager commandManager;
    private RankManager rankManager;
    private BanManager banManager;
    private UserManager userManager;
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
    private BerlinPlugin plugin;

    private TablistHandler tablistHandler;
    private PacketEventsAPI<?> packetEventsAPI;

    private Assemble assemble;

    public Berlin(BerlinPlugin plugin) {
        this.plugin = plugin;
    }

    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(BerlinPlugin.getPlugin()));

        this.packetEventsAPI = PacketEvents.getAPI();
        this.packetEventsAPI.getSettings().bStats(false).checkForUpdates(false);

        this.packetEventsAPI.load();
    }

    public void onEnable() {
        Arrays.asList(
                "&7&m                                              ",
                "      &8&l» &6&lBerlin &7(&fHubCore&7) &8&l«       ",
                "",
                " &8&l» &6Author&7: &f" + BerlinPlugin.getPlugin().getDescription().getAuthors(),
                " &8&l» &6Plugin Version&7: &f" + BerlinPlugin.getPlugin().getDescription().getVersion(),

                "",
                " &8&l» &6Thank you DevEliezeerPro123456789 for helping &a<3",
                " &8&l» &fOriginally based on Comet",
                "&7&m                                              "
                ).forEach(s -> Bukkit.getConsoleSender().sendMessage(ChatUtil.translate(s)));


        BerlinPlugin.get().registerManagers();
        BerlinPlugin.get().registerListeners();
        BerlinPlugin.get().registerCommands();

        if (BerlinPlugin.get().getConfigFile().getBoolean("lunar-client.enabled")) {
            Bukkit.getScheduler().runTaskTimer(BerlinPlugin.getPlugin(), new LunarClientRunnable(BerlinPlugin.get()), 0L, 20L);
        }
        if (BerlinPlugin.get().getConfigFile().getBoolean("action-bar.enabled")) {
            int interval = BerlinPlugin.get().getConfigFile().getInt("action-bar.interval");
            Bukkit.getScheduler().runTaskTimer(BerlinPlugin.getPlugin(), new ActionBarRunnable(BerlinPlugin.get(), interval), interval * 20L, interval * 20L);
        }

        QueueReminderThread reminderThread = new QueueReminderThread(BerlinPlugin.get());
        reminderThread.start();

        QueuePositionThread positionThread = new QueuePositionThread(BerlinPlugin.get());
        positionThread.start();

        Bukkit.getMessenger().registerOutgoingPluginChannel(BerlinPlugin.getPlugin(), "BungeeCord");
    }

    public void onDisable() {
        if (tablistHandler != null) this.tablistHandler.unload();
        if (userManager != null) this.userManager.onDisable();
        if (redisManager != null) this.redisManager.onDisable();
        if (assemble != null) ScoreboardAnimated.disable();
    }

    public void onReload() {
        this.configFile.reload();
        this.hotbarFile.reload();
        this.scoreboardFile.reload();
        this.tablistFile.reload();
        this.databaseFile.reload();
        this.queueFile.reload();
        this.serverSelectorFile.reload();
        this.normalHotbarManager.loadOrRefresh(true);
        this.customHotbarManager.loadOrRefresh(true);
        this.serverSelectorManager.loadOrRefresh();
        this.queueRankManager.loadOrRefresh();
        this.queueManager.loadOrRefresh();
    }

    public void registerManagers() {
        this.configFile = new FileConfig(BerlinPlugin.getPlugin(), "config.yml");
        this.languageFile = new FileConfig(BerlinPlugin.getPlugin(), "language.yml");
        this.hotbarFile = new FileConfig(BerlinPlugin.getPlugin(), "hotbar.yml");
        this.scoreboardFile = new FileConfig(BerlinPlugin.getPlugin(), "scoreboard.yml");
        this.databaseFile = new FileConfig(BerlinPlugin.getPlugin(), "database.yml");
        this.queueFile = new FileConfig(BerlinPlugin.getPlugin(), "queue.yml");
        this.tablistFile = new FileConfig(BerlinPlugin.getPlugin(), "tablist/tablist.yml");
        this.tablistHeadsFile = new FileConfig(BerlinPlugin.getPlugin(), "tablist/heads.yml");
        this.serverSelectorFile = new FileConfig(BerlinPlugin.getPlugin(), "selector/server-selector.yml");

        this.mongoManager = new MongoManager(this);
        this.redisManager = new RedisManager(this);
        this.commandManager = new CommandManager(BerlinPlugin.getPlugin());
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
            this.assemble = new Assemble(BerlinPlugin.getPlugin(), new AssembleProvider(this));
            this.assemble.setAssembleStyle(AssembleStyle.MODERN);
            this.assemble.setTicks(2);

            ScoreboardAnimated.init();
        }
        if (tablistFile.getBoolean("tablist.enabled")) {
            this.packetEventsAPI.init();

            this.tablistHandler = new TablistHandler(BerlinPlugin.getPlugin());
            this.tablistHandler.init(this.packetEventsAPI, new TeamsPacketListener(this.packetEventsAPI));
            this.tablistHandler.registerAdapter(new CustomTablistProvider(this), 20L);
        }
        PlaceholderAPIHook.initialize(this);
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ButtonListener(), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new PlayerListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new ChatManager(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new WorldListener(), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new ParticleUtils(), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new SpawnListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new WorldListener(), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new NormalHotbarListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new CustomHotbarListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new QueueListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new UserListener(this), BerlinPlugin.getPlugin());
        pluginManager.registerEvents(new CustomMessagesListener(this), BerlinPlugin.getPlugin());
    }

    public void registerCommands() {
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
    }
}