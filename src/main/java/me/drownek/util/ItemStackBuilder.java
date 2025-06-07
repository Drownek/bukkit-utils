package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;

public final class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemStackBuilder(final Material material, final int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    private ItemStackBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public static ItemStackBuilder furniture(final String namespacedId) {
        CustomStack instance = CustomFurniture.getInstance(namespacedId);
        if (instance == null) {
            throw new IllegalArgumentException("Cannot find furniture: " + namespacedId);
        }
        return new ItemStackBuilder(instance.getItemStack());
    }

    public static ItemStackBuilder of(final XMaterial material, final int amount) {
        var stack = Objects.requireNonNull(material.parseItem());
        stack.setAmount(amount);
        return new ItemStackBuilder(stack);
    }

    public static ItemStackBuilder of(final XMaterial material) {
        return new ItemStackBuilder(Objects.requireNonNull(material.parseItem()));
    }

    public static ItemStackBuilder of(final Material material) {
        return new ItemStackBuilder(material, 1);
    }

    public static ItemStackBuilder of(final Material material, final int amount) {
        return new ItemStackBuilder(material, amount);
    }

    public static ItemStackBuilder of(final ItemStack item) {
        return new ItemStackBuilder(item);
    }

    public static ItemStack formatNameAndLore(final ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        return of(item).name(meta.getDisplayName()).lore(meta.getLore()).asItemStack();
    }

    public void refreshMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public ItemStackBuilder name(final String name) {
        this.itemMeta.setDisplayName(TextUtil.color(name));
        this.refreshMeta();
        return this;
    }

    public ItemStackBuilder lore(final List<String> lore) {
        this.itemMeta.setLore(TextUtil.color(lore));
        this.refreshMeta();
        return this;
    }

    public ItemStackBuilder lore(final String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public ItemStackBuilder replaceLore(Map<String, String> replacements) {
        List<String> lore = this.itemMeta.getLore();
        if (lore == null) {
            return this;
        }

        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
            newLore.add(line);
        }

        this.itemMeta.setLore(newLore);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemStackBuilder appendLore(final List<String> lore) {
        ItemMeta itemMeta = this.itemMeta;
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(TextUtil.color(lore));
        } else {
            final List<String> newLore = itemMeta.getLore();
            newLore.addAll(TextUtil.color(lore));
            itemMeta.setLore(newLore);
        }
        this.refreshMeta();
        return this;
    }

    public ItemStackBuilder appendLore(final String lore) {
        return this.appendLore(Collections.singletonList(lore));
    }

    public ItemStackBuilder appendLore(final String... lore) {
        return this.appendLore(Arrays.asList(lore));
    }

    public ItemStackBuilder enchantment(final Enchantment enchant, final int level) {
        this.itemMeta.addEnchant(enchant, level, true);
        this.refreshMeta();
        return this;
    }

    public ItemStackBuilder flag(final ItemFlag flag) {
        this.itemMeta.addItemFlags(flag);
        this.refreshMeta();
        return this;
    }

    public ItemStackBuilder amount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder glow() {
        return this.glow(true);
    }

    public ItemStackBuilder glow(final boolean glow) {
        if (glow) {
            this.itemMeta.addEnchant(Enchantment.LURE, 1, false);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            for (final Enchantment enchantment : this.itemMeta.getEnchants().keySet()) {
                this.itemMeta.removeEnchant(enchantment);
            }
        }
        this.refreshMeta();
        return this;
    }

    public ItemMeta getMeta() {
        return this.itemMeta;
    }

    public ItemStack asItemStack() {
        return this.itemStack;
    }

    public GuiItem asGuiItem() {
        return new GuiItem(this.itemStack);
    }

    public GuiItem asGuiItem(final GuiAction<InventoryClickEvent> event) {
        return new GuiItem(this.itemStack, event);
    }

    public ItemStackBuilder applyMeta(final Consumer<ItemMeta> consumer) {
        consumer.accept(this.itemMeta);
        this.refreshMeta();
        return this;
    }
}
