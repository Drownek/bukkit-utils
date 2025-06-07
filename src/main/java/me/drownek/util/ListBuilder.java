package me.drownek.util;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ListBuilder<T> {

    private final List<T> list = new ArrayList<>();

    public static <T> ListBuilder<T> builder() {
        return new ListBuilder<>();
    }

    @SafeVarargs
    public static <T> List<T> of(T... values) {
        return new ListBuilder<T>()
                .addAll(Arrays.stream(values)
                        .collect(Collectors.toList()))
                .build();
    }

    public ListBuilder<T> add(T t) {
        this.list.add(t);
        return this;
    }

    public ListBuilder<T> addAll(@NonNull Collection<? extends T> list) {
        this.list.addAll(list);
        return this;
    }

    public List<T> build() {
        return this.list;
    }
}
