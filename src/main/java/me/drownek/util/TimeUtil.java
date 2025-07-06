package me.drownek.util;

import me.drownek.util.localization.LocalizationManager;
import me.drownek.util.localization.MessageKey;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeUtil {
    private TimeUtil() {
    }

    public static String formatDuration(Duration duration) {
        return formatTimeMillis(duration.toMillis());
    }

    public static String formatTimeMillisToDate(final long millis) {
        if (millis < 0L) {
            return LocalizationManager.getMessage(MessageKey.TIME_UTIL_NEVER);
        }
        return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date(millis));
    }

    public static String formatTimeMillis(final long millis) {
        if (millis == -1L) {
            return LocalizationManager.getMessage(MessageKey.TIME_UTIL_NEVER);
        }
        long seconds = millis / 1000L;
        if (seconds <= 0L) {
            return "<1s";
        }
        long minutes = seconds / 60L;
        seconds %= 60L;
        long hours = minutes / 60L;
        minutes %= 60L;
        long day = hours / 24L;
        hours %= 24L;
        final long years = day / 365L;
        day %= 365L;
        final StringBuilder time = new StringBuilder();
        if (years != 0L) {
            time.append(years).append("r");
        }
        if (day != 0L) {
            time.append(day).append("d");
        }
        if (hours != 0L) {
            time.append(hours).append("h");
        }
        if (minutes != 0L) {
            time.append(minutes).append("min");
        }
        if (seconds != 0L) {
            time.append(seconds).append("s");
        }
        return time.toString().trim();
    }

    public static long timeFromString(final String string) {
        try {
            if (string.isEmpty()) {
                return 0L;
            }
            StringBuilder builder = new StringBuilder();
            long time = 0L;
            for (final char character : string.toCharArray()) {
                if (Character.isDigit(character)) {
                    builder.append(character);
                } else {
                    final int amount = Integer.parseInt(builder.toString());
                    switch (character) {
                        case 'd': {
                            time += TimeUnit.DAYS.toMillis(amount);
                            break;
                        }
                        case 'h': {
                            time += TimeUnit.HOURS.toMillis(amount);
                            break;
                        }
                        case 'm': {
                            time += TimeUnit.MINUTES.toMillis(amount);
                            break;
                        }
                        case 's': {
                            time += TimeUnit.SECONDS.toMillis(amount);
                            break;
                        }
                    }
                    builder = new StringBuilder();
                }
            }
            return time;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getHour(final long time) {
        final Date date = new Date(time);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        return Integer.parseInt(dateFormat.format(date));
    }

    public static int getMinute(final long time) {
        final Date date = new Date(time);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
        return Integer.parseInt(dateFormat.format(date));
    }
}
