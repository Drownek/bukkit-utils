package me.drownek.util.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.gui.PaginatedBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Builder;
import me.drownek.util.itemsadder.PluginUtil;
import me.drownek.util.message.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

@Builder
public class PaginatedGuiSettings extends OkaeriConfig {

    @Builder.Default
    public ItemStack closeGuiItem = GuiHelper.BACK_ITEM;

    @Builder.Default
    public ItemStack nextPageItem = GuiHelper.NEXT_ITEM;

    @Builder.Default
    public ItemStack previousPageItem = GuiHelper.PREVIOUS_ITEM;

    @Builder.Default
    public int closeGuiItemPosition = 40;

    @Builder.Default
    public int nextPageItemPosition = 41;

    @Builder.Default
    public int previousPageItemPosition = 39;

    @Builder.Default
    public String title = "Gui";

    @Builder.Default
    public int rows = 5;

    @Builder.Default
    public boolean fillGui = true;

    @Builder.Default
    public ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

    @Builder.Default
    public String guiTexture = "";

    private transient GuiAction<InventoryClickEvent> closeAction;

    public PaginatedGuiSettings closeAction(Consumer<HumanEntity> closeAction) {
        this.closeAction = closeAction == null ? null : event -> closeAction.accept(event.getWhoClicked());
        return this;
    }

    public PaginatedBuilder toPaginatedGuiBuilder() {
        return Gui.paginated()
            .title(TextUtil.component(this.title))
            .rows(this.rows)
            .apply(gui -> {
                if (this.fillGui) {
                    gui.getFiller().fillBorder(new GuiItem(this.filler));
                }
                gui.setItem(closeGuiItemPosition, new GuiItem(closeGuiItem, event -> {
                    if (closeAction == null) {
                        event.getWhoClicked().closeInventory();
                        return;
                    }
                    closeAction.execute(event);
                }));
                gui.setItem(nextPageItemPosition, new GuiItem(nextPageItem, event -> {
                    gui.next();
                    updatePageTitle(this.title, gui);
                }));
                gui.setItem(previousPageItemPosition, new GuiItem(previousPageItem, event -> {
                    gui.previous();
                    updatePageTitle(this.title, gui);
                }));

                gui.setOpenGuiAction(event -> {
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(PaginatedGuiSettings.class), () -> {
                        updatePageTitle(this.title, gui);
                    });

                    Player player = (Player) event.getPlayer();
                    if (PluginUtil.isItemsAdderPresent()) {
                        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(PaginatedGuiSettings.class), () -> {
                            GuiUtil.setGuiTexture(player, this.guiTexture);
                        }, 1L);
                    }
                });
            });
    }

    private static void updatePageTitle(String title, PaginatedGui gui) {
        gui.updateTitle(TextUtil.color(String.format(title + " (%s/%s)", gui.getCurrentPageNum(), gui.getPagesNum())));
    }
}
