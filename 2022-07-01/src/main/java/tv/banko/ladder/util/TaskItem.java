package tv.banko.ladder.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import tv.banko.core.builder.ItemBuilder;
import tv.banko.ladder.Ladder;
import tv.banko.ladder.ladder.TaskState;

import java.util.List;

public class TaskItem {

    private final Ladder ladder;
    private final ItemBuilder builder;

    public TaskItem(Material material, Component displayName, List<Component> lores, TaskState state, Ladder ladder) {
        this.ladder = ladder;

        switch (state.getType()) {
            case TASK -> this.builder = getTask(material, displayName, lores);
            case REACHED -> this.builder = getReached(material, displayName, lores);
            default -> this.builder = getLocked();
        }
    }

    public ItemStack toItemStack() {
        return builder.build().clone();
    }

    private ItemBuilder getLocked() {
        ItemBuilder builder = new ItemBuilder(Material.BARRIER);

        builder.setDisplayName(Component.text(ladder.getTranslation().get("taskitem.locked.name"), NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false));

        builder.addLore("");

        for (String s : ladder.getTranslation().get("taskitem.locked.lore").split("\n")) {
            builder.addLore(Component.text(s, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        builder.addLore("");

        builder.addLore(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("❌", NamedTextColor.RED))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(ladder.getTranslation().get("taskitem.state.locked"), NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false));

        return builder;
    }

    private ItemBuilder getTask(Material material, Component displayName, List<Component> lores) {
        ItemBuilder builder = new ItemBuilder(material, displayName.color(NamedTextColor.BLUE)
                .decoration(TextDecoration.ITALIC, false));

        builder.addLore("");
        lores.forEach(builder::addLore);
        builder.addLore("");

        builder.addLore(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("⌚", NamedTextColor.BLUE))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(ladder.getTranslation().get("taskitem.state.task"), NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false));

        return builder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
    }

    private ItemBuilder getReached(Material material, Component displayName, List<Component> lores) {
        ItemBuilder builder = new ItemBuilder(material, displayName.color(NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false));

        builder.addLore("");
        lores.forEach(builder::addLore);
        builder.addLore("");

        builder.addLore(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("✔", NamedTextColor.GREEN))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(ladder.getTranslation().get("taskitem.state.reached"), NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false));

        return builder.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
    }
}
