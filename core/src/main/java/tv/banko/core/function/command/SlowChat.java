package tv.banko.core.function.command;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tv.banko.core.Core;
import tv.banko.core.function.CommandFunction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The SlowChat may be toggled by using the command <b>/slowchat.</b><br><br>
 * It disables sending messages in public chat when chatters are chatting too fast and don't have the following permission: <code>function.slowchat.chat</code><br>
 * To use the command you need the following permission: <code>function.slowchat.command</code>
 */
public class SlowChat extends CommandFunction {

    private final Core core;
    private int coolDown;

    public SlowChat(Core core) {
        super(core, core.getFunction().getTranslation().get("slowchat.name"), "slowchat", false);
        this.core = core;
        this.coolDown = 3;
    }

    @Override
    public void enable() {
        plugin.getServer().broadcast(getPrefix().append(Component.text(core.getFunction().getTranslation()
                        .get("slowchat.broadcast.enable"), NamedTextColor.RED))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(Component.text(coolDown, NamedTextColor.DARK_RED))
                        .build()));
    }

    @Override
    public void disable() {
        plugin.getServer().broadcast(getPrefix().append(Component.text(core.getFunction().getTranslation()
                .get("slowchat.broadcast.disable"), NamedTextColor.GREEN)));
    }

    @Override
    public void load(YamlConfiguration config) {
        if (!config.contains("slowchat.cooldown")) {
            return;
        }

        coolDown = config.getInt("slowchat.cooldown");
    }

    @Override
    public void save(YamlConfiguration config) {
        config.set("slowchat.cooldown", coolDown);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("function.slowchat.command")) {
            sender.sendMessage(getPrefix().append(Component.text(core.getTranslation()
                    .get("global.no-permission"), NamedTextColor.RED)));
            return true;
        }

        if (args.length == 0) {
            setStatus(!status);
            return true;
        }

        try {
            int coolDown = Integer.parseInt(args[0]);

            if (coolDown == this.coolDown) {
                sender.sendMessage(getPrefix().append(Component.text(core.getFunction().getTranslation()
                                .get("slowchat.already-same-cooldown", this.coolDown), NamedTextColor.RED)));
                return true;
            }

            if (coolDown == 0) {
                setStatus(false);
                return true;
            }

            this.coolDown = coolDown;
            setStatus(true);
        } catch (NumberFormatException e) {
            sender.sendMessage(getPrefix().append(Component.text(core.getFunction().getTranslation()
                    .get("slowchat.number-as-argument"), NamedTextColor.RED)));
        }

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

        if (player.hasPermission("function.slowchat.chat")) {
            return;
        }

        NamespacedKey key = NamespacedKey.fromString("slowchat", core);
        PersistentDataContainer container = player.getPersistentDataContainer();

        if (key == null) {
            throw new NullPointerException("NamespacedKey 'slowchat' is null");
        }

        if (!container.has(key, PersistentDataType.LONG)) {
            container.set(key, PersistentDataType.LONG, System.currentTimeMillis());
            return;
        }

        long time = Objects.requireNonNull(container.get(key, PersistentDataType.LONG));

        if ((time + (coolDown * 1000L)) <= System.currentTimeMillis()) {
            container.set(key, PersistentDataType.LONG, System.currentTimeMillis());
            return;
        }

        event.setCancelled(true);
        player.sendMessage(getPrefix().append(Component.text(core.getFunction().getTranslation()
                        .get("slowchat.chat-in-cooldown"), NamedTextColor.RED))
                .replaceText(TextReplacementConfig.builder()
                        .replacement(Component.text(((time + (coolDown * 1000L)) - System.currentTimeMillis()) / 1000D, NamedTextColor.RED))
                        .build()));
    }
}
