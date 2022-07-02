package tv.banko.core.function.command;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tv.banko.core.Core;
import tv.banko.core.function.CommandFunction;

import java.util.Collections;
import java.util.List;

/**
 * The GlobalMute may be toggled by using the command <b>/globalmute.</b><br><br>
 * It disables chatting in public chat when chatters don't have the following permission: <code>function.globalmute.chat</code><br>
 * To use the command you need the following permission: <code>function.globalmute.command</code>
 */
public class GlobalMute extends CommandFunction {

    private final Core core;

    public GlobalMute(Core core) {
        super(core, core.getFunction().getTranslation().get("globalmute.name"), "globalmute", false);
        this.core = core;
    }

    @Override
    public void enable() {
        plugin.getServer().broadcast(getPrefix().append(Component.text(core.getFunction().getTranslation()
                .get("globalmute.broadcast.enable"), NamedTextColor.RED)));
    }

    @Override
    public void disable() {
        plugin.getServer().broadcast(getPrefix().append(Component.text(core.getFunction().getTranslation()
                .get("globalmute.broadcast.disable"), NamedTextColor.GREEN)));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("function.globalmute.command")) {
            sender.sendMessage(getPrefix().append(Component.text(core.getTranslation()
                    .get("global.no-permission"), NamedTextColor.RED)));
            return true;
        }

        setStatus(!status);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if (isDisabled()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.hasPermission("function.globalmute.chat")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(getPrefix().append(Component.text(core.getFunction().getTranslation()
                .get("globalmute.chat-while-disabled"), NamedTextColor.RED)));
    }
}
