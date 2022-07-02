package tv.banko.ladder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.game.GameTime;
import tv.banko.ladder.config.Config;
import tv.banko.ladder.ladder.LadderManager;
import tv.banko.ladder.ladder.Task;
import tv.banko.ladder.ladder.TaskState;
import tv.banko.ladder.listener.PreRoundListeners;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Ladder extends Game {

    private Config config;
    private LadderManager ladder;

    private boolean ended;

    public Ladder() {
        super("Ladder", NamedTextColor.BLUE);
        this.ended = false;
    }

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        this.config = new Config(this);
        this.ladder = new LadderManager(this);

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PreRoundListeners(this), this);

        this.getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.SPAWN_RADIUS, 15);
        });
    }

    @Override
    public void disable() {
        this.config.saveTasks();
    }

    @Override
    public void task(BukkitTask task) {
        super.task(task);

        switch (state) {
            case STARTING -> timeMessage.broadcastStarting();
            case RUNNING -> {
                timeMessage.broadcastIngame();

                int seconds = this.time.getTime();
                long hours = TimeUnit.SECONDS.toHours(seconds);

                seconds -= TimeUnit.HOURS.toSeconds(hours);

                long minutes = TimeUnit.SECONDS.toMinutes(seconds);

                seconds -= TimeUnit.MINUTES.toSeconds(minutes);

                Component timer = Component.text((hours < 10 ? "0" : "") + hours)
                        .append(Component.text(":"))
                        .append(Component.text((minutes < 10 ? "0" : "") + minutes))
                        .append(Component.text(":"))
                        .append(Component.text((seconds < 10 ? "0" : "") + seconds))
                        .color(NamedTextColor.BLUE)
                        .decorate(TextDecoration.BOLD);

                this.getServer().getOnlinePlayers().forEach(player -> {
                    if (this.players.isSpectator(player.getUniqueId())) {
                        player.sendActionBar(Component.text(translation.get("actionbar.spectator"), NamedTextColor.GRAY)
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{0}")
                                        .replacement(timer)
                                        .build()));
                        return;
                    }

                    Task currentTask = this.ladder.getCurrentTask(player);

                    if (currentTask == null) {
                        return;
                    }

                    player.sendActionBar(Component.text(translation.get("actionbar.player"), NamedTextColor.GRAY)
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{0}")
                                    .replacement(Objects.requireNonNull(currentTask.getDisplayItem(player).getItemMeta().displayName())
                                            .color(NamedTextColor.BLUE))
                                    .build())
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{1}")
                                    .replacement(timer)
                                    .build()));
                });
            }
        }
    }

    public Config getTaskConfig() {
        return config;
    }

    public LadderManager getLadder() {
        return ladder;
    }

    @Override
    public void setState(GameState state) {
        if (state.equals(this.state)) {
            return;
        }

        super.setState(state);

        switch (state) {
            case STARTING -> {
                this.time.setTime(61);
                this.time.setType(GameTime.Type.DESCENDING);
            }
            case RUNNING -> {
                this.getServer().getOnlinePlayers().forEach(player -> {
                    if (this.players.isSpectator(player.getUniqueId())) {
                        return;
                    }

                    this.players.addPlayer(player.getUniqueId());
                    this.getLadder().getTask(1).setState(player, TaskState.Type.TASK);
                });
                this.time.setTime((60 * 60 * 4) + 1);
                this.time.setType(GameTime.Type.DESCENDING);
            }
            case FINISHED -> {
                this.time.setTime(0);
                this.time.setType(GameTime.Type.PAUSED);

                if (this.ended) {
                    return;
                }

                broadcastWinner(getWinner());
            }
        }
    }

    public void broadcastSpectators(Component message) {
        this.players.getSpectators().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                return;
            }

            player.sendMessage(message);
        });
    }

    public void broadcastWinner(OfflinePlayer player) {
        this.ended = true;
        setState(GameState.FINISHED);

        this.getServer().broadcast(Component.text(""));
        this.getServer().broadcast(getPrefix().append(Component.text(translation.get("message.winner"), NamedTextColor.GOLD)
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(Component.text("" + player.getName(), NamedTextColor.BLUE))
                        .build())));
        this.getServer().broadcast(Component.text(""));

        int placement = 1;

        for (TaskState state : this.ladder.getReversedTaskStates()) {
            Player target = this.getServer().getPlayer(state.getUUID());

            if (target == null) {
                continue;
            }

            target.sendMessage(getPrefix().append(Component.text(translation.get("message.placement"), NamedTextColor.GRAY)
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(Component.text(placement, NamedTextColor.BLUE))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{1}")
                            .replacement(Component.text(state.getTaskId(), NamedTextColor.BLUE))
                            .build())));
            placement++;
        }
    }

    private OfflinePlayer getWinner() {
        Optional<TaskState> optional = this.ladder.getReversedTaskStates().stream().findFirst();

        if (optional.isEmpty()) {
            throw new NullPointerException("No winner");
        }

        return getServer().getOfflinePlayer(optional.get().getUUID());
    }
}
