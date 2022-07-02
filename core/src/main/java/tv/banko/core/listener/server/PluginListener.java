package tv.banko.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import tv.banko.core.Core;
import tv.banko.core.game.Game;

public record PluginListener(Core core) implements Listener {

    @EventHandler
    public void onPlugin(PluginEnableEvent event) {
        if (!(event.getPlugin() instanceof Game game)) {
            return;
        }

        if (core.getGame() != null) {
            throw new IllegalPluginAccessException("You must not add to events at the same time.");
        }

        core.setGame(game);
    }
}
