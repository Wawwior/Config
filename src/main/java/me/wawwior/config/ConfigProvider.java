package me.wawwior.config;

import com.google.gson.*;
import me.wawwior.config.io.AdapterInfo;
import me.wawwior.config.io.ConfigStreamAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to categorize configs / config trees.
 */
public class ConfigProvider<T extends AdapterInfo> {

    public final ConfigStreamAdapter<T> adapter;

    public final boolean strict;
    private final HashMap<Class<?>, Object> extensions = new HashMap<>();

    /**
     * @param adapter Adapter used for saving the configurables.
     * @param strict Whether children of configurables using this provider should be allowed to load/save the whole tree or not
     */
    public ConfigProvider(ConfigStreamAdapter<T> adapter, boolean strict) {
        this.strict = strict;
        this.adapter = adapter;
    }

    public <C, A extends JsonSerializer<C> & JsonDeserializer<C>> ConfigProvider<T> withExtension(Class<C> clazz, A adapter) {
        extensions.put(clazz, adapter);
        return this;
    }

    public <C, A extends JsonSerializer<C>, B extends JsonDeserializer<C>> ConfigProvider<T> withExtension(Class<C> clazz, A serializer, B deserializer) {
        extensions.put(clazz, new JsonAdapterPair<>(serializer, deserializer));
        return this;
    }

    public Map<Class<?>, Object> getExtensions() {
        return extensions;
    }

    public static class JsonAdapterPair<T> implements JsonSerializer<T>, JsonDeserializer<T> {

        private final JsonSerializer<T> serializer;
        private final JsonDeserializer<T> deserializer;

        public <A extends JsonSerializer<T>, B extends JsonDeserializer<T>> JsonAdapterPair(A serializer, B deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return deserializer.deserialize(json, typeOfT, context);
        }

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return serializer.serialize(src, typeOfSrc, context);
        }
    }

}
