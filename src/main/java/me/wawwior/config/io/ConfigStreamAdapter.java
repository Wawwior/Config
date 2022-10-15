package me.wawwior.config.io;

import com.google.gson.JsonElement;

/**
 * Interface for Adapters reading and writing json to/from any source.
 * @param <T> The type of {@link AdapterInfo} holding information required by the adapter.
 */

public interface ConfigStreamAdapter<T extends AdapterInfo> {

    JsonElement readJson(T info);

    void writeJson(JsonElement json, T info);
}
