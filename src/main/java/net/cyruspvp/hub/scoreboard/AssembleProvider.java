package net.cyruspvp.hub.scoreboard;

import net.cyruspvp.hub.model.ban.BanManager;
import net.cyruspvp.hub.model.rank.RankManager;
import net.cyruspvp.hub.scoreboard.animation.ScoreboardAnimated;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.model.queue.Queue;
import net.cyruspvp.hub.model.queue.QueueManager;
import net.cyruspvp.hub.model.timer.Timer;
import net.cyruspvp.hub.model.timer.TimerManager;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.JavaUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AssembleProvider implements AssembleAdapter {

    private final Berlin plugin;
    private final TimerManager timerManager;
    private final QueueManager queueManager;
    private final BanManager banManager;

    public AssembleProvider(Berlin plugin) {
        this.plugin = plugin;
        this.timerManager = plugin.getTimerManager();
        this.queueManager = plugin.getQueueManager();
        this.banManager = plugin.getBanManager();
    }

    @Override
    public String getTitle(Player player) {
        return ScoreboardAnimated.getTitle();
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        if (isBanned(player)) {
            for (String line : plugin.getScoreboardFile().getStringList("scoreboard.banned.lines")) {
                if (line.contains("<queue>")) {
                    handleQueueLines(player, lines);
                    continue;
                }

                if (line.contains("<timers>")) {
                    handleTimerLines(lines);
                    continue;
                }

                if (line.contains("<footer>")) {
                    lines.add(ScoreboardAnimated.getFooter());
                    continue;
                }

                line = handleRankPlaceholders(player, line);
                line = handleBannedPlaceholders(player, line);

                lines.add(line);
            }
        } else {
            for (String line : plugin.getScoreboardFile().getStringList("scoreboard.lines")) {
                if (line.contains("<queue>")) {
                    handleQueueLines(player, lines);
                    continue;
                }

                if (line.contains("<timers>")) {
                    handleTimerLines(lines);
                    continue;
                }

                if (line.contains("<footer>")) {
                    lines.add(ScoreboardAnimated.getFooter());
                    continue;
                }

                line = handleRankPlaceholders(player, line);
                lines.add(line);
            }
        }

        return ChatUtil.placeholder(player, lines);
    }

    private boolean isBanned(Player player) {
        return banManager != null && banManager.getBan() != null && banManager.getBan().isBanned(player.getUniqueId());
    }

    private String getBanReason(Player player) {
        return banManager != null && banManager.getBan() != null
                ? banManager.getBan().getBanReason(player.getUniqueId())
                : "Unknown";
    }

    private String getBanTimeLeft(Player player) {
        if (banManager == null || banManager.getBan() == null) {
            return "Unknown";
        }
        long duration = banManager.getBan().getBanDuration(player.getUniqueId());
        return duration == -1 ? "Permanent" : JavaUtil.formatMillis(duration);
    }

    private void handleQueueLines(Player player, List<String> lines) {
        Queue queue = queueManager.getQueueByPlayer(player);
        if (queue != null) {
            for (String queueLine : plugin.getScoreboardFile().getStringList("scoreboard.addons.queue")) {
                lines.add(queueLine
                        .replace("<queue-player-name>", queue.getServer())
                        .replace("<queue_player_position>", String.valueOf(queueManager.getQueuePlayerPosition(queue, player.getUniqueId())))
                        .replace("<queue-player-size>", String.valueOf(queue.getPlayers().size())));
            }
        }
    }

    private void handleTimerLines(List<String> lines) {
        for (Timer timer : timerManager.getTimers().values()) {
            for (String timerLine : plugin.getScoreboardFile().getStringList("scoreboard.addons.timers")) {
                lines.add(timerLine
                        .replace("<timer-name>", timer.getPrefix())
                        .replace("<timer-time>", JavaUtil.formatMillis(timer.getRemaining())));
            }
        }
    }

    private String handleBannedPlaceholders(Player player, String line) {
        if (line.contains("%ban-reason%")) line = line.replace("%ban-reason%", getBanReason(player));
        if (line.contains("%ban-expiration%")) line = line.replace("%ban-expiration%", getBanTimeLeft(player));

        return ChatUtil.placeholder(player, line);
    }

    private String handleRankPlaceholders(Player player, String line) {
        if (line.contains("%rank-prefix%")) {
            line = line.replace("%rank-prefix%", getRankManager().getRank().getPrefix(player.getUniqueId()));
        }
        if (line.contains("%rank-suffix%")) {
            line = line.replace("%rank-suffix%", getRankManager().getRank().getSuffix(player.getUniqueId()));
        }
        if (line.contains("%rank-name%")) {
            line = line.replace("%rank-name%", getRankManager().getRank().getRankName(player.getUniqueId()));
        }
        return line;
    }

    private RankManager getRankManager() {
        return plugin.getRankManager();
    }
}