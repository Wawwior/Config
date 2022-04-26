package me.wawwior.config;


import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Enables classes extending this to save a Config in the format of T.
 * @param <T> The Config used by this Configurable.
 */
public class Configurable<T extends IConfig> {

    protected T config;

    private final Class<T> configClass;

    private final ConfigProvider provider;

    private final Map<String, Configurable<? extends IConfig>> children = new HashMap<>();

    private boolean child = false;

    private Configurable<? extends IConfig> parent;

    private String pathName, fileName;

    /**
     * Constructor for an independent Configurable.
     *
     * @param configClass The class of T.
     * @param path The directory where the config file is located in, having the {@link ConfigProvider provider's} path as root.
     * @param file The name of the config file.
     * @param provider The {@link ConfigProvider} used by this Configurable.
     */
    public Configurable(Class<T> configClass, String path, String file, ConfigProvider provider) {
        try {
            config = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.configClass = configClass;
        this.provider = provider;

        path = path.replace("./", "");
        path = path.replace("../", "");
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (!path.endsWith("/")) path += "/";
        pathName = path;
        fileName = file;
    }

    /**
     * Constructor for a Configurable having another Configurable as parent and therefor depending on it.
     * This means that instead of saving this Configurable's config in an individual file, it will be saved in the parents file.
     *
     * @param configClass The class of T.
     * @param parent This Configurables parent.
     * @param id ID used to identify this Configurable in the parent's config.
     */
    public Configurable(Class<T> configClass, Configurable<? extends IConfig> parent, String id) {
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

        try {

            FileReader reader = new FileReader(provider.pathName + pathName + String.format("%s.json", fileName));

            JsonElement element = JsonParser.parseReader(reader);

            fromJson(element);

            reader.close();

        } catch (FileNotFoundException e) {
            config = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (config == null) {
            try {
                config = configClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
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
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public final void save() {
        if (child) {
            if (provider.listenToChildren)
                parent.save();
            else
                System.out.println("Error -> Children of this ConfigProvider cannot call #save()");
            return;
        }

        File file = new File(provider.pathName + pathName + String.format("%s.json", fileName));

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(file);

            writer.write(gson.toJson(toJson()));

            writer.close();

        } catch (IOException e) {
            new File(provider.pathName + pathName).mkdir();
            try {
                file.createNewFile();
                save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Type type(Class<? extends IConfig> c) {
        return TypeToken.of(c).getType();
    }

}
