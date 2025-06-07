package me.drownek.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class LocationUtil {
    private LocationUtil() {
    }

    public static boolean isSimilarExceptRotation(final Location first, final Location second) {
        return first.getX() == second.getX() && first.getY() == second.getY() && first.getZ() == second.getZ();
    }

    public static double distance(final Location first, final Location second) {
        return Math.max(Math.abs(first.getX() - second.getX()), Math.abs(first.getZ() - second.getZ()));
    }

    public static boolean isInRadius(final Location entityLocation, final Location center, final int radius) {
        return distance(center, entityLocation) < radius;
    }

    public static boolean loc(final int minX, final int maxX, final int minZ, final int maxZ, final Location l) {
        final double[] dim = {minX, maxX};
        Arrays.sort(dim);
        if (l.getX() >= dim[1] || l.getX() <= dim[0]) {
            return false;
        }
        dim[0] = minZ;
        dim[1] = maxZ;
        Arrays.sort(dim);
        return l.getZ() < dim[1] && l.getZ() > dim[0];
    }

    public static Location toCenter(final Location location) {
        final Location centerLoc = location.clone();
        centerLoc.setX(location.getBlockX() + 0.5);
        centerLoc.setY(location.getBlockY() + 0.5);
        centerLoc.setZ(location.getBlockZ() + 0.5);
        return centerLoc;
    }

    public static List<? extends Player> getPlayersWithin(final Location location, final float radius) {
        return Bukkit.getServer().getOnlinePlayers().stream()
            .filter(player -> player.getLocation().distance(location) <= radius)
            .toList();
    }
}
