package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.NonNull;
import me.drownek.util.gui.GuiItemInfo;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    public DataItemStack withList(@NonNull String key, @NonNull List<@NonNull ?> values) {
        ItemStack stack = this.itemStack.clone();
        var itemMeta = stack.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        if (itemMeta.hasDisplayName() && values.size() == 1) {
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace(key, values.get(0).toString()));
        }

        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            if (lore != null) {
                List<String> newLore = new java.util.ArrayList<>();

                for (String line : lore) {
                    if (line.contains(key)) {
                        for (Object value : values) {
                            newLore.add(line.replace(key, value.toString()));
                        }
                    } else {
                        newLore.add(line);
                    }
                }

                itemMeta.setLore(newLore);
            }
        }

        stack.setItemMeta(itemMeta);
        return new DataItemStack(stack);
    }

    public DataItemStack with(@NonNull String key, @NonNull Object value) {
        return withList(key, List.of(value));
    }

    public DataItemStack with(Map<String, Object> replacements) {
        DataItemStack modifiedStack = this;
        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            modifiedStack = modifiedStack.with(entry.getKey(), entry.getValue());
        }
        return modifiedStack;
    }

    public DataItemStack name(String name) {
        this.itemStack = ItemStackBuilder.of(this.itemStack).name(name).asItemStack();
        return this;
    }

    public DataItemStack lore(String lore) {
        this.itemStack = ItemStackBuilder.of(this.itemStack).lore(lore).asItemStack();
        return this;
    }

    public DataItemStack lore(List<String> lore) {
        this.itemStack = ItemStackBuilder.of(this.itemStack).lore(lore).asItemStack();
        return this;
    }

    public DataItemStack apply(Consumer<DataItemStack> function) {
        function.accept(this);
        return this;
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
