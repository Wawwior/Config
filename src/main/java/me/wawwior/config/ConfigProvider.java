package me.wawwior.config;

import me.wawwior.config.io.ConfigStream;
import me.wawwior.config.io.ConfigurableInfo;

/**
 * Used to categorize configs / config trees.
 */
public class ConfigProvider<T extends ConfigurableInfo> {

    public final ConfigStream<T> stream;

    public final boolean listenToChildren;

    /**
     * @param stream
     * @param listenToChildren Whether children of configurables using this provider should be allowed to load/save the whole tree or not
     */
    public ConfigProvider(ConfigStream<T> stream, boolean listenToChildren) {
        this.listenToChildren = listenToChildren;
        this.stream = stream;
    }

}
