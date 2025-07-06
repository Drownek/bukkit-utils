package me.drownek.util.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.gui.SimpleBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Builder;
import me.drownek.util.itemsadder.PluginUtil;
import me.drownek.util.message.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@Builder
public class GuiSettings extends OkaeriConfig {

    @Builder.Default
    public String title = "Gui";

    @Builder.Default
    public int rows = 3;

    @Builder.Default
    public boolean fillGui = false;

    @Builder.Default
    public ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

    @Builder.Default
    public String guiTexture = "";

    public SimpleBuilder toGuiBuilder() {
        return Gui.gui()
            .title(TextUtil.component(this.title))
            .rows(this.rows)
            .apply(gui -> {
                if (this.fillGui) {
                    gui.getFiller().fillBorder(new GuiItem(this.filler));
                }
                if (PluginUtil.isItemsAdderPresent()) {
                    gui.setOpenGuiAction(event -> {
                        Player player = (Player) event.getPlayer();
                        Bukkit.getScheduler().runTaskLater(
                            JavaPlugin.getProvidingPlugin(GuiSettings.class),
                            () -> GuiUtil.setGuiTexture(player, this.guiTexture),
                            1L
                        );
                    });
                }
            });
    }
}
