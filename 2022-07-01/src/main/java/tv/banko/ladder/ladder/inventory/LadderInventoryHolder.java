package tv.banko.ladderbingo.ladder.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import tv.banko.ladderbingo.LadderBingo;

public record LadderInventoryHolder(LadderBingo ladder, Player player, int taskId) implements InventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return ladder.getServer().createInventory(null, 9);
    }
}
