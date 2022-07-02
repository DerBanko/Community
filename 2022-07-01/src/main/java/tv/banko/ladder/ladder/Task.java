package tv.banko.ladderbingo.ladder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tv.banko.ladderbingo.LadderBingo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Task implements Comparable<Task> {

    protected final LadderBingo ladder;

    protected final int id;
    protected final Map<UUID, TaskState> state;

    public Task(LadderBingo ladder, int id) {
        this.ladder = ladder;
        this.id = id;
        this.state = this.ladder.getTaskConfig().getStatesOfTask(this);
    }

    public TaskState getState(UUID uuid) {
        return state.getOrDefault(uuid, TaskState.LOCKED);
    }

    public TaskState getState(Player player) {
        return getState(player.getUniqueId());
    }

    public void setState(Player player, TaskState state) {
        this.state.put(player.getUniqueId(), state);

        switch (state) {
            case TASK -> player.sendMessage(ladder.getPrefix()
                    .append(Component.text(ladder.getTranslation().get("message.task"), NamedTextColor.GRAY)
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{0}")
                                    .replacement(getDisplayItem(player).getItemMeta().displayName())
                                    .build())));
            case REACHED -> {
                player.sendMessage(ladder.getPrefix()
                        .append(Component.text(ladder.getTranslation().get("message.reached"), NamedTextColor.GRAY)
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{0}")
                                        .replacement(Component.text(id, NamedTextColor.BLUE))
                                        .build())
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{1}")
                                        .replacement(getDisplayItem(player).getItemMeta().displayName())
                                        .build())));

                if (id == 30) {
                    // TODO: Finished
                    return;
                }

                ladder.getLadder().getTask(id + 1).setState(player, TaskState.TASK);
            }
        }
    }

    public int getId() {
        return id;
    }

    public abstract ItemStack getDisplayItem(Player player);

    public abstract boolean hasReached(Player player);

    @Override
    public int compareTo(@NotNull Task task) {
        return Integer.compare(id, task.id);
    }

    public @NotNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        this.state.forEach((uuid, taskState) -> map.put(uuid.toString(), taskState.name()));
        return map;
    }
}
