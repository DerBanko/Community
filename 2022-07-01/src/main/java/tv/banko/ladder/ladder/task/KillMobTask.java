package tv.banko.ladder.ladder.task;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tv.banko.core.game.GameState;
import tv.banko.ladder.Ladder;
import tv.banko.ladder.ladder.Task;
import tv.banko.ladder.ladder.TaskState;
import tv.banko.ladder.util.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class KillMobTask extends Task {

    private final EntityType entityType;

    public KillMobTask(Ladder ladder, int id, EntityType entityType) {
        super(ladder, id);
        this.entityType = entityType;
    }

    @Override
    public ItemStack getDisplayItem(Player player) {
        List<Component> list = new ArrayList<>();

        for (String s : ladder.getTranslation().get("task.mob.lore").split("\n")) {
            list.add(Component.text(s, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        ItemStack spawnEgg = ladder.getServer().getItemFactory().getSpawnEgg(entityType);

        return new TaskItem(spawnEgg == null ? Material.SKELETON_SKULL : spawnEgg.getType(),
                Component.text(ladder.getTranslation().get("task.mob.name"))
                        .replaceText(TextReplacementConfig.builder()
                                .matchLiteral("{0}")
                                .replacement(Component.translatable(entityType.translationKey()))
                                .build()), list, getState(player), ladder).toItemStack();
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean hasReached(Player player) {
        if (!ladder.getState().equals(GameState.RUNNING)) {
            return false;
        }

        return getState(player).getType() == TaskState.Type.REACHED;
    }
}
