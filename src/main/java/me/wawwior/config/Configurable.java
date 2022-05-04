package me.wawwior.config;


import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import me.wawwior.config.io.ConfigurableInfo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Enables classes extending this to save a Config in the format of T.
 * @param <T> The Config used by this Configurable.
 */
public class Configurable<T extends IConfig, S extends ConfigurableInfo> {

    protected T config;

    private final Class<T> configClass;

    private final ConfigProvider<S> provider;

    private final Map<String, Configurable<? extends IConfig, S>> children = new HashMap<>();

    private boolean child = false;

    private Configurable<? extends IConfig, S> parent;

    private S info;

    /**
     * Constructor for an independent Configurable.
     *
     * @param configClass The class of T.
     * @param info
     * @param provider The {@link ConfigProvider} used by this Configurable.
     */
    public Configurable(Class<T> configClass, S info, ConfigProvider<S> provider) {
        try {
            config = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.configClass = configClass;
        this.provider = provider;

        this.info = info;

    }

    /**
     * Constructor for a Configurable having another Configurable as parent and therefor depending on it.
     * This means that instead of saving this Configurable's config in an individual file, it will be saved in the parents file.
     *
     * @param configClass The class of T.
     * @param parent This Configurables parent.
     * @param id ID used to identify this Configurable in the parent's config.
     */
    public Configurable(Class<T> configClass, Configurable<? extends IConfig, S> parent, String id) {
        try {
            config = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.configClass = configClass;
        this.provider = parent.provider;

        child = true;

        this.parent = parent;

        parent.children.put(id, this);
    }

    private void fromJson(JsonElement element) {

        Gson gson = new Gson();

        config = gson.fromJson(element, type(configClass));

        children.forEach((i, c) -> {
            JsonElement e = ((JsonObject) element).get(i);
            c.fromJson(e);
        });

    }

    /**
     * Load this Configurable's config from the in the constructor defined location.
     */
    public final void load() {

        if (child) {
            if (provider.listenToChildren)
                parent.load();
            else
                System.out.println("Error -> Children of this ConfigProvider cannot call #load()");
            return;
        }

        JsonElement element = provider.stream.readJson(info);

        if (element == null) {
            try {
                config = configClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            fromJson(element);
        }
    }

    private JsonElement toJson() {

        Gson gson = new Gson();

        JsonObject json = (JsonObject) gson.toJsonTree(config, type(configClass));

        children.forEach((i, c) -> json.add(i, c.toJson()));

        return json;
    }

    /**
     * Save this Configurable's config at the in the constructor defined location.
     */
    public final void save() {
        if (child) {
            if (provider.listenToChildren)
                parent.save();
            else
                System.out.println("Error -> Children of this ConfigProvider cannot call #save()");
            return;
        }

        provider.stream.writeJson(toJson(), info);
    }

    private static  Type type(Class<? extends IConfig> c) {
        return TypeToken.of(c).getType();
    }

}
