package tv.banko.ladderbingo.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tv.banko.ladderbingo.LadderBingo;

public record LadderCommand(LadderBingo ladder) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(this.ladder.getPrefix().append(Component.text(this.ladder.getTranslation().get("command.no-player"), NamedTextColor.RED)));
            return true;
        }

        player.openInventory(ladder.getLadder().getInventory().getInventory(player));
        return true;
    }
}
