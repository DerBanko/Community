package tv.banko.ladder.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import tv.banko.core.game.GameState;
import tv.banko.ladder.Ladder;

public record MoveListener(Ladder ladder) implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (ladder.getState().equals(GameState.RUNNING)
                || ladder.getState().equals(GameState.FINISHED)) {
            return;
        }

        if (ladder.getPlayers().isSpectator(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }
}
