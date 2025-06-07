package me.drownek.util;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class SendableMessage extends OkaeriConfig {

    private BukkitMessageTarget target;
    private String message;
    private transient Map<String, Object> fields;

    public static SendableMessage of(BukkitMessageTarget target, String message) {
        return new SendableMessage(target, message);
    }

    public static SendableMessage of(String message) {
        return of(BukkitMessageTarget.CHAT, message);
    }

    public static SendableMessage of(BukkitMessageTarget target, List<String> message) {
        return new SendableMessage(target, String.join("\n", message));
    }

    public static SendableMessage of(List<String> message) {
        return of(BukkitMessageTarget.CHAT, message);
    }

    public SendableMessage(BukkitMessageTarget target, String message) {
        this.message = message;
        this.target = target;
    }

    public SendableMessage with(String key, Object value) {
        if (fields == null) {
            fields = new LinkedHashMap<>();
        }
        this.fields.put(key, value);
        return this;
    }

    public String format() {
        String formatted = this.message;
        if (this.fields != null) {
            for (Map.Entry<String, Object> entry : this.fields.entrySet()) {
                formatted = formatted.replace(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return formatted;
    }

    public void sendTo(@NonNull CommandSender receiver) {
        switch (this.target) {
            case CHAT:
                TextUtil.message(receiver, Arrays.stream(this.format().split("\n")).toList());
                break;
            case ACTION_BAR:
                if (receiver instanceof Player player) {
                    TextUtil.adventure.sender(player).sendActionBar(TextUtil.component(this.format()));
                }
                break;
            case TITLE:
                if (receiver instanceof Player player) {
                    var format = this.format();
                    var split = format.split("\n");
                    if (split.length > 3) {
                        throw new IllegalArgumentException();
                    }
                    var title = split.length >= 1 ? split[0] : "";
                    var subTitle = split.length >= 2 ? split[1] : "";

                    TextUtil.adventure.sender(player).showTitle(
                        Title.title(
                            TextUtil.component(title),
                            TextUtil.component(subTitle),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1))
                        )
                    );
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported target: " + this.target);
        }
    }

    public void announce() {
        Bukkit.getOnlinePlayers().forEach(this::sendTo);
    }
}
