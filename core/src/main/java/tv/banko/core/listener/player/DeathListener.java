package tv.banko.core.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import tv.banko.core.Core;
import tv.banko.core.game.Game;

public record DeathListener(Core core) implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Game game = core.getGame();

        if (game == null) {
            return;
        }

        event.deathMessage(Component.text("† ", NamedTextColor.DARK_GRAY)
                .append(Component.text(player.getName(), NamedTextColor.BLUE)));
    }

}
