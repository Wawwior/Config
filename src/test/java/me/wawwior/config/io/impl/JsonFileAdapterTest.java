package me.wawwior.config.io.impl;

import com.google.gson.*;
import me.wawwior.config.ConfigProvider;
import me.wawwior.config.Configurable;
import me.wawwior.config.IConfig;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonFileAdapterTest {

    public static class TestConfigurable extends Configurable<Config, FileInfo> {

        public TestConfigurable(ConfigProvider<FileInfo> provider, String name) {
            super(Config.class, FileInfo.of("", name), provider);
        }

        public Config getConfig() {
            return config;
        }
    }

    public static class Config implements IConfig {

        public String test = "test";

        public Identifier identifier = new Identifier("this", "test");

    }

    @Test
    void test() {

        ConfigProvider<FileInfo> provider = new ConfigProvider<>(
                new JsonFileAdapter("test"),
                true
        ).withExtension(Identifier.class, new Identifier.Serializer());

        TestConfigurable configurable = new TestConfigurable(provider, "test");

        configurable.save();

        assertTrue(true);

    }

    @Test
    void testWithAdapter() {

        ConfigProvider<FileInfo> provider = new ConfigProvider<>(new JsonFileAdapter("test"), true);

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).create();

        TestConfigurable configurable = new TestConfigurable(provider, "test_with_adapter");

        System.out.println(gson.toJson(configurable.getConfig()));

        configurable.save();

        assertTrue(true);

    }

    public static class Identifier {
        private final String path;
        private final String name;

        public Identifier(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public String toString() {
            return path + ":" + name;
        }

        public static class Serializer implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

            @Override
            public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String[] split = json.getAsString().split(":");
                return new Identifier(split[0], split[1]);
            }

            @Override
            public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        }

    }
}