package tv.banko.ladder.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import tv.banko.core.game.GameState;
import tv.banko.ladder.Ladder;

public record PreRoundListeners(Ladder ladder) implements Listener {

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

        Location clone = event.getFrom().clone();
        clone.setY(event.getTo().getY());

        if (clone.distance(event.getTo()) == 0) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        if (ladder.getState().equals(GameState.RUNNING)
                || ladder.getState().equals(GameState.FINISHED)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (ladder.getState().equals(GameState.RUNNING)
                || ladder.getState().equals(GameState.FINISHED)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (ladder.getState().equals(GameState.RUNNING)
                || ladder.getState().equals(GameState.FINISHED)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {

        if (ladder.getState().equals(GameState.RUNNING)
                || ladder.getState().equals(GameState.FINISHED)) {
            return;
        }

        event.setCancelled(true);
    }
}
