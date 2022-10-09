package me.wawwior.config.io;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for Adapters reading and writing json to/from any source.
 * @param <T> The type of {@link AdapterInfo} holding information required by the adapter.
 */

public interface ConfigStreamAdapter<T extends AdapterInfo> {

    JsonElement readJson(T info);

    void writeJson(JsonElement json, T info);

    default Map<Class<?>, Object> getAdapters() {
        return null;
    }
}
