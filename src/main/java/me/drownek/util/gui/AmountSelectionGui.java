package me.drownek.util.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;
import lombok.experimental.WithBy;
import me.drownek.util.DataItemStack;
import me.drownek.util.localization.LocalizationManager;
import me.drownek.util.localization.MessageKey;
import me.drownek.util.message.Formatter;
import me.drownek.util.message.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

@Builder
public final class AmountSelectionGui {

    private static final int DEFAULT_ROWS = 3;
    private static final int DEFAULT_MIN_VALUE = 1;
    private static final int DEFAULT_INITIAL_VALUE = 1;
    private static final int DEFAULT_STEP = 1;
    private static final int DEFAULT_SHIFT_STEP = 10;
    private static final int MAX_ITEM_STACK_SIZE = 64;
    private static final int MIN_GUI_ROWS = 1;
    private static final int MAX_GUI_ROWS = 6;

    private final Consumer<Integer> onConfirm;

    @NonNull
    private final GuiItemInfo displayItem;

    @Builder.Default
    private final String title = "";

    @Builder.Default
    private final int rows = DEFAULT_ROWS;

    @Builder.Default
    private final Runnable onCancel = () -> {};

    @Builder.Default
    private final int initialValue = DEFAULT_INITIAL_VALUE;

    @Builder.Default
    private final int minValue = DEFAULT_MIN_VALUE;

    @Builder.Default
    private final int maxValue = Integer.MAX_VALUE;

    @Builder.Default
    private final int increaseStep = DEFAULT_STEP;

    @Builder.Default
    private final int decreaseStep = DEFAULT_STEP;

    @Builder.Default
    private final int increaseStepShift = DEFAULT_SHIFT_STEP;

    @Builder.Default
    private final int decreaseStepShift = DEFAULT_SHIFT_STEP;

    @Builder.Default
    private final GuiItemInfo increaseItem = new GuiItemInfo(15, XMaterial.GREEN_STAINED_GLASS_PANE, "&a&l+");

    @Builder.Default
    private final GuiItemInfo decreaseItem = new GuiItemInfo(11, XMaterial.RED_STAINED_GLASS_PANE, "&c&l-");

    @Builder.Default
    private final GuiItemInfo cancelItem = new GuiItemInfo(22, XMaterial.BARRIER, LocalizationManager.getMessage(MessageKey.AMOUNT_GUI_CANCEL));

    @Builder.Default
    private final DataItemStack fillerItem = new DataItemStack(XMaterial.BLACK_STAINED_GLASS_PANE, " ");

    @Builder.Default
    private final Function<Integer, Map<String, Object>> additionalPlaceholders = integer -> Map.of();

    @Builder.Default
    private final boolean closeOnConfirm = true;

    @Builder.Default
    private final boolean showCancelItem = true;

    public void open(@NonNull Player player) {
        validateConfiguration();

        final var gui = createGui();
        final var state = new GuiState(clampValue(initialValue));

        setupGui(gui, state, player);
        gui.open(player);
    }

    private void validateConfiguration() {
        require(minValue <= maxValue, "minValue cannot be greater than maxValue");
        require(rows >= MIN_GUI_ROWS && rows <= MAX_GUI_ROWS, "rows must be between %d and %d".formatted(MIN_GUI_ROWS, MAX_GUI_ROWS));
        require(increaseStep > 0, "increaseStep must be positive");
        require(decreaseStep > 0, "decreaseStep must be positive");
        require(increaseStepShift > 0, "increaseStepShift must be positive");
        require(decreaseStepShift > 0, "decreaseStepShift must be positive");
        require(onConfirm != null, "onConfirm cannot be null");
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    private Gui createGui() {
        return Gui.gui()
            .title(TextUtil.component(title))
            .rows(rows)
            .disableAllInteractions()
            .create();
    }

    private void setupGui(Gui gui, GuiState state, Player player) {
        final var confirmAction = createConfirmationAction(state, player);

        if (shouldAddFillerItems()) {
            gui.getFiller().fill(fillerItem.asGuiItem());
        }

        gui.setCloseGuiAction(event -> {
            if (!state.isConfirmed()) {
                onCancel.run();
            }
        });

        setupControls(gui, state, confirmAction, player);
        updateDisplay(gui, state, confirmAction);
    }

    private boolean shouldAddFillerItems() {
        return fillerItem != null &&
            fillerItem.getItemStack() != null &&
            fillerItem.getItemStack().getType() != XMaterial.AIR.get();
    }

    private GuiAction<InventoryClickEvent> createConfirmationAction(GuiState state, Player player) {
        return event -> {
            state.confirm();
            if (closeOnConfirm) {
                player.closeInventory();
            }
            onConfirm.accept(state.getAmount());
        };
    }

    private void setupControls(Gui gui, GuiState state, GuiAction<InventoryClickEvent> confirmAction, Player player) {
        setupAmountControl(gui, state, confirmAction, increaseItem, this::createIncreaseOperator);
        setupAmountControl(gui, state, confirmAction, decreaseItem, this::createDecreaseOperator);

        if (showCancelItem) {
            cancelItem.setGuiItem(gui, event -> player.closeInventory());
        }
    }

    private void setupAmountControl(
        Gui gui,
        GuiState state,
        GuiAction<InventoryClickEvent> confirmAction,
        GuiItemInfo controlItem,
        Function<Boolean, IntUnaryOperator> operatorFactory
    ) {
        controlItem.setGuiItem(gui, event -> {
            final var operator = operatorFactory.apply(event.isShiftClick());
            final var newAmount = clampValue(operator.applyAsInt(state.getAmount()));
            state.setAmount(newAmount);
            updateDisplay(gui, state, confirmAction);
        });
    }

    private IntUnaryOperator createIncreaseOperator(boolean isShift) {
        final int step = isShift ? increaseStepShift : increaseStep;
        return amount -> amount + step;
    }

    private IntUnaryOperator createDecreaseOperator(boolean isShift) {
        final int step = isShift ? decreaseStepShift : decreaseStep;
        return amount -> amount - step;
    }

    private void updateDisplay(Gui gui, GuiState state, GuiAction<InventoryClickEvent> confirmAction) {
        var formatter = new Formatter().register("{VALUE}", state.getAmount());
        Map<String, Object> additionalPlaceholders = this.additionalPlaceholders.apply(state.getAmount());
        formatter.register(additionalPlaceholders);

        final var updatedInfo = displayItem.with(formatter);
        final var displayStack = createDisplayStack(updatedInfo, state.getAmount());

        gui.updateItem(updatedInfo.firstPosition(), new GuiItem(displayStack, confirmAction));
    }

    private ItemStack createDisplayStack(GuiItemInfo info, int amount) {
        final var stack = Objects.requireNonNull(info.getItemStack()).clone();
        stack.setAmount(clampToStackSize(amount));
        return stack;
    }

    private int clampValue(int value) {
        return Math.max(minValue, Math.min(maxValue, value));
    }

    private int clampToStackSize(int amount) {
        return Math.max(1, Math.min(MAX_ITEM_STACK_SIZE, amount));
    }

    private static final class GuiState {
        private final AtomicInteger amount;
        private final AtomicBoolean confirmed = new AtomicBoolean(false);

        GuiState(int initialAmount) {
            this.amount = new AtomicInteger(initialAmount);
        }

        int getAmount() {
            return amount.get();
        }

        void setAmount(int value) {
            amount.set(value);
        }

        void confirm() {
            confirmed.set(true);
        }

        boolean isConfirmed() {
            return confirmed.get();
        }
    }
}