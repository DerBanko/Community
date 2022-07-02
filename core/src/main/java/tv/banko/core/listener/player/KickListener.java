package tv.banko.core.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import tv.banko.core.Core;

public record KickListener(Core core) implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        event.leaveMessage(Component.text(" - ", NamedTextColor.DARK_GREEN)
                .append(player.displayName().color(NamedTextColor.GRAY)));
    }

}
