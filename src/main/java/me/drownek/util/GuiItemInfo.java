package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.lone.itemsadder.api.CustomStack;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
@Getter
public class GuiItemInfo extends OkaeriConfig {

    public static @Exclude
    final String IAINTERNAL_ICON_BLANK = "_iainternal:icon_blank";

    // Helpers for List<Integer> positions
    public static List<Integer> fillCol(List<Integer> cols) {
        List<Integer> list = new ArrayList<>();
        cols.forEach(col -> IntStream.rangeClosed(1, 4).mapToObj(row -> getSlotFromRowCol(row, col)).forEach(list::add));
        return list;
    }

    public static int getSlotFromRowCol(int row, int col) {
        return col + (row - 1) * 9 - 1;
    }
    //

    private List<Integer> positions;
    private ItemStack itemStack;
    private boolean invisible;

    // Single position
    public GuiItemInfo(int position, ItemStack itemStack) {
        this.positions = List.of(position);
        this.itemStack = itemStack.clone();
    }

    public GuiItemInfo(int position, XMaterial material) {
        this.positions = List.of(position);
        this.itemStack = ItemStackBuilder.of(material).asItemStack();
    }

    public GuiItemInfo(int position, XMaterial material, String name) {
        this.positions = List.of(position);
        this.itemStack = ItemStackBuilder.of(material).name(name).asItemStack();
    }

    public GuiItemInfo(int position, XMaterial material, String name, List<String> lore) {
        this.positions = List.of(position);
        this.itemStack = ItemStackBuilder.of(material).name(name).lore(lore).asItemStack();
    }
    //

    // Multiple positions
    public GuiItemInfo(List<Integer> positions, ItemStack itemStack, boolean invisible) {
        this.positions = positions;
        this.itemStack = itemStack.clone();
        this.invisible = invisible;
    }

    public GuiItemInfo(List<Integer> positions, ItemStack itemStack) {
        this.positions = positions;
        this.itemStack = itemStack.clone();
    }

    public GuiItemInfo(List<Integer> positions, XMaterial material) {
        this.positions = positions;
        this.itemStack = ItemStackBuilder.of(material).asItemStack();
    }

    public GuiItemInfo(List<Integer> positions, XMaterial material, String name) {
        this.positions = positions;
        this.itemStack = ItemStackBuilder.of(material).name(name).asItemStack();
    }

    public GuiItemInfo(List<Integer> positions, XMaterial material, String name, List<String> lore) {
        this.positions = positions;
        this.itemStack = ItemStackBuilder.of(material).name(name).lore(lore).asItemStack();
    }
    //

    public void setGuiItem(BaseGui gui) {
        this.positions.forEach(position -> gui.setItem(position, this.asGuiItem()));
    }

    public void setGuiItem(BaseGui gui, GuiAction<InventoryClickEvent> event) {
        this.positions.forEach(position -> gui.setItem(position, this.asGuiItem(event)));
    }

    public GuiItemInfo setPosition(int position) {
        this.positions = List.of(position);
        return this;
    }

    public GuiItemInfo with(String key, Object value) {
        ItemStack stack = this.itemStack.clone();
        var itemMeta = stack.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(TextUtil.color(itemMeta.getDisplayName().replace(key, value.toString())));
        }

        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            if (lore != null) {
                lore.replaceAll(s -> s.replace(key, value.toString()));
                itemMeta.setLore(TextUtil.color(lore));
            }
        }

        stack.setItemMeta(itemMeta);
        return this.copy().setItemStack(stack);
    }

    public GuiItemInfo with(Map<String, Object> replacements) {
        GuiItemInfo modifiedStack = this;
        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            modifiedStack = modifiedStack.with(entry.getKey(), entry.getValue());
        }
        return modifiedStack;
    }

    public GuiItem asGuiItem() {
        return ItemStackBuilder.of(this.getItemStack()).asGuiItem();
    }

    public GuiItem asGuiItem(GuiAction<InventoryClickEvent> event) {
        return ItemStackBuilder.of(this.getItemStack()).asGuiItem(event);
    }

    public GuiItemInfo setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemStack getItemStack() {
        if (invisible) {
            return makeInvisible(itemStack);
        }
        return itemStack;
    }

    public GuiItemInfo makeInvisible() {
        invisible = true;
        return this;
    }

    private ItemStack makeInvisible(ItemStack itemStack) {
        CustomStack stack = CustomStack.getInstance(IAINTERNAL_ICON_BLANK);
        ItemStack stackItemStack = stack.getItemStack();
        int customModelData = stackItemStack.getItemMeta().getCustomModelData();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(customModelData);
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setType(stackItemStack.getType());
        return itemStack;
    }

    public GuiItemInfo copy() {
        return new GuiItemInfo(this.positions, this.itemStack.clone(), invisible);
    }

    // for compatibility
    public @Deprecated int getPosition() {
        if (this.positions.size() != 1) {
            throw new UnsupportedOperationException();
        }
        return this.positions.get(0);
    }
}
