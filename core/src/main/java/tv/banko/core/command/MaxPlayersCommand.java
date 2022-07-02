package tv.banko.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tv.banko.core.Core;
import tv.banko.core.api.UserAPI;
import tv.banko.core.game.Game;
import tv.banko.core.translation.CoreTranslation;

import java.util.UUID;

public record MaxPlayersCommand(Core core) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Game game = core.getGame();

        if (game == null) {
            sender.sendMessage(Component.text(core.getTranslation().get("global.no-game"), NamedTextColor.RED));
            return true;
        }

        CoreTranslation translation = core.getTranslation();

        if (!sender.hasPermission("game.command.maxplayers")) {
            sender.sendMessage(game.getPrefix().append(Component.text(core.getTranslation().get("global.no-permission"),
                    NamedTextColor.RED)));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(game.getPrefix().append(Component.text("/maxplayers <Count>")));
            return true;
        }

        try {
            core.getServer().setMaxPlayers(Integer.parseInt(args[0]));
            sender.sendMessage(game.getPrefix().append(Component.text("success")));
        } catch (NumberFormatException e) {
            sender.sendMessage(game.getPrefix().append(Component.text("error, no number")));
        }
        return true;
    }
}
