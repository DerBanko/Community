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

public record SpectateCommand(Core core) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Game game = core.getGame();

        if (game == null) {
            sender.sendMessage(Component.text(core.getTranslation().get("global.no-game"), NamedTextColor.RED));
            return true;
        }

        CoreTranslation translation = core.getTranslation();

        if (!sender.hasPermission("game.command.spectate")) {
            sender.sendMessage(game.getPrefix().append(Component.text(core.getTranslation().get("global.no-permission"),
                    NamedTextColor.RED)));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(game.getPrefix().append(Component.text(core.getTranslation().get("command.spectate.usage"),
                    NamedTextColor.GRAY)));
            return true;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if(player != null) {
            changeSpectator(sender, player.getUniqueId(), player.getName());
            return true;
        }

        sender.sendMessage(game.getPrefix().append(Component.text(translation.get("global.api-request"), NamedTextColor.GRAY)));

        UserAPI.getUUIDByName(playerName).whenCompleteAsync((uuid, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                sender.sendMessage(game.getPrefix().append(Component.text(translation.get("command.spectate.not-found",
                                playerName),
                        NamedTextColor.RED)));
                return;
            }

            changeSpectator(sender, uuid, playerName);
        });
        return true;
    }

    private void changeSpectator(CommandSender sender, UUID uuid, String name) {
        Game game = core.getGame();

        if (game.getPlayers().isPlayer(uuid)) {
            game.getPlayers().removePlayer(uuid);
            game.getPlayers().addSpectator(uuid);
            sender.sendMessage(game.getPrefix().append(Component.text(core.getTranslation().get("command.spectate.spectator-added"),
                            NamedTextColor.GRAY)
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(Component.text(name, NamedTextColor.GREEN))
                            .build())));
            return;
        }

        game.getPlayers().removeSpectator(uuid);
        game.getPlayers().addPlayer(uuid);
        sender.sendMessage(game.getPrefix().append(Component.text(core.getTranslation().get("command.spectate.spectator-removed"),
                        NamedTextColor.GRAY)
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(Component.text(name, NamedTextColor.GREEN))
                        .build())));
    }

}
