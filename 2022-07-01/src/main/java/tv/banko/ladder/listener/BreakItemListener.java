package tv.banko.ladder.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import tv.banko.ladder.ladder.LadderManager;
import tv.banko.ladder.ladder.TaskState;
import tv.banko.ladder.ladder.task.BreakItemTask;

public record BreakItemListener(LadderManager manager) implements Listener {

    @EventHandler
    public void onBreak(PlayerItemBreakEvent event) {

        Player player = event.getPlayer();

        for (BreakItemTask task : manager.getBreakItemTasks()) {
            if (!task.getMaterial().equals(event.getBrokenItem().getType())) {
                continue;
            }

            if (task.getState(player).getType().equals(TaskState.Type.LOCKED)) {
                continue;
            }

            if (task.hasReached(player)) {
                continue;
            }

            task.setState(player, TaskState.Type.REACHED);
        }
    }
}
