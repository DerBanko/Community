package tv.banko.core.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.util.CachedServerIcon;
import tv.banko.core.Core;
import tv.banko.core.game.Game;
import tv.banko.core.game.GameState;
import tv.banko.core.translation.CoreTranslation;

import java.io.File;

public record PingListener(Core core) implements Listener {

    @EventHandler
    public void onPing(PaperServerListPingEvent event) throws Exception {
        event.setServerIcon(core.getServer().loadServerIcon(new File("./server-icon.png")));
        event.setMaxPlayers(50);

        CoreTranslation translation = core.getTranslation();

        event.motd(Component.text(translation.get("motd.first.ip"), TextColor.color(0x1857de))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                .append(Component.text(translation.get("motd.first.description"), TextColor.color(0xebb92e)))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                .append(Component.text(translation.get("motd.first.version"), TextColor.color(0x1857de)))
                .append(Component.newline())
                .append(Component.text("               ", NamedTextColor.DARK_GRAY))
                .append(Component.text(translation.get("motd.second.event"), TextColor.color(0xebb92e))));
    }

}
