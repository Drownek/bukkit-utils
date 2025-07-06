package me.drownek.util;

import me.drownek.util.message.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class InventoryUtil {
    private InventoryUtil() {
    }

    public static boolean hasItems(final Player player, final List<ItemStack> items) {
        for (final ItemStack item : items) {
            if (!hasItem(player, item)) {
                return false;
            }
        }
        return true;
    }

    public static int countItemsIgnoreItemMeta(final Player player, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return 0;
        }
        final PlayerInventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < inventory.getSize(); ++i) {
            final ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                if (isSimilarExceptItemMeta(item, itemStack)) {
                    count += itemStack.getAmount();
                }
            }
        }
        return count;
    }

    private static boolean isSimilarExceptItemMeta(final ItemStack stack1, final ItemStack stack2) {
        return stack1 != null && stack2 != null && stack1.getType() == stack2.getType() && stack1.getDurability() == stack2.getDurability();
    }

    public static int countAmountForDeposit(final Player player, final ItemStack item) {
        int amount = 0;
        final PlayerInventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize(); ++i) {
            final ItemStack is = playerInventory.getItem(i);
            if (is != null) {
                if (is.getType() == item.getType() && is.getDurability() == item.getDurability() && is.getData().equals(item.getData())) {
                    amount += is.getAmount();
                }
            }
        }
        return amount;
    }

    public static boolean hasItem(final Player player, final ItemStack item, final int amount) {
        return item != null && item.getType() != Material.AIR && player.getInventory().containsAtLeast(item, amount);
    }

    public static boolean hasItem(final Player player, final Material material, final int amount) {
        return hasItem(player, new ItemStack(material, amount));
    }

    public static boolean hasItem(final Player player, final ItemStack item) {
        return item == null || item.getType() == Material.AIR || hasItem(player, item, item.getAmount());
    }

    public static void addItem(final Player player, final ItemStack item) {
        if (item == null) {
            return;
        }
        final Map<Integer, ItemStack> leftOver = player.getInventory().addItem(item.clone());
        for (final ItemStack leftoverItem : leftOver.values()) {
            player.getWorld().dropItem(player.getLocation(), leftoverItem.clone());
        }
    }

    public static void addItems(final Player player, final List<ItemStack> items, final Block block) {
        final Map<Integer, ItemStack> leftOver = player.getInventory().addItem(items.toArray(new ItemStack[0]));
        for (final Map.Entry<Integer, ItemStack> en : leftOver.entrySet()) {
            block.getWorld().dropItemNaturally(block.getLocation(), en.getValue());
        }
    }

    public static void addItem(final Player player, final List<ItemStack> items) {
        final Map<Integer, ItemStack> leftOver = player.getInventory().addItem(items.stream().filter(Objects::nonNull).map(ItemStack::clone).toArray(ItemStack[]::new));
        for (final ItemStack leftoverItem : leftOver.values()) {
            player.getWorld().dropItem(player.getLocation(), leftoverItem.clone());
        }
    }

    public static void removeItemIgnoreItemMeta(final Player player, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        int amountLeft = item.getAmount();
        final ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; ++i) {
            final ItemStack itemStack = contents[i];
            if (itemStack != null && itemStack.getType() == item.getType() && itemStack.getDurability() == item.getDurability()) {
                if (amountLeft >= itemStack.getAmount()) {
                    amountLeft -= itemStack.getAmount();
                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amountLeft);
                }
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    public static void removeItemByDisplayName(final Player player, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        int amountLeft = item.getAmount();
        final ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; ++i) {
            final ItemStack itemStack = contents[i];
            if (itemStack != null && itemStack.getType() == item.getType() && itemStack.getDurability() == item.getDurability() && TextUtil.color(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase(TextUtil.color(item.getItemMeta().getDisplayName()))) {
                if (amountLeft >= itemStack.getAmount()) {
                    amountLeft -= itemStack.getAmount();
                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amountLeft);
                }
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    public static void removeItem(final Player player, final List<ItemStack> items) {
        if (player == null || items == null || items.isEmpty()) {
            return;
        }
        for (final ItemStack is : items) {
            player.getInventory().removeItem(is);
        }
    }

    public static void removeItem(final Player player, final Material material, final int amount, final short durability) {
        removeItem(player, new ItemStack(material, amount, durability));
    }

    public static void removeItem(final Player player, final Material material, final int amount) {
        removeItem(player, new ItemStack(material, amount));
    }

    public static void removeItem(final Player player, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        player.getInventory().removeItem(item);
    }

    public static void removeItems(final Player player, final List<ItemStack> items) {
        player.getInventory().removeItem(items.stream().filter(Objects::nonNull).toArray(ItemStack[]::new));
    }

    public static void removeItem(final Inventory inventory, final int slot, final int amount) {
        if (inventory.getItem(slot) == null) {
            return;
        }
        final ItemStack item = inventory.getItem(slot).clone();
        if (item == null || item.getType().equals(Material.AIR)) {
            return;
        }
        if (item.getAmount() <= amount) {
            inventory.setItem(slot, new ItemStack(Material.AIR));
            return;
        }
        item.setAmount(item.getAmount() - amount);
        inventory.setItem(slot, item);
    }

    public static void removeItems(final Inventory inv, final ItemStack item, final int amount) {
        int toRemove = amount;
        final Map<Integer, ItemStack> slots = new HashMap<>();
        for (int slot = 0; slot < inv.getSize(); ++slot) {
            final ItemStack slotItem = inv.getItem(slot);
            if (slotItem != null && slotItem.getType() != Material.AIR && slotItem.getType() == item.getType() && slotItem.getDurability() == item.getDurability()) {
                slots.put(slot, slotItem);
            }
        }
        for (final Map.Entry<Integer, ItemStack> entrySlots : slots.entrySet()) {
            if (entrySlots.getValue().getAmount() > toRemove) {
                final ItemStack invItem = inv.getItem(entrySlots.getKey());
                invItem.setAmount(invItem.getAmount() - toRemove);
                break;
            }
            inv.setItem(entrySlots.getKey(), new ItemStack(Material.AIR));
            toRemove -= entrySlots.getValue().getAmount();
        }
    }
}
