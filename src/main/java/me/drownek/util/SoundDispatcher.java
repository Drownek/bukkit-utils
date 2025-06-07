package me.drownek.util;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class SoundDispatcher extends OkaeriConfig {

    private String sound;

    public static SoundDispatcher defaultSound() {
        return new SoundDispatcher("minecraft:block.anvil.place");
    }

    public void play(final Player player) {
        player.playSound(player.getLocation(), this.sound, 1.0F, 1.0F);
    }
}
