package me.drownek.util;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.gui.SimpleBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import lombok.Builder;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
public class ConfirmationGuiSettings extends OkaeriConfig {

    public static final @Exclude List<@NotNull Integer> YES_SLOTS = List.of(0, 1, 2, 9, 10, 11, 18, 19, 20);
    public static final @Exclude List<@NotNull Integer> NO_SLOTS = List.of(6, 7, 8, 15, 16, 17, 24, 25, 26);
    public static final @Exclude List<@NotNull Integer> INFO_SLOTS = List.of(13);

    @Builder.Default
    private GuiItemInfo yesItem = new GuiItemInfo(YES_SLOTS, XMaterial.PAPER, "&aTak").makeInvisible();

    @Builder.Default
    private GuiItemInfo infoItem = new GuiItemInfo(INFO_SLOTS, XMaterial.OAK_SIGN, "&cCzy na pewno?");

    @Builder.Default
    private GuiItemInfo noItem = new GuiItemInfo(NO_SLOTS, XMaterial.PAPER, "&cNie").makeInvisible();

    @Builder.Default
    private String title = " ";

    @Builder.Default
    private int rows = 3;

    @Builder.Default
    private boolean fillGui = false;

    @Builder.Default
    private ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

    @Builder.Default
    private String guiTexture = "fajneguis:template8";

    // Transient fields for actions
    private transient GuiAction<InventoryClickEvent> yesAction;
    private transient GuiAction<InventoryClickEvent> noAction;

    public ConfirmationGuiSettings yesItem(@NonNull GuiItemInfo yesItem) {
        return this.toBuilder()
            .yesItem(yesItem)
            .build();
    }

    public ConfirmationGuiSettings infoItem(@NonNull GuiItemInfo infoItem) {
        return this.toBuilder()
            .infoItem(infoItem)
            .build();
    }

    public ConfirmationGuiSettings noItem(@NonNull GuiItemInfo noItem) {
        return this.toBuilder()
            .noItem(noItem)
            .build();
    }

    public ConfirmationGuiSettings yesAction(@NonNull Runnable action) {
        return this.toBuilder()
            .yesAction(event -> action.run())
            .build();
    }

    public ConfirmationGuiSettings noAction(@NonNull Runnable action) {
        return this.toBuilder()
            .noAction(event -> action.run())
            .build();
    }

    public ConfirmationGuiSettings yesAction(@NonNull GuiAction<InventoryClickEvent> action) {
        return this.toBuilder()
            .yesAction(action)
            .build();
    }

    public ConfirmationGuiSettings noAction(@NonNull GuiAction<InventoryClickEvent> action) {
        return this.toBuilder()
            .noAction(action)
            .build();
    }

    public GuiItemInfo yesItem() {
        return this.yesItem;
    }

    public GuiItemInfo noItem() {
        return this.noItem;
    }

    public GuiItemInfo infoItem() {
        return this.infoItem;
    }

    public ConfirmationGuiSettings replacements(@NonNull Map<String, Object> replacements) {
        var settings = this.toBuilder().build();

        settings.yesItem = settings.yesItem.with(replacements);
        settings.infoItem = settings.infoItem.with(replacements);
        settings.noItem = settings.noItem.with(replacements);

        return settings;
    }

    private SimpleBuilder toGuiBuilder() {
        return Gui.gui()
            .title(TextUtil.component(this.title))
            .rows(this.rows)
            .apply(gui -> {
                if (this.fillGui) {
                    gui.getFiller().fillBorder(new GuiItem(this.filler));
                }
                gui.setOpenGuiAction(event -> {
                    Player player = (Player) event.getPlayer();
                    Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(ConfirmationGuiSettings.class), () -> {
                        GuiUtil.setGuiTexture(player, this.guiTexture);
                    }, 1L);
                });
            });
    }

    public Gui toGui() {
        var gui = this.toGuiBuilder().disableAllInteractions().create();

        yesItem.setGuiItem(gui, yesAction);
        infoItem.setGuiItem(gui);
        noItem.setGuiItem(gui, noAction);

        return gui;
    }

    public void open(@NonNull Player player) {
        this.toGui().open(player);
    }
}
