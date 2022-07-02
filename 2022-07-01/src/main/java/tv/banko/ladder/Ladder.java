package tv.banko.ladderbingo;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scheduler.BukkitTask;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.game.GameTime;
import tv.banko.ladderbingo.config.Config;
import tv.banko.ladderbingo.ladder.LadderManager;

public class LadderBingo extends Game {

    private Config config;
    private LadderManager ladder;

    public LadderBingo() {
        super("LadderBingo", NamedTextColor.BLUE);
    }

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        this.config = new Config(this);
        this.ladder = new LadderManager(this);
    }

    @Override
    public void disable() {
        this.config.saveTasks();
    }

    @Override
    public void task(BukkitTask task) {
        super.task(task);
        switch (state) {
            case STARTING -> {
                int minutes = this.time.getTime() / 60;
                int seconds = this.time.getTime() % 60;

                switch (minutes) {
                    case 1 -> {
                        if (seconds != 0) {
                            break;
                        }

                        this.getServer().broadcast(getPrefix()
                                .append(Component.text(translation.get("time.starting.minute"), NamedTextColor.GRAY)));
                    }
                    case 0 -> {
                        if (seconds == 0) {
                            break;
                        }

                        if (seconds == 1) {
                            this.getServer().broadcast(getPrefix()
                                    .append(Component.text(translation.get("time.starting.second"), NamedTextColor.GRAY)));
                            break;
                        }

                        this.getServer().broadcast(getPrefix()
                                .append(Component.text(translation.get("time.starting.seconds"), NamedTextColor.GRAY)
                                        .replaceText(TextReplacementConfig.builder()
                                                .replacement(Component.text(seconds, NamedTextColor.BLUE))
                                                .build())));
                    }
                    default -> {
                        if (seconds != 0) {
                            break;
                        }

                        this.getServer().broadcast(getPrefix()
                                .append(Component.text(translation.get("time.starting.minutes"), NamedTextColor.GRAY)
                                        .replaceText(TextReplacementConfig.builder()
                                                .replacement(Component.text(minutes, NamedTextColor.BLUE))
                                                .build())));
                    }
                }

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
        super.setState(state);

        switch (state) {
            case STARTING -> {
                this.time.setTime(60);
                this.time.setType(GameTime.Type.DESCENDING);
            }
        }
    }
}
