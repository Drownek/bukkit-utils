package me.drownek.util.itemsadder;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class PluginUtil {

    public boolean isItemsAdderPresent() {
        return Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
    }
}
