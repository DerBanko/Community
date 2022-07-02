package tv.banko.core.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {

    private final Inventory inventory;

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder(InventoryHolder holder, int slots, Component component) {
        this(Bukkit.createInventory(holder, slots, component));
    }

    public InventoryBuilder(InventoryHolder holder, InventoryType type, Component component) {
        this(Bukkit.createInventory(holder, type, component));
    }

    public InventoryBuilder(InventoryHolder holder, int slots) {
        this(Bukkit.createInventory(holder, slots));
    }

    public InventoryBuilder(InventoryHolder holder, InventoryType type) {
        this(Bukkit.createInventory(holder, type));
    }

    public InventoryBuilder(int slots, Component component) {
        this(null, slots, component);
    }

    public InventoryBuilder(InventoryType type, Component component) {
        this(null, type, component);
    }

    public InventoryBuilder(int slots) {
        this(null, slots);
    }

    public InventoryBuilder(InventoryType type) {
        this(null, type);
    }

    public InventoryBuilder setItem(ItemStack itemStack, int slot, int... slots) {
        this.inventory.setItem(slot, itemStack);
        for (int slot1 : slots) {
            this.inventory.setItem(slot1, itemStack);
        }
        return this;
    }

    public InventoryBuilder setItem(ItemBuilder itemBuilder, int slot, int... slots) {
        return setItem(itemBuilder.build(), slot, slots);
    }

    public InventoryBuilder clearSlot(int slot, int... slots) {
        this.inventory.clear(slot);
        for (int slot1 : slots) {
            this.inventory.clear(slot1);
        }
        return this;
    }

    public InventoryBuilder addItem(ItemStack itemStack) {
        this.inventory.addItem(itemStack);
        return this;
    }

    public InventoryBuilder addItem(ItemBuilder itemBuilder) {
        return addItem(itemBuilder.build());
    }

    public InventoryBuilder setContents(ItemStack[] itemStacks) {
        this.inventory.setContents(itemStacks);
        return this;
    }

    public InventoryBuilder fillInventory(ItemStack itemStack) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            setItem(itemStack, slot);
        }
        return this;
    }

    public InventoryBuilder fillInventory(ItemBuilder itemBuilder) {
        return fillInventory(itemBuilder.build());
    }

    public ItemStack getItem(int slot) {
        return this.inventory.getItem(slot);
    }

    public Inventory build() {
        return this.inventory;
    }

}