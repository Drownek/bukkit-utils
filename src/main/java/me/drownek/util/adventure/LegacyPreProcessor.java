package me.drownek.util.adventure;

import java.util.function.UnaryOperator;

public final class LegacyPreProcessor implements UnaryOperator<String> {

    @Override
    public String apply(String component) {
        return component.replace("§", "&");
    }

}
