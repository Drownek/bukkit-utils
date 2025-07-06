package me.drownek.util.message;

import lombok.Getter;
import lombok.NonNull;
import me.drownek.util.SoundDispatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class AudibleMessage extends SendableMessage {

    private SoundDispatcher sound;

    public AudibleMessage(BukkitMessageTarget target, String message, SoundDispatcher sound) {
        super(target, message);
        this.sound = sound;
    }

    public AudibleMessage(String message, SoundDispatcher sound) {
        super(BukkitMessageTarget.CHAT, message);
        this.sound = sound;
    }

    public AudibleMessage(String message) {
        super(BukkitMessageTarget.CHAT, message);
        this.sound = SoundDispatcher.defaultSound();
    }

    public static AudibleMessage of(BukkitMessageTarget target, String message, SoundDispatcher sound) {
        return new AudibleMessage(target, message, sound);
    }

    public static AudibleMessage of(String message, SoundDispatcher sound) {
        return new AudibleMessage(message, sound);
    }

    public static AudibleMessage of(String message) {
        return new AudibleMessage(message);
    }

    @Override
    public void sendTo(@NonNull CommandSender receiver) {
        if (receiver instanceof Player player) {
            sound.play(player);
        }
        super.sendTo(receiver);
    }
}
