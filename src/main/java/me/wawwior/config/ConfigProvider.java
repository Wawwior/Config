package me.wawwior.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigProvider {

    public final String pathName;

    private final List<Configurable<? extends IConfig>> configs = new ArrayList<>();

    @SuppressWarnings("rawtypes")
    private final List<Class<? extends Configurable>> configClasses = new ArrayList<>();

    public ConfigProvider(String path) {
        if (!path.endsWith("/")) path += "/";
        pathName = path;
    }

    public void register(Configurable<? extends IConfig> configurable) {
        if (!configClasses.contains(configurable.getClass())) {
            configs.add(configurable);
            configClasses.add(configurable.getClass());
        }
    }

    public void load() {
        configs.forEach(Configurable::load);
    }

    public void save() {
        configs.forEach(Configurable::save);
    }


}
