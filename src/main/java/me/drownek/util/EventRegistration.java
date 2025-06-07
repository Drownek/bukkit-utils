package me.drownek.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

@Getter
public class EventRegistration {
    private final Listener listener;

    private EventRegistration(Listener listener) {
        this.listener = listener;
    }

    public void unregister() {
        HandlerList.unregisterAll(listener);
    }

    public static <E extends Event> EventRegistration register(
        Class<E> eventClass,
        Consumer<E> consumer,
        EventPriority priority
    ) {
        return register(new Listener() {}, eventClass, consumer, priority);
    }

    public static <E extends Event> EventRegistration register(
        Listener listener,
        Class<E> eventClass,
        Consumer<E> consumer,
        EventPriority priority
    ) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(EventRegistration.class);
        Bukkit.getPluginManager().registerEvent(
            eventClass,
            listener,
            priority,
            (l, event) -> {
                if (eventClass.isInstance(event)) {
                    consumer.accept(eventClass.cast(event));
                }
            },
            plugin
        );

        return new EventRegistration(listener);
    }
}
