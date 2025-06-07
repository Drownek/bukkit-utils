package me.drownek.util;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class GuiUtil {

    public void setGuiTexture(Player player, String guiTextureId) {
        if (guiTextureId != null && !guiTextureId.isEmpty()) {
            FontImageWrapper texture = new FontImageWrapper(guiTextureId);
            if (texture != null && texture.exists()) {
                TexturedInventoryWrapper.setPlayerInventoryTexture(player, texture);
            }
        }
    }
}
