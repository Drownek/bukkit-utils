package me.drownek.util;

public final class MathUtil {
    private MathUtil() {
    }

    public static double round(final double value, final int decimals) {
        final double p = Math.pow(10.0, decimals);
        return Math.round(value * p) / p;
    }

    public static float round(final float value, final int decimals) {
        final double p = Math.pow(10.0, decimals);
        return (float) (Math.round(value * p) / p);
    }
}
