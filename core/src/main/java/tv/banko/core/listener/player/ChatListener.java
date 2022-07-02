package tv.banko.core.listener.player;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tv.banko.core.Core;
import tv.banko.core.game.Game;

public record ChatListener(Core core) implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Game game = core.getGame();

        if (game == null) {
            return;
        }

        if (game.getPlayers().isSpectator(player.getUniqueId())) {
            event.renderer((source, sourceDisplayName, message, viewer) ->
                    Component.text("† ", NamedTextColor.DARK_GRAY)
                            .append(Component.text(source.getName(), NamedTextColor.GRAY))
                            .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                            .append(message.color(NamedTextColor.GRAY)));
            return;
        }

        event.renderer((source, sourceDisplayName, message, viewer) ->
                Component.text(source.getName(), NamedTextColor.GRAY)
                        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(message.color(NamedTextColor.GRAY)));
    }

}
