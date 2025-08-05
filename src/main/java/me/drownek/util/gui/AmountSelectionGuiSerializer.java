package me.drownek.util.gui;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import me.drownek.util.DataItemStack;

import java.util.function.Consumer;

public class AmountSelectionGuiSerializer implements ObjectSerializer<AmountSelectionGui> {

    @Override
    public boolean supports(@NonNull Class<? super AmountSelectionGui> type) {
        return AmountSelectionGui.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull AmountSelectionGui object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("title", object.getTitle());
        data.add("rows", object.getRows());
        data.add("initialValue", object.getInitialValue());
        data.add("minValue", object.getMinValue());
        data.add("maxValue", object.getMaxValue());
        data.add("increaseStep", object.getIncreaseStep());
        data.add("decreaseStep", object.getDecreaseStep());
        data.add("increaseStepShift", object.getIncreaseStepShift());
        data.add("decreaseStepShift", object.getDecreaseStepShift());
        data.add("closeOnConfirm", object.isCloseOnConfirm());
        data.add("showCancelItem", object.isShowCancelItem());

        data.add("displayItem", object.getDisplayItem(), GuiItemInfo.class);
        data.add("increaseItem", object.getIncreaseItem(), GuiItemInfo.class);
        data.add("decreaseItem", object.getDecreaseItem(), GuiItemInfo.class);
        data.add("cancelItem", object.getCancelItem(), GuiItemInfo.class);
        data.add("fillerItem", object.getFillerItem(), DataItemStack.class);
    }

    @Override
    public AmountSelectionGui deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        AmountSelectionGui.AmountSelectionGuiBuilder builder = AmountSelectionGui.builder();

        if (data.containsKey("title")) {
            builder.title(data.get("title", String.class));
        }
        if (data.containsKey("rows")) {
            builder.rows(data.get("rows", Integer.class));
        }
        if (data.containsKey("initialValue")) {
            builder.initialValue(data.get("initialValue", Integer.class));
        }
        if (data.containsKey("minValue")) {
            builder.minValue(data.get("minValue", Integer.class));
        }
        if (data.containsKey("maxValue")) {
            builder.maxValue(data.get("maxValue", Integer.class));
        }
        if (data.containsKey("increaseStep")) {
            builder.increaseStep(data.get("increaseStep", Integer.class));
        }
        if (data.containsKey("decreaseStep")) {
            builder.decreaseStep(data.get("decreaseStep", Integer.class));
        }
        if (data.containsKey("increaseStepShift")) {
            builder.increaseStepShift(data.get("increaseStepShift", Integer.class));
        }
        if (data.containsKey("decreaseStepShift")) {
            builder.decreaseStepShift(data.get("decreaseStepShift", Integer.class));
        }
        if (data.containsKey("closeOnConfirm")) {
            builder.closeOnConfirm(data.get("closeOnConfirm", Boolean.class));
        }
        if (data.containsKey("showCancelItem")) {
            builder.showCancelItem(data.get("showCancelItem", Boolean.class));
        }

        if (data.containsKey("displayItem")) {
            builder.displayItem(data.get("displayItem", GuiItemInfo.class));
        }
        if (data.containsKey("increaseItem")) {
            builder.increaseItem(data.get("increaseItem", GuiItemInfo.class));
        }
        if (data.containsKey("decreaseItem")) {
            builder.decreaseItem(data.get("decreaseItem", GuiItemInfo.class));
        }
        if (data.containsKey("cancelItem")) {
            builder.cancelItem(data.get("cancelItem", GuiItemInfo.class));
        }
        if (data.containsKey("fillerItem")) {
            builder.fillerItem(data.get("fillerItem", DataItemStack.class));
        }

        return builder.build();
    }
}