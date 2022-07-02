package tv.banko.core.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.game.GameTime;
import tv.banko.core.translation.CoreTranslation;

import java.util.concurrent.TimeUnit;

public record TimeMessage(Game game) {

    public void broadcastStarting() {
        broadcastTime("time.starting");
    }

    public void broadcastIngame() {
        broadcastTime("time.ingame");
    }

    private void broadcastTime(String prefix) {

        GameTime time = game.getTime();
        CoreTranslation translation = new CoreTranslation();

        int seconds = time.getTime();
        int hours = (int) TimeUnit.SECONDS.toHours(seconds);
        seconds -= TimeUnit.HOURS.toSeconds(hours);
        int minutes = (int) TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);

        switch (hours) {
            case 1 -> {
                if (minutes == 0 && seconds == 0) {
                    game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                            .get(prefix + ".hour"), NamedTextColor.GRAY)));
                }
            }
            case 0 -> {
                switch (minutes) {
                    case 1 -> {
                        if (seconds == 0) {
                            game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                                    .get(prefix + ".minute"), NamedTextColor.GRAY)));
                        }
                    }
                    case 0 -> {
                        if (seconds == 0) {
                            break;
                        }

                        if (seconds == 1) {
                            game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                                    .get(prefix + ".second"), NamedTextColor.GRAY)));
                            break;
                        }

                        if (!(seconds == 2 || seconds == 3 || seconds == 5 || seconds == 10
                                || seconds == 15 || seconds == 30 || seconds == 45)) {
                            break;
                        }

                        game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                                        .get(prefix + ".seconds"), NamedTextColor.GRAY)
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{0}")
                                        .replacement(Component.text(seconds, NamedTextColor.BLUE))
                                        .build())));
                    }
                    default -> {
                        if (seconds == 0) {
                            game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                                            .get(prefix + ".minutes"), NamedTextColor.GRAY)
                                    .replaceText(TextReplacementConfig.builder()
                                            .matchLiteral("{0}")
                                            .replacement(Component.text(minutes, NamedTextColor.BLUE))
                                            .build())));
                        }
                    }
                }
            }
            default -> {
                if (minutes == 0 && seconds == 0) {
                    game.getServer().broadcast(game.getPrefix().append(Component.text(translation
                                    .get(prefix + ".hours"), NamedTextColor.GRAY)
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{0}")
                                    .replacement(Component.text(hours, NamedTextColor.BLUE))
                                    .build())));
                }
            }
        }
    }

    private void playSound() {
        game.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
        });
    }
}