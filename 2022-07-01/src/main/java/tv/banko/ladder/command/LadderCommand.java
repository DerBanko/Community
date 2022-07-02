package tv.banko.ladder.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tv.banko.core.game.GameState;
import tv.banko.core.translation.Translation;
import tv.banko.ladder.Ladder;
import tv.banko.ladder.ladder.TaskState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LadderCommand(Ladder ladder) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Translation translation = this.ladder.getTranslation();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(this.ladder.getPrefix().append(Component.text(translation
                    .get("command.no-player"), NamedTextColor.RED)));
            return true;
        }

        if (!ladder.getState().equals(GameState.RUNNING)) {
            sender.sendMessage(this.ladder.getPrefix().append(Component.text(translation
                    .get("command.not-ingame"), NamedTextColor.RED)));
            return true;
        }

        if (ladder.getPlayers().isSpectator(player.getUniqueId())) {
            List<TaskState> list = ladder.getLadder().getReversedTaskStates();

            Component component = ladder.getPrefix().append(Component.text(translation
                    .get("command.list-top-10"), NamedTextColor.GRAY));

            Map<Integer, Long> map = new HashMap<>();

            for (int i = 0; i < 10; i++) {
                if (list.size() <= i) {
                    break;
                }

                TaskState state = list.get(i);
                OfflinePlayer offlinePlayer = ladder.getServer().getOfflinePlayer(state.getUUID());

                long fastest = state.getReached();

                if (map.containsKey(state.getTaskId())) {
                    fastest = map.get(state.getTaskId());
                } else {
                    map.put(state.getTaskId(), fastest);
                }

                long time = state.getReached() - fastest;

                ItemStack item = this.ladder.getLadder().getTask(state.getTaskId()).getDisplayItem(player);

                component = component.append(Component.newline()
                        .append(Component.text((i + 1) + ". ", NamedTextColor.GRAY))
                        .append(Component.text("" + offlinePlayer.getName(), NamedTextColor.BLUE))
                        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(state.getTaskId(), NamedTextColor.BLUE))
                        .append(Component.text(". Aufgabe", NamedTextColor.GRAY))
                        .append(Component.text(" (", NamedTextColor.DARK_GRAY))
                        .append(time <= 0 ? Component.text("+0.0", NamedTextColor.GOLD) :
                                Component.text("+" + (time / 1000D), NamedTextColor.RED))
                        .append(Component.text(")", NamedTextColor.DARK_GRAY))
                        .hoverEvent(item.asHoverEvent()));
            }

            player.sendMessage(component);
            return true;
        }

        player.openInventory(ladder.getLadder().getInventory().getInventory(player));
        return true;
    }
}
