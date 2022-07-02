package tv.banko.ladderbingo.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import tv.banko.ladderbingo.ladder.LadderManager;
import tv.banko.ladderbingo.ladder.TaskState;
import tv.banko.ladderbingo.ladder.task.BreakBlockTask;

public record BreakBlockListener(LadderManager manager) implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        for (BreakBlockTask task : manager.getBreakBlockTasks()) {
            if (!task.getMaterial().equals(event.getBlock().getType())) {
                continue;
            }

            if (task.hasReached(player)) {
                continue;
            }

            task.setState(player, TaskState.REACHED);
        }
    }
}
