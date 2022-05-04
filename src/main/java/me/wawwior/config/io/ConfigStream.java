package me.wawwior.config.io;

import com.google.gson.JsonElement;

public interface ConfigStream<T extends ConfigurableInfo> {

    JsonElement readJson(T info);

    void writeJson(JsonElement json, T info);
}
