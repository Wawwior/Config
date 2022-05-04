package me.wawwior.config.io;

import com.google.gson.JsonElement;

public interface ConfigStreamAdapter<T extends AdapterInfo> {

    JsonElement readJson(T info);

    void writeJson(JsonElement json, T info);
}
