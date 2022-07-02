package tv.banko.ladderbingo.ladder.task;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tv.banko.ladderbingo.LadderBingo;
import tv.banko.ladderbingo.ladder.Task;
import tv.banko.ladderbingo.ladder.TaskState;
import tv.banko.ladderbingo.util.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class AdvancementTask extends Task {

    private final Advancement advancement;

    public AdvancementTask(LadderBingo ladder, int id, Advancement advancement) {
        super(ladder, id);
        this.advancement = advancement;
    }

    @Override
    public ItemStack getDisplayItem(Player player) {
        List<Component> list = new ArrayList<>();

        if (advancement.getDisplay() == null) {
            throw new NullPointerException("AdvancementDisplay of " + advancement.getKey().value() + " is null");
        }

        for (String s : ladder.getTranslation().get("task.advancement.lore").split("\n")) {
            list.add(Component.text(s, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(advancement.getDisplay().description())
                            .build()));
        }

        return new TaskItem(advancement.getDisplay().icon().getType(), Component.text(ladder.getTranslation().get("task.advancement.name"))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(advancement.getDisplay().title())
                        .build()), list, getState(player), ladder).toItemStack();
    }

    @Override
    public boolean hasReached(Player player) {
        switch (getState(player)) {
            case TASK -> {
                if (!player.getAdvancementProgress(advancement).isDone()) {
                    return false;
                }

                setState(player, TaskState.REACHED);
                return true;
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
