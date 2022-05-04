package me.wawwior.config;

import me.wawwior.config.io.ConfigStreamAdapter;
import me.wawwior.config.io.AdapterInfo;

/**
 * Used to categorize configs / config trees.
 */
public class ConfigProvider<T extends AdapterInfo> {

    public final ConfigStreamAdapter<T> stream;

    public final boolean listenToChildren;

    /**
     * @param stream
     * @param listenToChildren Whether children of configurables using this provider should be allowed to load/save the whole tree or not
     */
    public ConfigProvider(ConfigStreamAdapter<T> stream, boolean listenToChildren) {
        this.listenToChildren = listenToChildren;
        this.stream = stream;
    }

}
