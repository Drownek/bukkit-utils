package me.drownek.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@UtilityClass
public final class RandomUtil {

    public boolean chance(float chance) {
        return chance >= 1.0F || chance > 0.0F && ThreadLocalRandom.current().nextFloat() < chance;
    }

    public double randomDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public <T> T randomElement(@NonNull List<T> list) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty!");
        }
        int index = randomInteger(0, list.size() - 1);
        return list.get(index);
    }

    public static <T> T randomElementWithoutGivenElement(@NonNull List<T> list, T element) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty!");
        }

        long distinctElementCount = list.stream().filter(e -> !e.equals(element)).count();

        if (distinctElementCount == 0) {
            throw new RuntimeException("No distinct element found!");
        }

        T randomElement;
        do {
            randomElement = randomElement(list);
        } while (randomElement.equals(element));

        return randomElement;
    }

    public static <T> T randomElementWithoutGivenElementExceptOnlyOneElementInList(@NonNull List<T> list, T element) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty!");
        }

        long distinctElementCount = list.stream().filter(e -> !e.equals(element)).count();

        if (distinctElementCount == 0) {
            return element;
        }

        T randomElement;
        do {
            randomElement = randomElement(list);
        } while (randomElement.equals(element));

        return randomElement;
    }

    public <T> List<T> randomElements(@NonNull List<T> list, int n) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty!");
        }
        List<T> elements = new ArrayList<>();
        IntStream.range(0, n).forEach(value -> elements.add(list.get(randomInteger(0, list.size() - 1))));
        return elements;
    }

    public int randomInteger(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int randomDigit() {
        return randomInteger(1, 9);
    }

    public List<Integer> randomIntegers(final int count, final int min, final int max) {
        return IntStream.range(0, count).map(operand -> randomInteger(min, max)).boxed().toList();
    }

    public List<Integer> randomIntegersNoRepeat(final int count, final int min, final int max) {
        if (count > (max - min + 1)) {
            throw new IllegalArgumentException("Count cannot be greater than the range size.");
        }

        Set<Integer> uniqueIntegers = new HashSet<>();
        while (uniqueIntegers.size() < count) {
            uniqueIntegers.add(randomInteger(min, max));
        }

        return new ArrayList<>(uniqueIntegers);
    }

    public static float randomFloat(float min, float max) {
        return (float) randomDouble(min, max + 1);
    }
}
