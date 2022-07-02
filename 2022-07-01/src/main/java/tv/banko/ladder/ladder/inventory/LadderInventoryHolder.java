package tv.banko.ladder.ladder.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import tv.banko.ladder.Ladder;

public record LadderInventoryHolder(Ladder ladder, Player player, int taskId) implements InventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return ladder.getServer().createInventory(null, 9);
    }
}
