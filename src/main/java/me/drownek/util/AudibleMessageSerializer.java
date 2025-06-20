package me.drownek.util;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class AudibleMessageSerializer implements ObjectSerializer<AudibleMessage> {
    @Override
    public boolean supports(@NonNull Class<? super AudibleMessage> type) {
        return AudibleMessage.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull AudibleMessage object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("target", object.getTarget());
        data.add("message", object.getMessage());
        data.add("sound", object.getSound());
    }

    @Override
    public AudibleMessage deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        BukkitMessageTarget target = data.get("target", BukkitMessageTarget.class);
        String message = data.get("message", String.class);
        SoundDispatcher sound = data.get("sound", SoundDispatcher.class);
        return new AudibleMessage(target, message, sound);
    }
}
