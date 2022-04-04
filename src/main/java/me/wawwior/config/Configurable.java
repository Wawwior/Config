package me.wawwior.config;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class Configurable<T extends IConfig> {

    public T config;

    private final Class<T> configClass;

    private final ConfigProvider provider;

    private final String pathName, fileName;

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

    @SuppressWarnings("UnstableApiUsage")
    public final void load() {
        try {
            Gson gson = new Gson();
            config = gson.fromJson(new FileReader(provider.pathName + pathName + String.format("%s.json", fileName)), new TypeToken<T>(getClass()){}.getType());
        } catch (FileNotFoundException e) {
            config = null;
        }
        if (config == null) {
            try {
                config = configClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
    public final void save() {
        File file = new File(provider.pathName + pathName + String.format("%s.json", fileName));
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(config, new TypeToken<T>(getClass()){}.getType()));
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

}
