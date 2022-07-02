package tv.banko.core.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import tv.banko.core.Core;
import tv.banko.core.game.Game;

public record JoinListener(Core core) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(Component.text(" + ", NamedTextColor.DARK_GREEN)
                .append(player.displayName().color(NamedTextColor.GRAY)));

        Scoreboard scoreboard = core.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("default");

        if (team == null) {
            team = scoreboard.registerNewTeam("default");
            team.color(NamedTextColor.BLUE);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        team.addEntity(player);

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
