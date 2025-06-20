package me.drownek.util;

import me.drownek.util.adventure.LegacyPostProcessor;
import me.drownek.util.adventure.LegacyPreProcessor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.IntStream;

public final class TextUtil {

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(TextUtil.class);
    public static BukkitAudiences adventure = BukkitAudiences.create(PLUGIN);
    public static MiniMessage miniMessage = MiniMessage.builder()
        .postProcessor(new LegacyPostProcessor())
        .preProcessor(new LegacyPreProcessor())
        .build();

    static {
        PLUGIN.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            void handle(PluginDisableEvent event) {
                if (event.getPlugin().equals(PLUGIN)) {
                    if (adventure != null) {
                        adventure.close();
                        adventure = null;
                    }
                }
            }
        }, PLUGIN);
    }

    private TextUtil() {
    }

    public static String prettyFormatLocation(double x, double y, double z) {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x, y, z);
    }

    public static String prettyFormatLocation(Location location) {
        if (location == null) {
            return "Brak.";
        }
        return prettyFormatLocation(location.getX(), location.getY(), location.getZ());
    }

    public static String prettyFormatItemStack(ItemStack item) {
        if (item == null) {
            return "Brak.";
        }

        ItemMeta meta = item.getItemMeta();
        String itemName;

        if (meta != null && meta.hasDisplayName()) {
            itemName = meta.getDisplayName();
        } else {
            itemName = item.getType().name();
        }

        return "%dx %s".formatted(item.getAmount(), itemName);
    }

    public static String color(final String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static Component component(final String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return resetItalic(miniMessage.deserialize(text));
    }

    public static Component component(final List<String> text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return Component.join(JoinConfiguration.newlines(), text.stream().map(TextUtil::component).toList());
    }

    public static Component component(final String... text) {
        return component(List.of(text));
    }

    public static List<String> color(final List<String> text) {
        return text.stream().map(TextUtil::color).toList();
    }

    public static void message(final CommandSender commandSender, final String text) {
        adventure.sender(commandSender).sendMessage(component(text));
    }

    public static void message(final CommandSender commandSender, final List<String> text) {
        text.forEach(value -> message(commandSender, value));
    }

    public static void sendEmptyMessage(final Player player, final int i) {
        IntStream.range(0, i).forEach(it -> message(player, ""));
    }

    public static void announce(final String text) {
        Bukkit.getOnlinePlayers().forEach(value -> message(value, text));
    }

    public static String progressBar(final long current, final long max, final int bars, final char symbol, final ChatColor completedColor, final ChatColor notCompletedColor) {
        final float percent = current / (float) max;
        final int progressBars = (int) (bars * percent);
        final int leftOver = bars - progressBars;
        final StringBuilder builder = new StringBuilder();
        if (current > max) {
            builder.append(completedColor);
            for (int i = 0; i < bars; ++i) {
                builder.append(symbol);
            }
            return builder.toString();
        }
        builder.append(completedColor.toString());
        for (int i = 0; i < progressBars; ++i) {
            builder.append(symbol);
        }
        builder.append(notCompletedColor.toString());
        for (int i = 0; i < leftOver; ++i) {
            builder.append(symbol);
        }
        return builder.toString();
    }

    public static String progressBar(final long current, final long max, final int bars, final char symbol, final String completedColor, final String notCompletedColor) {
        final float percent = current / (float) max;
        final int progressBars = (int) (bars * percent);
        final int leftOver = bars - progressBars;
        final StringBuilder builder = new StringBuilder();
        if (current > max) {
            builder.append(completedColor);
            for (int i = 0; i < bars; ++i) {
                builder.append(symbol);
            }
            return builder.toString();
        }
        builder.append(completedColor.toString());
        for (int i = 0; i < progressBars; ++i) {
            builder.append(symbol);
        }
        builder.append(notCompletedColor.toString());
        for (int i = 0; i < leftOver; ++i) {
            builder.append(symbol);
        }
        return builder.toString();
    }

    public static String clickableLocationRaw(Location location) {
        String command = String.format("/tp %s %s %s", location.getX(), location.getY(), location.getZ());
        String hoverText = "Kliknij, by przeteleportować";
        String locationText = TextUtil.prettyFormatLocation(location);

        return String.format(
            "<click:run_command:%s><hover:show_text:%s><green>%s</hover></click>",
            command, hoverText, locationText
        );
    }

    public static Component clickableLocation(Location location) {
        return miniMessage.deserialize(clickableLocationRaw(location));
    }

    public static Component resetItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
