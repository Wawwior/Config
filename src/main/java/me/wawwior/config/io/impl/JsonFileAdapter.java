package me.wawwior.config.io.impl;

import com.google.gson.*;
import me.wawwior.config.io.ConfigStreamAdapter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link ConfigStreamAdapter} for (de)serialization to a json file.
 */
public class JsonFileAdapter implements ConfigStreamAdapter<FileInfo> {

    private final String root;

    private final Map<Class<?>, Object> adapters = new HashMap<>();

    public JsonFileAdapter(String root) {
        this.root = root;
    }

    public <C, A extends JsonSerializer<C> & JsonDeserializer<C>> JsonFileAdapter withAdapter(Class<C> clazz, A adapter) {
        adapters.put(clazz, adapter);
        return this;
    }

    @Override
    public JsonElement readJson(FileInfo info) {
        try {

            FileReader reader = new FileReader(format(root + "/" + info.path).substring(1) + String.format("%s.json", info.file));

            JsonElement element = JsonParser.parseReader(reader);

            reader.close();

            return element;

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeJson(JsonElement json, FileInfo info) {

        String path = format(root + "/" + info.path).substring(1);

        File file = new File(path + String.format("%s.json", info.file));

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter writer = new FileWriter(file);

            writer.write(gson.toJson(json));

            writer.close();

        } catch (IOException e) {
            new File(path).mkdir();
            try {
                file.createNewFile();
                writeJson(json, info);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String format(String s) {
        return s.replaceAll("[/\\\\]{2,}|\\\\+|^(?![/\\\\]|\\.*[$/]|\\.*/)|(?<![/\\\\])$", "/").replaceAll("[^\\w/.]", "_");
    }

    public Map<Class<?>, Object> getAdapters() {
        return adapters;
    }
}
