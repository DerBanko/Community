package tv.banko.ladder.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import tv.banko.ladder.ladder.LadderManager;
import tv.banko.ladder.ladder.TaskState;
import tv.banko.ladder.ladder.task.BreakBlockTask;

public record BreakBlockListener(LadderManager manager) implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        for (BreakBlockTask task : manager.getBreakBlockTasks()) {
            if (!task.getMaterial().equals(event.getBlock().getType())) {
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
