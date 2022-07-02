package tv.banko.core.function.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tv.banko.core.Core;
import tv.banko.core.api.UserAPI;
import tv.banko.core.function.CommandFunction;
import tv.banko.core.translation.FunctionTranslation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Whitelist extends CommandFunction {

    private final Core core;
    private final List<UUID> whitelist;

    public Whitelist(Core core) {
        super(core, core.getFunction().getTranslation().get("whitelist.name"), "whitelist", true);
        this.core = core;
        this.whitelist = new ArrayList<>();
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void load(YamlConfiguration config) {
        if (!config.contains("whitelist.uuids")) {
            return;
        }

        config.getStringList("whitelist.uuids").forEach(s -> this.whitelist.add(UUID.fromString(s)));
    }

    @Override
    public void save(YamlConfiguration config) {
        List<String> list = new ArrayList<>();

        this.whitelist.forEach(uuid -> list.add(uuid.toString()));

        config.set("whitelist.uuids", list);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("function.globalmute.command")) {
            sender.sendMessage(getPrefix().append(Component.text(core.getTranslation().get("global.no-permission"), NamedTextColor.RED)));
            return true;
        }

        FunctionTranslation translation = core.getFunction().getTranslation();

        if (args.length == 0) {
            sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.usage"), NamedTextColor.GRAY)));
            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length != 2) {
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.usage-add"), NamedTextColor.GRAY)));
                    return true;
                }

                String playerName = args[1];

                sender.sendMessage(getPrefix().append(Component.text(core.getTranslation().get("global.api-request"), NamedTextColor.GRAY)));

                UserAPI.getUUIDByName(playerName).whenCompleteAsync((uuid, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.not-found"), NamedTextColor.RED)));
                        return;
                    }

                    if (whitelist.contains(uuid)) {
                        sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.user-already-added", playerName), NamedTextColor.RED)));
                        return;
                    }

                    whitelist.add(uuid);
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.user-added"), NamedTextColor.GRAY)).replaceText(TextReplacementConfig.builder().matchLiteral("{0}").replacement(Component.text(playerName, NamedTextColor.GREEN)).build()));
                });
            }
            case "remove" -> {
                if (args.length != 2) {
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.usage-remove"), NamedTextColor.GRAY)));
                    return true;
                }

                String playerName = args[1];

                sender.sendMessage(getPrefix().append(Component.text(core.getTranslation().get("global.api-request"), NamedTextColor.GRAY)));

                UserAPI.getUUIDByName(playerName).whenCompleteAsync((uuid, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.not-found"), NamedTextColor.RED)));
                        return;
                    }

                    if (!whitelist.contains(uuid)) {
                        sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.user-not-added", playerName), NamedTextColor.RED)));
                        return;
                    }

                    whitelist.remove(uuid);
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.user-removed"), NamedTextColor.GRAY)).replaceText(TextReplacementConfig.builder().matchLiteral("{0}").replacement(Component.text(playerName, NamedTextColor.GREEN)).build()));
                });
            }
            case "on" -> {
                if (isEnabled()) {
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.already-enabled"), NamedTextColor.RED)));
                    return true;
                }

                setStatus(true);
                sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.enabled"), NamedTextColor.GREEN)));
            }
            case "off" -> {
                if (isDisabled()) {
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.already-disabled"), NamedTextColor.RED)));
                    return true;
                }

                setStatus(false);
                sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.disabled"), NamedTextColor.GREEN)));
            }
            case "set" -> {
                this.whitelist.clear();

                this.plugin.getServer().getOnlinePlayers().forEach(player -> this.whitelist.add(player.getUniqueId()));

                sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.set"), NamedTextColor.GREEN)));
            }
            case "list" -> new Thread(() -> {
                Component playerList = Component.empty();

                for (UUID uuid : this.whitelist) {
                    OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(uuid);

                    if (player.getName() == null) {
                        return;
                    }

                    if (!playerList.equals(Component.empty())) {
                        playerList = playerList.append(Component.text(", ", NamedTextColor.GRAY));
                    }

                    playerList = playerList.append(Component.text(player.getName(), NamedTextColor.BLUE));
                }

                sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.list"), NamedTextColor.GRAY))
                        .replaceText(TextReplacementConfig.builder()
                                .matchLiteral("{0}")
                                .replacement(Component.text(this.whitelist.size(), NamedTextColor.BLUE))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .matchLiteral("{1}")
                                .replacement(playerList)
                                .build()));
            }).start();
            default ->
                    sender.sendMessage(getPrefix().append(Component.text(translation.get("whitelist.usage"), NamedTextColor.GRAY)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arg0 = Stream.of("add", "remove", "list", "off", "on", "set").sorted().toList();

        switch (args.length) {
            case 0 -> {
                return arg0;
            }
            case 1 -> {
                return arg0.stream().filter(s -> s.startsWith(args[0].toLowerCase())).toList();
            }
            case 2 -> {
                if (!args[0].equalsIgnoreCase("remove")) {
                    return Collections.emptyList();
                }

                List<String> playerList = new ArrayList<>();

                for (UUID uuid : this.whitelist) {
                    OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(uuid);

                    if (player.getName() == null) {
                        continue;
                    }

                    playerList.add(player.getName());
                }

                return playerList;
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (isDisabled()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.hasPermission("function.whitelist.bypass")) {
            return;
        }

        if (this.whitelist.contains(player.getUniqueId())) {
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Component.text(core.getFunction().getTranslation().get("whitelist.not-allowed"), NamedTextColor.RED));
    }
}
