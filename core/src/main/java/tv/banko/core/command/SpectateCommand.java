package tv.banko.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tv.banko.core.Core;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.translation.FunctionTranslation;

public record StartCommand(Core core) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Game game = core.getGame();

        if (game == null) {
            sender.sendMessage(Component.text(core.getTranslation().get("global.no-permission", NamedTextColor.RED)));
            return true;
        }

        FunctionTranslation translation = core.getFunction().getTranslation();

        if (!sender.hasPermission("game.command.start")) {
            sender.sendMessage(Component.text(core.getTranslation().get("global.no-permission", NamedTextColor.RED)));
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
