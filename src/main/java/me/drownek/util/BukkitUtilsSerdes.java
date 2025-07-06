package me.drownek.util;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;
import me.drownek.util.gui.GuiItemInfoSerializer;
import me.drownek.util.message.AudibleMessageSerializer;

public class BukkitUtilsSerdes implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new AudibleMessageSerializer());
        registry.register(new GuiItemInfoSerializer());
    }
}
