package me.drownek.util.gui;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class GuiItemInfoSerializer implements ObjectSerializer<GuiItemInfo> {
    @Override
    public boolean supports(@NonNull Class<? super GuiItemInfo> type) {
        return GuiItemInfo.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull GuiItemInfo object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("positions", object.getPositions());
        data.add("itemStack", object.getItemStack());
        if (object.isInvisible()) {
            data.add("invisible", true);
        }
    }

    @Override
    public GuiItemInfo deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        List<Integer> positions = data.getAsList("positions", Integer.class);
        ItemStack itemStack = data.get("itemStack", ItemStack.class);
        boolean invisible = Optional.ofNullable(data.get("invisible", Boolean.class)).orElse(false);
        
        return new GuiItemInfo(positions, itemStack, invisible);
    }
}