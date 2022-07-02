package tv.banko.ladderbingo.ladder.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tv.banko.core.builder.InventoryBuilder;
import tv.banko.core.builder.ItemBuilder;
import tv.banko.ladderbingo.LadderBingo;
import tv.banko.ladderbingo.ladder.Task;

public class LadderInventory {

    private final LadderBingo ladder;

    public LadderInventory(LadderBingo ladder) {
        this.ladder = ladder;
    }

    public Inventory getInventory(Player player) {
        return getInventory(player, 1);
    }

    public Inventory getInventory(Player player, int taskId) {
        LadderInventoryHolder holder = new LadderInventoryHolder(this.ladder, player, taskId);

        InventoryBuilder builder = new InventoryBuilder(this.ladder.getServer()
                .createInventory(holder, 27, Component.text(this.ladder.getTranslation().get("inventory.title"), NamedTextColor.BLUE)))
                .fillInventory(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE))
                .setItem(getArrowLeft(holder), 0, 18)
                .setItem(getArrowRight(holder), 8, 26);

        int slot = 10;

        for (Task task : ladder.getLadder().getTaskRange(taskId)) {
            builder.setItem(task.getDisplayItem(player), slot++);
        }

        return builder.build();
    }

    public ItemStack getArrowLeft(LadderInventoryHolder holder) {

        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);

        if (holder.taskId() <= 1) {
            builder.setDisplayName(Component.text(ladder.getTranslation().get("inventory.left"), NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.STRIKETHROUGH))
                    .setBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=");
        } else {
            builder.setDisplayName(Component.text(ladder.getTranslation().get("inventory.left"), NamedTextColor.BLUE)
                            .decoration(TextDecoration.ITALIC, false))
                    .setBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19");
        }

        return builder.build();
    }

    public ItemStack getArrowRight(LadderInventoryHolder holder) {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);

        if (holder.taskId() >= 21) {
            builder.setDisplayName(Component.text(ladder.getTranslation().get("inventory.right"), NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.STRIKETHROUGH))
                    .setBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==");
        } else {
            builder.setDisplayName(Component.text(ladder.getTranslation().get("inventory.right"), NamedTextColor.BLUE)
                            .decoration(TextDecoration.ITALIC, false))
                    .setBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19");
        }

        return builder.build();
    }
}
