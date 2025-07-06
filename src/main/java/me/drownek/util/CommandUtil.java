package me.drownek.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.drownek.util.localization.LocalizationManager;
import me.drownek.util.localization.MessageKey;
import me.drownek.util.message.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class CommandUtil {
    
    /*
     * Remove actions
     */
    public <T> String removeIfPresent(@NonNull Collection<T> list, T object, @Nullable Runnable successAction) {
        if (object == null) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_INVALID_ELEMENT);
        }
        if (!list.contains(object)) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_ELEMENT_MISSING);
        }
        list.remove(object);
        if (successAction != null) {
            successAction.run();
        }
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }

    public <T> String removeIfPresent(@NonNull Collection<T> list, T object) {
        return removeIfPresent(list, object, null);
    }

    public <T> String removeIfMatch(@NonNull Collection<T> list, @NonNull Predicate<T> predicate, @Nullable Runnable successAction) {
        Optional<T> any = list.stream().filter(predicate).findAny();
        if (any.isEmpty()) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_ELEMENT_MISSING);
        }
        list.remove(any.get());
        if (successAction != null) {
            successAction.run();
        }
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }

    public <T> String removeIfMatch(@NonNull Collection<T> list, @NonNull Predicate<T> predicate) {
        return removeIfMatch(list, predicate, null);
    }

    public <T> String removeAtIndexAndConsume(@NonNull List<T> list, int index, Consumer<T> consumer) {
        if (index <= 0 || index > list.size()) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_ELEMENT_MISSING);
        }
        T element = list.remove(index - 1);
        consumer.accept(element);
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }

    public <T> String removeAtIndex(@NonNull List<T> list, int index, Runnable successAction) {
        if (index <= 0 || index > list.size()) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_ELEMENT_MISSING);
        }
        list.remove(index - 1);
        successAction.run();
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }


    /*
     *
     */

    public <T> String addIfNoneMatch(@NonNull Collection<T> list, T object, @NonNull Predicate<T> predicate, @Nullable Runnable runnable) {
        if (predicate.test(object)) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_ELEMENT_ALREADY_EXIST);
        }
        list.add(object);
        if (runnable != null) {
            runnable.run();
        }
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }

    public <T> String addIfNoneMatch(@NonNull Collection<T> list, T object, @NonNull Predicate<T> predicate) {
        return addIfNoneMatch(list, object, predicate, null);
    }

    public <T> String addIfAbsent(@NonNull Collection<T> list, T object, Runnable runnable) {
        return addIfNoneMatch(list, object, list::contains, runnable);
    }

    public <T> String addIfAbsent(@NonNull Collection<T> list, T object) {
        return addIfAbsent(list, object, null);
    }

    /*
     * List
     */

    public <T> void sendOrderList(@NonNull Player player,
                             @NonNull Collection<T> list,
                             @NonNull Function<T, Component> mapper) {
        sendOrderList(player, list, mapper, t -> true);
    }

    public <T> void sendOrderList(@NonNull Player player,
                                  @NonNull Collection<T> list,
                                  @NonNull Function<T, Component> mapper,
                                  @NonNull Predicate<Component> filter) {
        if (list.isEmpty()) {
            TextUtil.message(player, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_EMPTY));
            return;
        }

        TextUtil.message(player, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS));

        AtomicInteger i = new AtomicInteger(1);
        List<Component> parcelComponents = list.stream()
            .map(t -> {
                Component apply = mapper.apply(t);
                if (filter.test(apply)) {
                    return TextUtil.component(i.getAndIncrement() + ". ").append(apply);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

        Component fullMessage = Component.join(JoinConfiguration.newlines(), parcelComponents);

        TextUtil.adventure.player(player).sendMessage(fullMessage);
    }

    public <T> void sendList(@NonNull Player player,
                             @Nullable Component prefix,
                             @NonNull Collection<T> list,
                             @NonNull Function<T, Component> mapper) {
        if (list.isEmpty()) {
            TextUtil.message(player, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_EMPTY));
            return;
        }

        TextUtil.message(player, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS));

        List<Component> parcelComponents = list.stream()
            .map(t -> {
                if (prefix != null) {
                    return prefix.append(mapper.apply(t));
                }
                return mapper.apply(t);
            })
            .toList();

        Component fullMessage = Component.join(JoinConfiguration.newlines(), parcelComponents);

        TextUtil.adventure.player(player).sendMessage(fullMessage);
    }

    public <T> void sendList(@NonNull Player player, @NonNull Collection<T> list, @NonNull Function<T, Component> mapper) {
        sendList(player, null, list, mapper);
    }

    public <T> List<String> listElements(@NonNull Collection<T> list) {
        return listElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), Object::toString);
    }

    public <T> List<String> listElements(@NonNull Collection<T> list, Function<T, String> mapper) {
        return listElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), mapper);
    }

    public <T> List<String> listElements(@NonNull Collection<T> list, String header, Function<T, String> mapper) {
        return Stream.of(header)
            .flatMap(string -> Stream.concat(Stream.of(string), list.stream().map(mapper)))
            .collect(Collectors.toList());
    }

    public <T> List<String> listOrderElements(@NonNull Collection<T> list, Function<T, String> mapper) {
        return listOrderElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), mapper);
    }

    public <T> List<String> listOrderElements(@NonNull Collection<T> list) {
        return listOrderElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), Object::toString);
    }

    public <T> List<String> listOrderElements(@NonNull Collection<T> collection, String header, Function<T, String> mapper) {
        List<T> list = new ArrayList<>(collection);
        return Stream.concat(
            Stream.of(header),
            IntStream.range(0, list.size())
                .mapToObj(index -> index + ". " + mapper.apply(list.get(index)))
        ).collect(Collectors.toList());
    }

    public <T> Component listLocationElements(Collection<T> list, Function<T, Location> locationMapper) {
        return listLocationElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), Object::toString, locationMapper);
    }

    public <T> Component listLocationElements(Collection<T> list, Function<T, String> mapper, Function<T, Location> locationMapper) {
        return listLocationElements(list, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), mapper, locationMapper);
    }

    public <T> Component listLocationElements(@NonNull Collection<T> list, String header, Function<T, String> mapper, Function<T, Location> locationMapper) {
        Component component = TextUtil.component(header);
        for (T t : list) {
            String apply = mapper.apply(t);
            Location location = locationMapper.apply(t);
            Component message = MiniMessage.miniMessage().deserialize(apply + " " + TextUtil.clickableLocationRaw(location));
            component = component.appendNewline().append(message);
        }
        return component;
    }

    public <K, V> List<String> listMapElements(@NonNull Map<K, V> map, Function<Map.Entry<K, V>, String> mapper) {
        return listMapElements(map, LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS), mapper);
    }

    public <K, V> List<String> listMapElements(@NonNull Map<K, V> map, String header, Function<Map.Entry<K, V>, String> mapper) {
        return Stream.of(header)
            .flatMap(string -> Stream.concat(Stream.of(string), map.entrySet().stream().map(mapper)))
            .collect(Collectors.toList());
    }

    public <T> Component listOrderElementsComponent(@NonNull Collection<T> collection, Function<T, Component> mapper) {
        return Component.text(LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_LIST_CONTENTS))
            .appendNewline()
            .append(
                Component.join(
                    JoinConfiguration.newlines(),
                    collection.stream().map(mapper).toList()
                )
            );
    }

    // --------------------------
    public <T> String consumeItemFromList(@NonNull List<T> list, int index, Consumer<T> consumer) {
        if (index < 0 || index >= list.size()) {
            return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_NO_SUCH_ELEMENT);
        }
        consumer.accept(list.get(index));
        return LocalizationManager.getMessage(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE);
    }
}
