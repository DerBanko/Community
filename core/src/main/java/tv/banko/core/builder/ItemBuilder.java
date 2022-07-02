package tv.banko.core.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ItemBuilder implements Cloneable {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = Objects.requireNonNullElse(itemStack.getItemMeta(),
                Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, String displayName) {
        this(material, 1);
        setDisplayName(displayName);
    }

    public ItemBuilder(Material material, Component displayName) {
        this(material, 1);
        setDisplayName(displayName);
    }

    public ItemBuilder setDisplayName(String displayName) {
        return setDisplayName(deserializeLegacy(displayName));
    }

    public ItemBuilder setDisplayName(Component displayName) {
        this.itemMeta.displayName(displayName);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addLore(Component lore) {
        List<Component> loreList = Objects.requireNonNullElse(this.itemMeta.lore(), new ArrayList<>());
        loreList.add(lore);
        this.itemMeta.lore(loreList);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        return addLore(deserializeLegacy(lore));
    }

    public ItemBuilder addLores(Component... lores) {
        List<Component> loreList = Objects.requireNonNullElse(this.itemMeta.lore(), new ArrayList<>());
        loreList.addAll(Arrays.stream(lores).toList());
        this.itemMeta.lore(loreList);
        return this;
    }

    public ItemBuilder addLores(String... lores) {
        List<Component> loreList = new ArrayList<>();

        for (String lore : lores) {
            loreList.add(deserializeLegacy(lore));
        }

        return addLores(loreList.toArray(new Component[0]));
    }

    public ItemBuilder removeLore(Component lore) {
        List<Component> loreList = Objects.requireNonNullElse(this.itemMeta.lore(), new ArrayList<>());
        loreList.removeIf(component -> component.equals(lore));
        this.itemMeta.lore(loreList);
        return this;
    }

    public ItemBuilder removeLore(String lore) {
        return removeLore(deserializeLegacy(lore));
    }

    public ItemBuilder clearLores() {
        this.itemMeta.lore(new ArrayList<>());
        return this;
    }

    /**
     * Set an item unbreakable
     * @param unbreakable value if it should be unbreakable or not
     * @return the ItemBuilder
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment) {
        return addUnsafeEnchantment(enchantment, 1);
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        return addEnchantment(enchantment, 1);
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.itemStack.getEnchantments().forEach((enchantment, integer) -> this.itemStack.removeEnchantment(enchantment));
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemStack.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemStack.removeItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer player) {

        if(!(itemMeta instanceof SkullMeta skullMeta)) {
            throw new ClassCastException("This item is no player head");
        }

        skullMeta.setOwningPlayer(player);
        this.itemMeta = skullMeta;
        return this;
    }

    public ItemBuilder setBase64(String base64) {

        if(!(itemMeta instanceof SkullMeta skullMeta)) {
            throw new ClassCastException("This item is no player head");
        }

        mutateItemMeta(skullMeta, base64);
        itemStack.setItemMeta(skullMeta);
        this.itemMeta = skullMeta;
        return this;
    }

    public <T> ItemBuilder addData(NamespacedKey key, PersistentDataType<T, T> type, T value) {
        this.itemMeta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder setLeatherArmor(Color color) {

        if(!(itemMeta instanceof LeatherArmorMeta leatherArmorMeta)) {
            throw new ClassCastException("This item is no leather armor");
        }

        leatherArmorMeta.setColor(color);
        return this;
    }

    public ItemBuilder setBasePotionEffect(PotionType type) {

        if(!(itemMeta instanceof PotionMeta potionMeta)) {
            throw new ClassCastException("This item is no potion");
        }

        potionMeta.setBasePotionData(new PotionData(type));
        return this;
    }

    public ItemBuilder addCustomEffect(PotionEffect effect, boolean overwrite) {

        if(!(itemMeta instanceof PotionMeta potionMeta)) {
            throw new ClassCastException("This item is no potion");
        }

        potionMeta.addCustomEffect(effect, overwrite);
        return this;
    }

    public ItemBuilder addCustomEffect(PotionEffect effect) {
        return addCustomEffect(effect, false);
    }

    public ItemBuilder setPotionColor(Color color) {

        if(!(itemMeta instanceof PotionMeta potionMeta)) {
            throw new ClassCastException("This item is no potion");
        }

        potionMeta.setColor(color);
        return this;
    }

    public ItemStack build() {
        ItemStack clone = itemStack.clone();
        clone.setItemMeta(itemMeta);
        return clone;
    }

    public ItemBuilder clone() throws CloneNotSupportedException {
        return (ItemBuilder) super.clone();
    }

    private Component deserializeLegacy(String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }

    private void mutateItemMeta(SkullMeta meta, String b64) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, makeProfile(b64));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    private GameProfile makeProfile(String b64) {
        // random id based on the b64 string
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "Player");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }
}