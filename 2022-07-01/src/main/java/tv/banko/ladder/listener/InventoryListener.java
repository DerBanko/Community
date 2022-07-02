package tv.banko.ladder.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import tv.banko.ladder.ladder.LadderManager;
import tv.banko.ladder.ladder.inventory.LadderInventory;
import tv.banko.ladder.ladder.inventory.LadderInventoryHolder;

public record InventoryListener(LadderManager manager) implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory().getHolder() == null) {
            return;
        }

        if (!(event.getClickedInventory().getHolder() instanceof LadderInventoryHolder holder)) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        LadderInventory inventory = manager.getInventory();

        if (item == null) {
            return;
        }

        if (item.isSimilar(inventory.getArrowLeft(holder))) {

            if (!inventory.isArrowLeft(holder)) {
                return;
            }

            player.openInventory(inventory.getInventory(player, holder.taskId() - 1));
            return;
        }

        if (!item.isSimilar(inventory.getArrowRight(holder))) {
            return;
        }

        if (!inventory.isArrowRight(holder)) {
            return;
        }

        player.openInventory(inventory.getInventory(player, holder.taskId() + 1));
    }
}
