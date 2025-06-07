package me.drownek.util;

import dev.lone.itemsadder.api.CustomFurniture;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class FurnitureInfo extends OkaeriConfig {

    private @NonNull String id;
    private @NonNull Location location;

    public FurnitureInfo(@NonNull String id, @NonNull Location location) {
        this.id = id;
        this.location = location.getBlock().getLocation();
    }

    public static FurnitureInfo of(@NonNull CustomFurniture customFurniture) {
        Objects.requireNonNull(customFurniture.getEntity());

        Location location = customFurniture.getEntity().getLocation();
        return new FurnitureInfo(customFurniture.getId(), location);
    }

    public static FurnitureInfo of(@NonNull String furnitureId, @NonNull Location furnitureLocation) {
        return new FurnitureInfo(furnitureId, furnitureLocation);
    }

    public void spawn() {
        CustomFurniture.spawn(id, location.getBlock());
    }

    public void removeAllInLocation() {
        location.getWorld().getEntities().stream().filter(entity -> entity instanceof ArmorStand).forEach(entity -> {
            if (LocationUtil.isSimilarExceptRotation(entity.getLocation().getBlock().getLocation(), location.getBlock().getLocation())) {
                entity.remove();
            }
        });
        var customFurniture = CustomFurniture.byAlreadySpawned(location.getBlock());
        if (customFurniture != null) {
            customFurniture.remove(false);
        }
    }

    public boolean isSimilar(CustomFurniture customFurniture) {
        if (customFurniture == null || customFurniture.getEntity() == null) {
            return false;
        }
        Location customFurnitureLocation = customFurniture.getEntity().getLocation().getBlock().getLocation();
        return (Objects.equals(this.id, customFurniture.getId()) || Objects.equals(this.id, customFurniture.getNamespacedID())) &&
               Objects.equals(this.location.getX(), customFurnitureLocation.getX()) &&
               Objects.equals(this.location.getY(), customFurnitureLocation.getY()) &&
               Objects.equals(this.location.getZ(), customFurnitureLocation.getZ());
    }


    public boolean isSimilarExceptId(CustomFurniture customFurniture) {
        if (customFurniture == null || customFurniture.getEntity() == null) {
            return false;
        }

        Location customFurnitureLocation = customFurniture.getEntity().getLocation().getBlock().getLocation();
        return Objects.equals(this.location.getX(), customFurnitureLocation.getX()) &&
               Objects.equals(this.location.getY(), customFurnitureLocation.getY()) &&
               Objects.equals(this.location.getZ(), customFurnitureLocation.getZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Location customFurnitureLocation = ((FurnitureInfo) obj).getLocation().getBlock().getLocation();
        return Objects.equals(this.location.getX(), customFurnitureLocation.getX()) &&
               Objects.equals(this.location.getY(), customFurnitureLocation.getY()) &&
               Objects.equals(this.location.getZ(), customFurnitureLocation.getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.location.getX(), this.location.getY(), this.location.getZ());
    }

    @Override
    public String toString() {
        return this.id + " w lokacji " + TextUtil.prettyFormatLocation(this.location);
    }
}
