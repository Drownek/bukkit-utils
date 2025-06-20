package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

public final class GuiHelper {

    public static final ItemStack NEXT_ITEM;
    public static final ItemStack PREVIOUS_ITEM;
    public static final ItemStack BACK_ITEM;

    public static final String NEXT_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFhNGI1ZTg4MDVhYmZhY2VjMzIwNjU0ODllZjExZmNjZWUzZjUxYmVmZGRkMzE3MTQ3NzNmNzE0ZTdiMjczIn19fQ==";
    public static final String PREVIOUS_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZhOGQyOTIxOWUyYzljODQ5NTQ2OTNiODdmMzdhZWNlM2MyNzFkNTdmYjZhMmQ3MWZhZmUzOWYwYjgwNDAifX19";

    static {
        BACK_ITEM = ItemStackBuilder.of(Material.ARROW).name("&cBack").asItemStack();
        NEXT_ITEM = ItemBuilder.skull().texture(NEXT_TEXTURE).name(TextUtil.component("&aNext page")).build();
        PREVIOUS_ITEM = ItemBuilder.skull().texture(PREVIOUS_TEXTURE).name(TextUtil.component("&cPrevious page")).build();
    }

    private GuiHelper() {
    }

    public static PaginatedGui defaultPaginatedGui(String title, Consumer<PaginatedGui> consumer) {
        return defaultPaginatedGui(title, null, consumer);
    }

    public static PaginatedGui defaultPaginatedGui(String title, Runnable backAction, Consumer<PaginatedGui> consumer) {
        PaginatedGui gui = Gui.paginated().rows(6).title(Component.empty()).pageSize(28).disableAllInteractions().create();

        gui.getFiller().fillBorder(new GuiItem(Objects.requireNonNull(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())));

        gui.setItem(6, 4, ItemStackBuilder.of(GuiHelper.PREVIOUS_ITEM).asGuiItem((inventoryClickEvent) -> {
            gui.previous();
            updatePageTitle(title, gui);
        }));
        gui.setItem(6, 5, ItemStackBuilder.of(GuiHelper.BACK_ITEM).asGuiItem((inventoryClickEvent) -> {
            if (backAction == null) {
                inventoryClickEvent.getWhoClicked().closeInventory();
                return;
            }
            backAction.run();
        }));
        gui.setItem(6, 6, ItemStackBuilder.of(GuiHelper.NEXT_ITEM).asGuiItem((inventoryClickEvent) -> {
            gui.next();
            updatePageTitle(title, gui);
        }));

        consumer.accept(gui);
        updatePageTitle(title, gui);

        return gui;
    }

    private static void updatePageTitle(String title, PaginatedGui gui) {
        gui.updateTitle(TextUtil.color(String.format(title + " (%s/%s)", gui.getCurrentPageNum(), gui.getPagesNum())));
    }
}
