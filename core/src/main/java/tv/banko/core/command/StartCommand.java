package tv.banko.core.game.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.translation.FunctionTranslation;

public record StartCommand(Game game) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        FunctionTranslation translation = new FunctionTranslation();

        if (!sender.hasPermission("game.command.start")) {
            sender.sendMessage(Component.text(translation.get("global.no-permission", NamedTextColor.RED)));
            return true;
        }

        if (!game.getState().equals(GameState.WAITING)) {
            sender.sendMessage(Component.text(translation.get("command.start.already-started", NamedTextColor.RED)));
            return true;
        }

        game.setState(GameState.STARTING);
        sender.sendMessage(Component.text(translation.get("command.start.success", NamedTextColor.RED)));
        return true;
    }

}
