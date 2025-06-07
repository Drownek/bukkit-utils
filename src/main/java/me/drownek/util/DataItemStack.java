package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class DataItemStack extends OkaeriConfig {

    private ItemStack itemStack;

    public DataItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public DataItemStack(Material material, String name, List<String> lore) {
        this(ItemStackBuilder.of(material).name(name).lore(lore).asItemStack());
    }

    public DataItemStack(Material material, String name) {
        this(ItemStackBuilder.of(material).name(name).asItemStack());
    }

    public DataItemStack(String name) {
        this(ItemStackBuilder.of(XMaterial.PAPER).name(name).asItemStack());
    }

    public DataItemStack with(String key, Object value) {
        ItemStack stack = this.itemStack.clone();
        var itemMeta = stack.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace(key, value.toString()));
        }

        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            if (lore != null) {
                lore.replaceAll(s -> s.replace(key, value.toString()));
                itemMeta.setLore(lore);
            }
        }

        stack.setItemMeta(itemMeta);
        return new DataItemStack(stack);
    }

    public DataItemStack with(Map<String, Object> replacements) {
        DataItemStack modifiedStack = this;
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

    public DataItemStack setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
}
