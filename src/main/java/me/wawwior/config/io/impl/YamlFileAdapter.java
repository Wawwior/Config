package me.wawwior.config.io.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import me.wawwior.config.io.ConfigStreamAdapter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.util.Map;

/**
 * Implementation of {@link ConfigStreamAdapter} for (de)serialization to a yaml file.
 */
public class YamlFileAdapter implements ConfigStreamAdapter<FileInfo> {

    private final String root;

    public YamlFileAdapter(String root) {
        this.root = root;
    }

    @Override
    public JsonElement readJson(FileInfo info) {

        try {
            Yaml yaml = new Yaml();

            FileReader reader = new FileReader(root + info.path + String.format("%s.yml", info.file));
            Object loadedYaml = yaml.load(reader);

            reader.close();

            return new Gson().toJsonTree(loadedYaml);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void writeJson(JsonElement json, FileInfo info) {

        Yaml yaml = new Yaml();

        File file = new File(root + info.path + String.format("%s.yml", info.file));


        try {
            FileWriter writer = new FileWriter(file);

            Map<String, Map<String, Object>> map = yaml.load(new Gson().toJson(json));

            writer.write(yaml.dumpAs(map, Tag.MAP, DumperOptions.FlowStyle.BLOCK));

            writer.close();

        } catch (IOException e) {
            new File(root + info.path).mkdir();
            try {
                file.createNewFile();
                writeJson(json, info);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
