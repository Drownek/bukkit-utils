package me.drownek.util;

import dev.lone.itemsadder.api.CustomFurniture;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.Optional;

@UtilityClass
public class FurnitureUtil {

    public Optional<CustomFurniture> getFurnitureByLocation(final @NonNull Location location) {
        return Optional.ofNullable(CustomFurniture.byAlreadySpawned(location.getBlock()));
    }

    public void removeAllFurnituresInLocation(final @NonNull Location location) {
        if (location.getWorld() == null) {
            return;
        }

        location.getWorld().getEntitiesByClass(ArmorStand.class).stream()
            .filter(armorStand -> {
                Location armorStandBlockLocation = armorStand.getLocation().getBlock().getLocation();
                return LocationUtil.isSimilarExceptRotation(armorStandBlockLocation, location.getBlock().getLocation());
            })
            .forEach(Entity::remove);
    }
}
