package tv.banko.ladder.ladder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tv.banko.ladder.Ladder;

import java.util.*;

public abstract class Task implements Comparable<Task> {

    protected final Ladder ladder;

    protected final int id;
    protected final List<TaskState> state;

    public Task(Ladder ladder, int id) {
        this.ladder = ladder;
        this.id = id;
        this.state = this.ladder.getTaskConfig().getStatesOfTask(this);
    }

    public TaskState getState(UUID uuid) {
        return state.stream().filter(taskState -> taskState.getUUID().equals(uuid)).findFirst().orElseGet(() -> {
            TaskState taskState = new TaskState(uuid, id);
            this.state.add(taskState);
            return taskState;
        });
    }

    public TaskState getState(Player player) {

        if (this.ladder.getPlayers().isSpectator(player.getUniqueId())) {
            return new TaskState(player.getUniqueId(), this.id, TaskState.Type.REACHED, System.currentTimeMillis());
        }

        return getState(player.getUniqueId());
    }

    public List<TaskState> getReached() {
        List<TaskState> list = new ArrayList<>();

        state.forEach((taskState) -> {
            if (!taskState.getType().equals(TaskState.Type.REACHED)) {
                return;
            }

            list.add(taskState);
        });
        return list;
    }

    public void setState(Player player, TaskState.Type type) {
        TaskState taskState = getState(player);
        taskState.setType(type);

        switch (type) {
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

                ladder.broadcastSpectators(ladder.getPrefix()
                        .append(Component.text(ladder.getTranslation().get("message.spectator-reached"), NamedTextColor.GRAY)
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{0}")
                                        .replacement(Component.text(player.getName(), NamedTextColor.BLUE))
                                        .build())
                                .replaceText(TextReplacementConfig.builder()
                                        .matchLiteral("{1}")
                                        .replacement(Component.text(id, NamedTextColor.BLUE))
                                        .build()))
                        .clickEvent(ClickEvent.runCommand("/tp " + player.getName())));

                if (id == 30) {
                    ladder.broadcastWinner(player);
                    return;
                }

                ladder.getLadder().getTask(id + 1).setState(player, TaskState.Type.TASK);
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
        this.state.forEach((state) -> map.put(state.getUUID().toString(), state.toString()));
        return map;
    }
}
