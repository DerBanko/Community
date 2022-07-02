package tv.banko.core.listener.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import tv.banko.core.Core;
import tv.banko.core.game.Game;
import tv.banko.core.translation.CoreTranslation;

public record JoinListener(Core core) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(Component.text("+ ", NamedTextColor.GREEN)
                .append(player.displayName().color(NamedTextColor.GRAY)));

        Scoreboard scoreboard = core.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("default");

        if (team == null) {
            team = scoreboard.registerNewTeam("default");
            team.color(NamedTextColor.BLUE);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        team.addEntity(player);

        CoreTranslation translation = core.getTranslation();

        player.sendPlayerListHeaderAndFooter(Component.newline()
                        .append(Component.text(translation.get("list.header.ip"), TextColor.color(0x1857de)))
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(Component.text(translation.get("list.header.info"), TextColor.color(0xebb92e))
                                .append(Component.newline())),
                Component.newline()
                        .append(Component.text("Twitch", NamedTextColor.GRAY))
                        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(translation.get("list.footer.twitch"), TextColor.color(0x6441a5)))
                        .append(Component.newline())
                        .append(Component.text("Twitter", NamedTextColor.GRAY))
                        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(translation.get("list.footer.twitter"), TextColor.color(0x1DA1F2)))
                        .append(Component.newline())
                        .append(Component.text("  YouTube", NamedTextColor.GRAY))
                        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(translation.get("list.footer.youtube"), TextColor.color(0xFF0000)))
                        .append(Component.text("  "))
                        .append(Component.newline()));

        Game game = core.getGame();

        if (game == null) {
            return;
        }

        if (!game.getPlayers().isSpectator(player.getUniqueId())) {
            return;
        }

        event.joinMessage(Component.empty());
    }

}
