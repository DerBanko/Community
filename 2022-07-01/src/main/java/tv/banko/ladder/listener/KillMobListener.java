package tv.banko.ladder.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import tv.banko.ladder.ladder.LadderManager;
import tv.banko.ladder.ladder.TaskState;
import tv.banko.ladder.ladder.task.KillMobTask;

public record KillMobListener(LadderManager manager) implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        EntityDamageEvent damageCause = event.getEntity().getLastDamageCause();

        if (damageCause == null) {
            return;
        }

        if (!(damageCause instanceof EntityDamageByEntityEvent cause)) {
            return;
        }

        if (cause.getDamager() instanceof Player player) {
            check(event, player);
            return;
        }

        if (!(cause.getDamager() instanceof Projectile projectile)) {
            return;
        }

        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }

        check(event, player);
    }

    private void check(EntityDeathEvent event, Player player) {
        for (KillMobTask task : manager.getKillMobTasks()) {
            if (!task.getEntityType().equals(event.getEntityType())) {
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
