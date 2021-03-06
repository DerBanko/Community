package tv.banko.core.listener.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tv.banko.core.Core;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;

public record LoginListener(Core core) implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Game game = core.getGame();

        if (game == null) {
            return;
        }

        if (game.getPlayers().isSpectator(player.getUniqueId())) {
            event.allow();
            return;
        }

        if (game.getState().equals(GameState.WAITING) ||
                game.getState().equals(GameState.STARTING)) {

            if(core.getServer().getOnlinePlayers().size() >= core.getServer().getMaxPlayers()) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, Component.text(core.getTranslation()
                        .get("game.full"), NamedTextColor.RED));
                return;
            }

            return;
        }

        if (game.getPlayers().isPlayer(player.getUniqueId())) {
            event.allow();
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Component.text(core.getTranslation()
                .get("game.already-started"), NamedTextColor.RED));
    }

}
