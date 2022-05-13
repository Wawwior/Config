package me.wawwior.config;

import me.wawwior.config.io.ConfigStreamAdapter;
import me.wawwior.config.io.AdapterInfo;

/**
 * Used to categorize configs / config trees.
 */
public class ConfigProvider<T extends AdapterInfo> {

    public final ConfigStreamAdapter<T> adapter;

    public final boolean strict;

    /**
     * @param adapter Adapter used for saving the configurables.
     * @param strict Whether children of configurables using this provider should be allowed to load/save the whole tree or not
     */
    public ConfigProvider(ConfigStreamAdapter<T> adapter, boolean strict) {
        this.strict = strict;
        this.adapter = adapter;
    }

}
