package me.wawwior.config.io.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.wawwior.config.io.ConfigStreamAdapter;

import java.io.*;

/**
 * Implementation of {@link ConfigStreamAdapter} for (de)serialization to a json file.
 */
public class JsonFileAdapter implements ConfigStreamAdapter<FileInfo> {

    private final String root;

    public JsonFileAdapter(String root) {
        this.root = root;
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
}
