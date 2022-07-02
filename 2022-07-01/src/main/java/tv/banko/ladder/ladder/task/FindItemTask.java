package tv.banko.ladderbingo.ladder.task;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tv.banko.ladderbingo.LadderBingo;
import tv.banko.ladderbingo.ladder.Task;
import tv.banko.ladderbingo.ladder.TaskState;
import tv.banko.ladderbingo.util.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class FindItemTask extends Task {

    private final Material material;

    public FindItemTask(LadderBingo ladder, int id, Material material) {
        super(ladder, id);
        this.material = material;
    }

    @Override
    public ItemStack getDisplayItem(Player player) {
        List<Component> list = new ArrayList<>();

        for (String s : ladder.getTranslation().get("task.find.lore").split("\n")) {
            list.add(Component.text(s, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        return new TaskItem(material, Component.text(ladder.getTranslation().get("task.find.name"))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(Component.translatable(material.translationKey()))
                        .build()), list, getState(player), ladder).toItemStack();
    }

    @Override
    public boolean hasReached(Player player) {
        switch (getState(player)) {
            case TASK -> {
                if (player.getInventory().contains(material)) {
                    setState(player, TaskState.REACHED);
                    return true;
                }

                for (ItemStack itemStack : player.getInventory().getStorageContents()) {
                    if (itemStack == null) {
                        continue;
                    }

                    if (!itemStack.getType().equals(material)) {
                        continue;
                    }

                    setState(player, TaskState.REACHED);
                    return true;
                }

                for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                    if (itemStack == null) {
                        continue;
                    }

                    if (!itemStack.getType().equals(material)) {
                        continue;
                    }

                    setState(player, TaskState.REACHED);
                    return true;
                }

                return false;
            }
            case REACHED -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
