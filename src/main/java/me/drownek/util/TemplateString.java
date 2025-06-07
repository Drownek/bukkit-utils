package me.drownek.util;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class TemplateString extends OkaeriConfig {

    private String message;
    private @Exclude Map<String, Object> fields;

    public TemplateString(String message) {
        this.message = message;
    }

    public static TemplateString of(String message) {
        return new TemplateString(message);
    }

    public TemplateString with(Map<String, Object> replacements) {
        if (fields == null) {
            fields = new LinkedHashMap<>();
        }
        replacements.forEach(this::with);
        return this;
    }

    public TemplateString with(String key, Object value) {
        if (fields == null) {
            fields = new LinkedHashMap<>();
        }
        this.fields.put(key, value);
        return this;
    }

    public String format() {
        String formatted = this.message;
        if (this.fields != null) {
            for (Map.Entry<String, Object> entry : this.fields.entrySet()) {
                formatted = formatted.replace(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return formatted;
    }
}
